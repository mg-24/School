import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;


import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Elastic {

	static TransportClient client = null;
	private static String USER_AGENT = null;
	public static ArrayList<String> highliter = new ArrayList<String>();
	public static String highlightResponse;
	public static int numberOfWords;

	@SuppressWarnings("unchecked")
	public void connect() throws IOException, JSONException {

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

		queryMaker(client);
	}
	
	public void elasticDataPush() throws IOException{
		
		int numberOfFiles = 37069;
		int index = 0;
		String path = "C:/Users/Mat˙ö/workspace/VIZadanie/";
		
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
		
		while (index <= numberOfFiles) { 
		  String json = new String(Files.readAllBytes(Paths.get(path + index)),
				  StandardCharsets.UTF_8);
	  
		  IndexResponse indexes = client.prepareIndex("produkty", "produkt",
		  Integer.toString(index)).setSource(json, XContentType.JSON).get();
	  
		  System.out.println(index + " " + indexes.status());
	  
		  index++;
	  
	  }
	}

	public void queryMaker(final TransportClient client) throws JSONException {

		final Window window = new Window();
		final LinkedList<String> urls = new LinkedList<String>();
		final LinkedList<SearchRequestBuilder> responses = new LinkedList<SearchRequestBuilder>();
		final List<String> autocompleteList = null;
		//SearchRequestBuilder response;

		// Klik na riadok v tabulke
		window.table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = window.table.rowAtPoint(evt.getPoint());
				int col = window.table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {

					URI uri = null;

					try {
						uri = new URI(urls.get(row));
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					open(uri);

				}
			}
		});
		
		window.queryField.addKeyListener(new KeyAdapter() {
			
			public void keyTyped(KeyEvent arg0) {
				
				List<String> autocompleteList = autocomplete(window.queryField.getText(), "N·zov");
				window.autoCompleteArea.setText("");
				
				if (!autocompleteList.isEmpty()) {
					
					window.autoCompleteArea.append("MyslÌte: " + "\n");
					
					for (String suggest: autocompleteList) {
						window.autoCompleteArea.append(suggest + "\n");
					}
				}
				else
					window.autoCompleteArea.append("Nerozumieme Ëo myslÌte alebo vidÌte svoje v˝sledky."
							+ " \n " + "Ak nevidÌte ûiadne v˝sledky, sk˙ste napÌsaù nieËo inÈ.");
			}
		});

		window.btnMatch.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {

				int cena_od = 0;
				int cena_do = 999999999;
				
				window.group.clearSelection();
				
				responses.clear();
				urls.clear();
				
				//Autocomplete hlada v nazvoch

				// Default cena
				if (!window.priceFrom.getText().equals(""))
					cena_od = Integer.parseInt(window.priceFrom.getText());

				if (!window.priceTo.getText().equals(""))
					cena_do = Integer.parseInt(window.priceTo.getText());

				// Query
				BoolQueryBuilder query = QueryBuilders.boolQuery();
				
				//Nic nezaskrtnute - tak hlada iba v nazvoch
				if (!window.checkCategory.isSelected() && !window.checkInFactory.isSelected() 
						&& !window.checkInfo.isSelected()) {
					
					// By default hladanie v nazvoch s fuzziness miernou 1/2
					// pismena
					query.must(QueryBuilders
							.matchQuery("N·zov", window.queryField.getText())
							.minimumShouldMatch("30%")
							.fuzziness(Fuzziness.AUTO).operator(Operator.AND).slop(5));
				}
				
				else if (window.checkInTitles.isSelected() && !window.checkDocumentation.isSelected()) {
					query = searchInTitles(query, window);
				}

				// Ak maju dokumentaciu
				if (window.checkDocumentation.isSelected()) {
					query.must(QueryBuilders.matchQuery(
							"Dokument·cia k produktu", "¡no"));
				}

				// Ak sa to slovo nachadza v popise
				if (window.checkInfo.isSelected()) {
					
					query.must(QueryBuilders
							.matchQuery("Popis", window.queryField.getText())
							.fuzziness(Fuzziness.AUTO).slop(50).boost(5));
				}

				if (window.checkCategory.isSelected()) {
					query.must(QueryBuilders.matchQuery("KategÛria", window.queryField.getText())
							.fuzziness(Fuzziness.AUTO));
				}

				DefaultTableModel model = (DefaultTableModel) window.table
						.getModel();
				model.setRowCount(0);

				// Response z elasticu
					responses.add(client
							.prepareSearch()
							.setQuery(query)
							.setSize(1000)
							.setPostFilter(
									QueryBuilders.rangeQuery("Cena")
											.from(cena_od).to(cena_do)));
					responses.add(client
							.prepareSearch()
							.setQuery(query)
							.setSize(1000)
							.setPostFilter(
									QueryBuilders.rangeQuery("Cena")
											.from(cena_od).to(cena_do)));
				
				if (window.checkInFactory.isSelected()){
					query.must(QueryBuilders.matchQuery("V˝robca", window.queryField.getText())
							.fuzziness(Fuzziness.AUTO));			
				}
				
				getHits(responses.get(responses.size() - 1).execute().actionGet(), urls, model);
				
				if (window.checkInTitles.isSelected() && !window.checkCategory.isSelected()
						&& !window.checkDocumentation.isSelected() && !window.checkInFactory.isSelected()
						&& !window.checkInfo.isSelected() && cena_do == 999999999) {
					
					if (aggregation(window.queryField.getText(), "N·zov").get(0) != null)
						window.autoCompleteArea.append("\n" + 
								"--------------------------------------------------------------------------" + 
								"\n" + "Priemern· cena produktov bez filtrov: " + 
								aggregation(window.queryField.getText(), "N·zov").get(0));
					else 
						window.autoCompleteArea.append("\n" + "Priemern· cena nie je zobrazen·.");
				}
				
				else 
				{
					window.autoCompleteArea.setText("");
					window.autoCompleteArea.append("\n" + "Priemern· cena nie je zobrazen·.");
				}
				
				try {
					if (window.checkInTitles.isSelected() || (!window.checkCategory.isSelected()
							&& !window.checkDocumentation.isSelected() && !window.checkInFactory.isSelected()
							&& !window.checkInfo.isSelected())) {
						highliter.clear();
					highlight(window.queryField.getText(), "N·zov");
					} else
						highliter.clear();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				if (!highliter.isEmpty()){
					if (!highliter.get(0).startsWith("{")) {
						window.table.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
					}
				}
	        	
				resizeRows(window.table);
			}
		});
		
		//Sort podla ceny
		window.priceSortButton.addActionListener(new ActionListener() {

	        public void actionPerformed(ActionEvent e) {
	        	
	        	DefaultTableModel model = (DefaultTableModel) window.table
						.getModel();
				model.setRowCount(0);
				
				urls.clear();
				
				//Vratenie arrayListu vsetkych urliek(pre zobrazovanie v browseri)
				Iterator<String> hits = getHits(responses.get(responses.size() - 1)
						.addSort("Cena", SortOrder.ASC).execute().actionGet(), urls, model).iterator();
				
				LinkedList<String> temp = new LinkedList<String>();
				   
	        	while (hits.hasNext()) {
	        		String newHit = hits.next();
					temp.add(newHit);
				}
	        	
	        	for (String tmp: temp){
	        	urls.add(tmp);
	        	
	        	}
	        	
				resizeRows(window.table);
	        }
	    });
		
		//Sort podla poctu recenzii
		window.reviewSortButton.addActionListener(new ActionListener() {

	        public void actionPerformed(ActionEvent e) {
	        	
	        	DefaultTableModel model = (DefaultTableModel) window.table
						.getModel();
				model.setRowCount(0);
				
	        	urls.clear();
	        	
	        	Iterator<String> hits = getHits(responses.get(responses.size() - 2)
						.addSort("PoËet recenziÌ", SortOrder.DESC).execute().actionGet(), urls, model).iterator();
				LinkedList<String> temp = new LinkedList<String>();
	   
	        	while (hits.hasNext()) {
	        		String newHit = hits.next();
					temp.add(newHit);
				}
	        	
	        	for (String tmp: temp){
	        	urls.add(tmp);
	        	
	        	}

				resizeRows(window.table);
	        }
	    });

		// on shutdown
		// client.close();
	}

	// Otvor URL v browseri
	private static void open(URI uri) {
		
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
			}
		} 
		else {}
	}

	public void resizeRows(JTable table) {

		final TableColumnModel columnModel = table.getColumnModel();

		for (int column = 0; column < table.getColumnCount(); column++) {

			int width = 15;

			if (column == 1 || column == 2 || column == 4) {
				width = 15;
				columnModel.getColumn(column).setPreferredWidth(width);
				continue;
			}

			for (int row = 0; row < table.getRowCount(); row++) {

				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}

			if (width > 200)
				width = 200;

			columnModel.getColumn(column).setPreferredWidth(width);
		}

	}
	
	public LinkedList<String> getHits(SearchResponse response, LinkedList<String> urls, DefaultTableModel model) {
		
		String vyrobca;
		
		// Vsetky hity co vrati
		for (SearchHit hit : response.getHits()) {
			
			if (hit.getSource().get("V˝robca") != null) {
				vyrobca = hit.getSource().get("V˝robca").toString();
			} 
			else
				vyrobca = "Uveden˝ v n·zve";

			urls.add(hit.getSource().get("Odkaz").toString());
			
			model.addRow(new Object[] {
					hit.getSource().get("N·zov").toString(),
					hit.getSource().get("Cena").toString(),
					vyrobca,
					hit.getSource().get("Dokument·cia k produktu")
							.toString(),
					hit.getSource().get("PoËet recenziÌ").toString()

			});
			
		}
		
		return urls;
	}
	
	@SuppressWarnings("deprecation")
	
	//Hladanie v nazvoch
	public BoolQueryBuilder searchInTitles(BoolQueryBuilder query, Window window) {
		
		return query.should(QueryBuilders
				.matchQuery("N·zov", window.queryField.getText())
				.minimumShouldMatch("30%")
				.fuzziness(Fuzziness.AUTO).operator(Operator.AND).slop(5));
	}
	
	private static List<String> autocomplete(String query, String field) {
		List<String> results = new ArrayList<String>();
		
		try {
			String url = "http://localhost:9200/produkty/produkt/_search";
			URL obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			
			String jsonData = "{\r\n" + 
					"  \"suggest\": {\r\n" + 
					"    \"autocomplete\" : {\r\n" + 
					"      \"text\" : \""+query+"\",\r\n" + 
					"      \"term\" : {\r\n" + 
					"        \"field\" : \""+field+"\"\r\n" +
					"		}\r\n" +
					"      }\r\n" + 
					"  }\r\n" + 
					"}";
			System.out.println(jsonData);
			
			// Posli request
			connection.setDoOutput(true);
			DataOutputStream dataOutput = new DataOutputStream(connection.getOutputStream());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dataOutput, "UTF-8"));
			writer.write(jsonData);
			writer.flush();
			writer.close();

			int responseCode = connection.getResponseCode();
			
			if (responseCode != 200)
				return results;

			// Precitaj response
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			in.close();
			
			
			JSONObject jsonResponse = new JSONObject(response.toString());
			System.out.println(response);
			JSONArray suggestor = jsonResponse.getJSONObject("suggest").getJSONArray("autocomplete");

			if(suggestor.length() > 0) {
				ArrayList<JSONArray> options = new ArrayList<JSONArray>();

				for (int i = 0; i < suggestor.length(); i++)
					options.add(((JSONObject) suggestor.get(i)).getJSONArray("options"));
				
				for (int i = 0; i < options.size(); i++) {
					
					for (int j = 0; j < options.get(i).length(); j++) {
						String autoCompleteText = options.get(i).getJSONObject(j).getString("text");
						results.add(autoCompleteText);
					}
							
					if (i != options.size()-1)
						results.add("---------------------Ôalöie slovo myslÌte: -------------------------------------------------------------");
				}
			}
			
		} catch (Exception e) {
			
		}
		
		return results;
	}
	
	public static List<String> highlight(String query, String field) throws JSONException {
		List<String> results = new ArrayList<String>();
		int index = 0;
		
		try {
			String url = "http://localhost:9200/produkty/produkt/_search";
			URL obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", USER_AGENT);

			String jsonData = "{\r\n" + 
					"  \"query\": {\r\n" + 
					"    \"match\" : {" + "\""+field+"\": {\r\n" +
					"		\"query\" : \""+query+"\",\r\n" +
					"		\"fuzziness\": \"AUTO\"," +
					"		\"operator\": \"and\"\r\n" +
					" 			}\r\n" +
					"		  }\r\n" +		
					"		},\r\n" +	
					"   \"highlight\" : {\r\n" + 
					"      \"fields\" : {\r" + 
					"        \"N·zov\" : {}\r\n" + 
					"    }\r\n" + 
					"  }\r\n" + 
					"}";
			
			// Send post request
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
			writer.write(jsonData);
			writer.flush();
			writer.close();

			int responseCode = connection.getResponseCode();
			
			if (responseCode != 200)
				return results;

			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			in.close();
			
			// Json parse
			JSONObject jsonResponse = new JSONObject(response.toString());
			System.out.println(jsonResponse);
			highlightResponse = jsonResponse.toString();
			Pattern pattern = Pattern.compile("(?<=\\>).*?(?=\\<)");
			Matcher matcher = pattern.matcher(highlightResponse);
			highliter.clear();
			index = 0;
			
			StringTokenizer st = new StringTokenizer(query);
			
			//Pocet slov v query
			numberOfWords = st.countTokens();
			
			while (matcher.find())
			{	
				index++;
				if (index % 2 == 1){
					highliter.add(matcher.group());
				}
			}
			
		} catch (Exception e) {}
		
		return results;
	}
	
	private static List<String> aggregation (String query, String field) {
		List<String> results = new ArrayList<String>();
		
		try {
			String url = "http://localhost:9200/produkty/produkt/_search";
			URL obj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

			//add reuqest header
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			
			String jsonData = "{\r\n" + 
					"  \"query\": {\r\n" + 
					"    \"match\" : {" + "\""+field+"\": {\r\n" +
					"		\"query\" : \""+query+"\",\r\n" +
					"		\"fuzziness\": \"AUTO\"," +
					"		\"operator\": \"and\"\r\n" +
					" 			}\r\n" +
					"		  }\r\n" +		
					"		},\r\n" +
					"  \"aggs\": {\r\n" + 
					"    \"avg_grade\" : {\r\n" + 
					"		\"avg\" : {\r\n" + 
					"      \"field\" : \"Cena\"\r\n" +
					"  		}\r\n" + 
					"  	  }\r\n" +
					"  }\r\n" +
					"}";

			connection.setDoOutput(true);
			DataOutputStream dataOutput = new DataOutputStream(connection.getOutputStream());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dataOutput, "UTF-8"));
			writer.write(jsonData);
			writer.flush();
			writer.close();

			int responseCode = connection.getResponseCode();
			
			if (responseCode != 200)
				return results;

			// Precitaj response
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			in.close();
				
			JSONObject jsonResponse = new JSONObject(response.toString());
			
			String averagePrice = jsonResponse.getJSONObject("aggregations")
					.getJSONObject("avg_grade").getString("value");
			results.add(averagePrice);
			
			System.out.println(averagePrice);
			
		} catch (Exception e) {
			
		}
		
		return results;
	}

}
