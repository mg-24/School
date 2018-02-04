package zadanie2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONException;
import org.json.JSONObject;

public class Recommender {
	static TransportClient client = null;

	public static void main(String args[]) throws IOException, JSONException, SQLException {
		Recommender connection = new Recommender();	
		connection.connect();
		
		HashMap<Integer, ArrayList<Integer>> userRecommendedItem = new HashMap<Integer, ArrayList<Integer>>();
		HashMap<Integer, ArrayList<Integer>> finalRecommendedItem = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> userIds = new ArrayList<Integer>();
		
		try {
			userIds = connection.getTestUsers();
			connection.connectToElastic();
			if (!client.admin().indices().prepareExists("dealitems").execute().actionGet().isExists()) {
				connection.dataToElasticNotTimeDetect();
			}
			if (!client.admin().indices().prepareExists("relevantdealitems").execute().actionGet().isExists()) {
				connection.relevantDataToElastic();
			}
			userRecommendedItem = connection.recommendmostSelledProductsOnAllData(userIds);
			finalRecommendedItem = connection.recommendDataOnTextContext(client, userIds, userRecommendedItem);
			connection.countSuccess(finalRecommendedItem);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void connectToElastic() throws IOException, JSONException {

		Settings settings = Settings.builder()
				.put("cluster.name", "elasticsearch")
				.put("node.name", "DESKTOP").build();
		try {
			this.client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(
							InetAddress.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

	public void pushToElastic(ArrayList<String> jsons) {

		Settings settings = Settings.builder().put("cluster.name", "elasticsearch")
				.put("node.name", "DESKTOP").build();
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress
							.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		int counter = 0;
		//V premennej jsons mam dany JSON produktov, ktore chcem vlozit do ELasticu
		while (counter  != jsons.size()) {
		IndexResponse indexes = client.prepareIndex("relevantdealitems", "userdealitem", "zlavadna"+Integer.toString(counter))
				.setSource(jsons.get(counter), XContentType.JSON).get();
		
		counter++;
		}
	}
	
	//Connection na databazu SQL
	private Connection connect() {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:vi_db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return c;
	}
	
	//zobrat z testu idusera a jeho deal a pozret sa do userRecommendedItem hasmapy
	//zober usera z hasmapy a prejdi jeho produkty a porovnavaj kazdy produkt postupne s produktom z testu
	//countuj pocet v teste
	//countuj pocet hitov
	public void countSuccess(HashMap<Integer, ArrayList<Integer>> userRecommendedItem) throws SQLException {
		
		//tu je mozne dat LIMIT na recommend pre menej pouzivatelov
		String sql = "SELECT CAST(user_id AS INTEGER) AS user_id, deal_id FROM test_activity ORDER BY user_id ASC";
		Connection conn = this.connect();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		int hits = 0;
		double averageSuccess = 0.0;
		double tempSuccess = 0.0;
		int count = 0;
		
		//kolkokrat som ho navstivil to znamena, kolko mal realne nakupov
		HashMap<Integer, Integer> visitedUser = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> userHits = new HashMap<Integer, Integer>();
		
		try {
			while (rs.next()) {
				//Ak sa dany user nachadza v hashmape
				if (userRecommendedItem.containsKey(rs.getInt("user_id"))) {
						//ak som usera uz raz navstivil tak ho vlozim do hasmapy navstivenych a zvysim pocet navstivenosti +1
						if (visitedUser.containsKey(rs.getInt("user_id"))) {
							visitedUser.put(rs.getInt("user_id"), visitedUser.get(rs.getInt("user_id"))+1);
							
							//prejdem vsetky recomendovane produkty daneho usera
							for (Integer recommendedItem: userRecommendedItem.get(rs.getInt("user_id"))) {
								//System.out.println("USER  -  ODPORUCANE PRODUKTY : " + Integer.parseInt(rs.getString("user_id"))+"  -  "+userRecommendedItem.get(Integer.parseInt(rs.getString("user_id"))));  
								
								//ak deal z DB == jednemu z daneho recomendovaneho dealu, tak mame hit
								if (Integer.parseInt(rs.getString("deal_id")) == recommendedItem) {
									hits++;
								}
							}
							
							//ak v userHitoch je uz dany user tak zvysim jeho hity
							if (userHits.containsKey(rs.getInt("user_id"))) {
								userHits.put(rs.getInt("user_id"), userHits.get(rs.getInt("user_id"))+hits);
								hits = 0;
							}
							//inak mu priradim hity a vlozim ho danemu userovi ako hity
							else {
								userHits.put(rs.getInt("user_id"), hits);
								hits = 0;
							}
						}
						// ak sa ten user nenavstivil este, tak ho dam do hashmapy
						else {
							visitedUser.put(rs.getInt("user_id"), 1);
							for (Integer recommendedItem: userRecommendedItem.get(rs.getInt("user_id"))) {
								if (Integer.parseInt(rs.getString("deal_id")) == recommendedItem) {
									hits++;
								}
							}
							//ak v userHitoch je uz dany user tak zvysim jeho hity
							if (userHits.containsKey(rs.getInt("user_id"))) {
								userHits.put(rs.getInt("user_id"), userHits.get(rs.getInt("user_id"))+hits);
								hits = 0;
							}
							//inak mu priradim hity a vlozim ho danemu userovi ako hity
							else {
								userHits.put(rs.getInt("user_id"), hits);
								hits = 0;
							}
						}
				 }
			}
		} catch (SQLException e) {
		System.out.println(e.getMessage());
		}
		
		//Pocitanie uspesnosti (pocet hitov / min (pocet nakupov, pocet recomendovanych produktov))
		for(HashMap.Entry<Integer, Integer> m: userHits.entrySet()) {
				count++;
				tempSuccess = ((double)m.getValue() / (double)Math.min(visitedUser.get(m.getKey()), 10));
				tempSuccess = tempSuccess * 100;
				averageSuccess += tempSuccess;
				System.out.println(tempSuccess);				
		}
		System.out.println("Priemerna presnost je: "+averageSuccess/count +" %");
	}
	
	//Zoberie vsetkych testovacich userov (ich IDcka)
	public ArrayList<Integer> getTestUsers() throws SQLException {
		ArrayList<Integer> testUserIds = new ArrayList<Integer>();
		
		//Mozne zmenit LIMIT pre menej pouzivatelov
		String sql = "SELECT CAST(user_id AS INTEGER) AS user_id from test_activity GROUP BY user_id ORDER BY user_id ASC";
		Connection conn = this.connect();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		int count = 0;
		try {
			while (rs.next()) {
				testUserIds.add(rs.getInt("user_id"));
			}
		} catch (SQLException e) {
		System.out.println(e.getMessage());
		}
		
		for(Integer list: testUserIds){
			count++;
		}
		
		return testUserIds;
	}
	
	//Vlozi vsetky data do elasticu okrem tych, ktore maju len 1 znak nejaky v title
	public void dataToElasticNotTimeDetect() throws SQLException, JSONException {
		String sql = "SELECT id, title_deal, title_desc, title_city, deal_id, gpslat, gpslong FROM train_deal_details "
				+ "WHERE title_deal != 'NULL' AND title_desc != 'NULL' AND title_city != 'NULL' AND title_deal != ',' AND "+ 
				"title_desc != ',' AND title_city != ',' AND "+
				"title_deal != 'x' AND title_desc != 'x' AND title_city != 'x' AND "+ 
				"title_deal != '.x' AND title_desc != '.x' AND title_city != '.x' AND "+
				"title_deal != '.,' AND title_desc != '.,' AND title_city != '.,' AND "+ 
				"title_deal != ',.' AND title_desc != ',.' AND title_city != ',.' AND "+ 
				"title_deal != ',x' AND title_desc != ',x' AND title_city != ',x' AND "+
				"title_deal != '.' AND title_desc != '.' AND title_city != '.' AND "+
				"title_deal != 't' AND title_desc != 't' AND title_city != 't' AND "+
				"title_deal != 'r' AND title_desc != 'r' AND title_city != 'r' AND "+
				"title_deal != 'a' AND title_desc != 'a' AND title_city != 'a' AND "+
				"title_deal != 'z' AND title_desc != 'z' AND title_city != 'z' AND "+
				"title_deal != '..' AND title_desc != '..' AND title_city != '..' AND "+
				"title_deal != 'xx' AND title_desc != 'xx' AND title_city != 'xx' AND "+
				"title_deal != 's' AND title_desc != 's' AND title_city != 's' AND "+
				"title_deal != '1' AND title_desc != '1' AND title_city != '1'"+
				"GROUP BY deal_id";
		ArrayList<String> listOfJsons = new ArrayList<String>();
		Connection conn = this.connect();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		try {

			while (rs.next()) {
				//Tvorba JSONu
				JSONObject object = new JSONObject();
				object.put("Id", rs.getString("id"));
				object.put("title_deal", rs.getString("title_deal"));
				object.put("title_desc", rs.getString("title_desc"));
				object.put("title_city", rs.getString("title_city"));
				object.put("deal_id", rs.getString("deal_id"));
				object.put("gpslat", rs.getString("gpslat"));
				object.put("gpslong", rs.getString("gpslong"));
				listOfJsons.add(object.toString());
			}
			pushToElastic(listOfJsons);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//data do Elasticu - beriem do uvahy tie ktorych coupon_end_time > ako posledny create_time z trainu
	public void relevantDataToElastic() throws SQLException, JSONException {
		String sql = "SELECT A.id, A.title_deal, A.title_desc, A.title_city, A.deal_id, A.gpslat, A.gpslong FROM train_deal_details AS A "+
				"LEFT JOIN train_dealitems AS D ON A.id = D.deal_id "+
				"WHERE coupon_end_time > (SELECT create_time FROM train_activity "+
				"ORDER BY create_time DESC LiMIT 1) AND "
				+ "title_deal != 'NULL' AND title_desc != 'NULL' AND title_city != 'NULL' AND title_deal != ',' AND "
				+ "title_desc != ',' AND title_city != ',' AND title_deal != 'x' AND title_desc != 'x' AND "
				+ "title_city != 'x' AND title_deal != '..' AND title_desc != '..' AND title_city != '..' AND "
				+ "title_deal != 'r' AND title_desc != 'r' AND title_city != 'r' AND title_deal != '.' AND title_desc != '.' AND "
				+ "title_city != '.' AND title_deal != 'xx' AND title_desc != 'xx' AND title_city != 'xx' AND title_deal != 's' AND "
				+ "title_desc != 's' AND title_city != 's' AND title_deal != '1' AND title_desc != '1' AND "
				+ "title_city != '1' GROUP BY A.deal_id";
		ArrayList<String> listOfJsons = new ArrayList<String>();
		Connection conn = this.connect();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		try {

			while (rs.next()) {
				JSONObject object = new JSONObject();
				object.put("Id", rs.getString("id"));
				object.put("title_deal", rs.getString("title_deal"));
				object.put("title_desc", rs.getString("title_desc"));
				object.put("title_city", rs.getString("title_city"));
				object.put("deal_id", rs.getString("deal_id"));
				object.put("gpslat", rs.getString("gpslat"));
				object.put("gpslong", rs.getString("gpslong"));
				listOfJsons.add(object.toString());
			}
			pushToElastic(listOfJsons);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//TOP 10 produktov relevantnych (kde coupon end time > ako posledny create time z trainu - teda najblizsi k testu)
	public HashMap<Integer, ArrayList<Integer>> recommendmostSelledProducts(ArrayList<Integer> userIds) throws SQLException {
		ArrayList<Integer> mostSelled = new ArrayList<Integer>();
		HashMap<Integer, ArrayList<Integer>> userRecommendedItem = new HashMap<Integer, ArrayList<Integer>>();
		String sql = "SELECT A.* from train_deal_details AS A JOIN train_dealitems AS D ON A.id = D.deal_id "+
				"INNER JOIN (SELECT deal_id, COUNT(*) AS EZ from train_activity "+
				"GROUP BY deal_id "+
				"ORDER BY EZ DESC) AS act "+
				"ON act.deal_id = A.id "+
				"WHERE coupon_end_time > (SELECT create_time FROM train_activity "+
				"ORDER BY create_time DESC "+
				"LIMIT 1) AND title_deal != 'NULL' AND title_desc != 'NULL' AND title_city != 'NULL' AND title_deal != ',' AND "
				+ "title_desc != ',' AND title_city != ',' AND title_deal != 'x' AND title_desc != 'x' AND "
				+ "title_city != 'x' AND title_deal != '..' AND title_desc != '..' AND title_city != '..' AND "
				+ "title_deal != 'r' AND title_desc != 'r' AND title_city != 'r' AND title_deal != '.' AND "
				+ "title_desc != '.' AND title_city != '.' AND title_deal != 'xx' AND title_desc != 'xx' AND "
				+ "title_city != 'xx' AND title_deal != 's' AND title_desc != 's' AND title_city != 's' AND "
				+ "title_deal != '1' AND title_desc != '1' AND title_city != '1' GROUP BY A.deal_id ORDER BY act.EZ DESC LIMIT 10";
		Connection conn = this.connect();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
			try {
				while (rs.next()) {
					mostSelled.add(Integer.parseInt(rs.getString("deal_id")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
			for (Integer userId: userIds) {
				userRecommendedItem.put(userId, mostSelled);
			}
			
			return userRecommendedItem;
	}
	
	//TOP 10 ultimate
	public HashMap<Integer, ArrayList<Integer>> recommendmostSelledProductsOnAllData(ArrayList<Integer> userIds) throws SQLException {
		ArrayList<Integer> mostSelled = new ArrayList<Integer>();
		HashMap<Integer, ArrayList<Integer>> userRecommendedItem = new HashMap<Integer, ArrayList<Integer>>();
		String sql = "SELECT A.* from train_deal_details AS A JOIN train_dealitems AS D ON A.id = D.deal_id "+
				"INNER JOIN (SELECT deal_id, COUNT(*) AS EZ from train_activity "+
				"GROUP BY deal_id "+
				"ORDER BY EZ DESC) AS act "+
				"ON act.deal_id = A.id "+
				"WHERE title_deal != 'NULL' AND title_desc != 'NULL' AND title_city != 'NULL' AND title_deal != ',' AND "
				+ "title_desc != ',' AND title_city != ',' AND title_deal != 'x' AND title_desc != 'x' AND "
				+ "title_city != 'x' AND title_deal != '..' AND title_desc != '..' AND title_city != '..' AND "
				+ "title_deal != 'r' AND title_desc != 'r' AND title_city != 'r' AND title_deal != '.' AND "
				+ "title_desc != '.' AND title_city != '.' AND title_deal != 'xx' AND title_desc != 'xx' AND "
				+ "title_city != 'xx' AND title_deal != 's' AND title_desc != 's' AND title_city != 's' AND "
				+ "title_deal != '1' AND title_desc != '1' AND title_city != '1' GROUP BY A.deal_id ORDER BY act.EZ DESC LIMIT 10";
		Connection conn = this.connect();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
			try {
				while (rs.next()) {
					mostSelled.add(Integer.parseInt(rs.getString("deal_id")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
			for (Integer userId: userIds) {
				userRecommendedItem.put(userId, mostSelled);
			}
			
			return userRecommendedItem;
	}
	
	//Recommendovanie produktov na zaklade podobnosti (title, title_desc a podobne) - content-based
	public HashMap<Integer, ArrayList<Integer>> recommendDataOnTextContext(TransportClient client, ArrayList<Integer> testUserIds, 
			HashMap<Integer, ArrayList<Integer>> mostSelledProductsInValue) throws SQLException {
		
		HashMap<Integer, ArrayList<Integer>> userRecommendItem = new HashMap<Integer, ArrayList<Integer>>();
		String sql = "SELECT CAST(A.user_id AS INTEGER) AS user_id, CAST(A.market_price AS INTEGER) AS market_price, "
				+ "CAST(A.team_price AS INTEGER) AS team_price, D.deal_id, D.title_deal, D.title_desc, D.title_city"
				+ " from train_activity AS A "
				+ "JOIN train_deal_details AS D ON A.deal_id = D.id ORDER BY user_id";
		
		//Tu som skusal produkty ktore boli v nejakej akcii
		/*String onSaleProduct = "SELECT A.*, D.title_deal FROM train_activity AS A JOIN train_deal_details AS D ON A.deal_id = D.deal_id"
				+ " WHERE CAST(team_price AS INTEGER) > CAST(market_price * 0.4 AS INTEGER)"
				+ " and CAST(team_price AS INTEGER) < CAST(market_price * 0.95 AS INTEGER) and CAST(market_price AS INTEGER) < 1000 "
				+ "GROUP BY A.deal_id ORDER BY CAST(market_price AS INTEGER) DESC LIMIT 2";*/
		ArrayList<SearchRequestBuilder> responses = new ArrayList<SearchRequestBuilder>();
		
		HashMap<Integer, ArrayList<DealDetails>> trainUsersInfo = new HashMap<Integer, ArrayList<DealDetails>>();
		
		HashMap<Integer, Integer> titleDuplicities = new HashMap<Integer, Integer>();
		LinkedList<String> hits = new LinkedList<String>();
		
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		Connection conn = this.connect();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
			try {
				while (rs.next()) {
					ArrayList<DealDetails> deal = new ArrayList<DealDetails>();
					DealDetails details = new DealDetails();
					
					//userov z trainu si dam do hashmapy - Ak tam uz je tak zoberem jeho arrayList dealov a pridam mu dalsi
					if (trainUsersInfo.containsKey(rs.getInt("user_id"))) {
						deal = trainUsersInfo.get(rs.getInt("user_id"));
						details.setDeal_id(Integer.parseInt(rs.getString("deal_id")));
						details.setTitle_deal(rs.getString("title_deal"));
						details.setTitle_desc(rs.getString("title_desc"));
						details.setTitle_city(rs.getString("title_city"));
						deal.add(details);
						trainUsersInfo.put(rs.getInt("user_id"), deal);
					}
					
					//Ak tam user v hashmape este nie je, tak mu pridam len dany deal do hashmapy
					else {
						details.setDeal_id(Integer.parseInt(rs.getString("deal_id")));
						details.setTitle_deal(rs.getString("title_deal"));
						details.setTitle_desc(rs.getString("title_desc"));
						details.setTitle_city(rs.getString("title_city"));
						deal.add(details);
						trainUsersInfo.put(rs.getInt("user_id"), deal);
					}
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		//prechadzam vsetkych userov v teste a pozeram sa, ci v hashmape trainUserov sa nachadze testUser
		//ak sa tam testUser nachadza tak pre neho chcem vygenerovat odporucanie na zaklade textContextu z elasticu
		int c = 0;
		ArrayList<String> productsAction = new ArrayList<String>();
		//ResultSet productsOnBiggerSale = stmt.executeQuery(onSaleProduct);
		
		/*while (productsOnBiggerSale.next()) {
			productsAction.add(productsOnBiggerSale.getString("title_deal"));
		}*/
		
		for (Integer testUser: testUserIds) {
			int duplicityTitle = 0;
			if (trainUsersInfo.containsKey(testUser))	{
				
				query = QueryBuilders.boolQuery();
				
				//Pocitam si pocet rovnakym nazvov produktov k danemu userovi
				for (DealDetails deal: trainUsersInfo.get(testUser)) {
					for (DealDetails deal2: trainUsersInfo.get(testUser)) {
						if (deal.getDeal_id() == deal2.getDeal_id()) {
							duplicityTitle++;
						}
					}
					titleDuplicities.put(deal.getDeal_id(), duplicityTitle - 1);
					duplicityTitle = 0;
				}
				
				//Vsetko to appendujem do should klazul - prechadzam produkty	
				for (DealDetails deal: trainUsersInfo.get(testUser)) {
					
					//Tu som skusal recommendom aj podla title_desc
						/*query.should(QueryBuilders
							.matchQuery("title_desc", deal.getTitle_desc())
							.fuzziness(Fuzziness.AUTO)
							.minimumShouldMatch("30%"));*/
					//Ak je pocet rovnakych titleov v nakupoch vacsich ako 1 (cize si ho kupoval aspon 2krat) tak ho boostnem
						if (titleDuplicities.get(deal.getDeal_id()) >= 1) {
						query.should(QueryBuilders
								.matchQuery("title_deal", deal.getTitle_deal())
								.fuzziness(Fuzziness.AUTO)
								.minimumShouldMatch("70%").boost(1.0f));
						}
						if (titleDuplicities.get(deal.getDeal_id()) == 0) {
						query.should(QueryBuilders
								.matchQuery("title_deal", deal.getTitle_deal())
								.fuzziness(Fuzziness.AUTO)
								.minimumShouldMatch("50%"));
						}
				}
				
				//Response 10 dealov z elasticu
				responses.add(client
						.prepareSearch("relevantdealitems")
						.setQuery(query)
						.setExplain(false)
						.setSize(10)
						);
				
				//Ak je response z elasticu < 10 produktov, tak dopln z top 10 najpredavanejsich ostatok
				int count = 0;
				hits = getHits(responses.get(responses.size() - 1).execute().actionGet());
				ArrayList<Integer> recommendedItemsForOneUser = new ArrayList<Integer>();
				for (String hit: hits) {
					count++;
					recommendedItemsForOneUser.add(Integer.parseInt(hit));				
				}
				
				for (Integer product: mostSelledProductsInValue.get(testUser)) {
					if (count < 10) {
						recommendedItemsForOneUser.add(product);
						count++;
					}
					else break;
				}
				
				userRecommendItem.put(testUser, recommendedItemsForOneUser);
				
				responses = new ArrayList<SearchRequestBuilder>();	
			}
			
			//AK v traine neni test user tak test userovi daj top produkty alebo nieco ine TU!
			else {
				//System.out.println(mostSelledProductsInValue.get(testUser));
				userRecommendItem.put(testUser, mostSelledProductsInValue.get(testUser));
				
			}
			
			//Vypis mi pocet percent vykonania programu
			c++;
			double perc = (c / 50000.0) * 100.0;
			if (perc % 1 == 0)
				System.out.println((c / 50000.0) * 100 + "%");
		}
		
		return userRecommendItem;
	}
	
	public LinkedList<String> getHits(SearchResponse response) {
			
			LinkedList<String> hits = new LinkedList<String>();
			
			// Vsetky hity co vrati
			for (SearchHit hit : response.getHits()) {
						hits.add(hit.getSource().get("deal_id").toString());			
			}
					
			return hits;
		}
}
