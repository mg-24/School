import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;

import com.typesafe.config.ConfigException.Null;

public class Crawler {

	HashMap<String, Integer> urls = new HashMap<String, Integer>();
	LinkedList<String> urlsInProccess = new LinkedList();
	Connection connection = null;
	Document htmlDocument;
	Document sb;
	Elements hrefsOnPage;
	JSONObject obj = new JSONObject();
	String path = "C:/Users/Matúš/workspace/VIZadanie/";

	@SuppressWarnings("unused")
	public void crawl(String browser, String url) throws JSONException,
			InterruptedException, IOException {
		
		/*int numberOfFiles = 28329;
		int index = 0;
		
		while (index <= numberOfFiles) {
			String json = new String(Files.readAllBytes(Paths.get(path + index)), StandardCharsets.UTF_8);
			Pattern pattern = Pattern.compile("(https://www.hej.sk.+?\")");
			Matcher matcher = pattern.matcher(json);
			if (matcher.find())
			{
			    System.out.println(matcher.group(1).replace("\"", ""));
			    urls.put(matcher.group(1).replace("\"", ""), 1);
			}
			index++;
		}
		*/
		
		threadCrawl(browser, url);

	}
	
	public void threadCrawl (String browser, String url) throws UnsupportedEncodingException, FileNotFoundException, JSONException {
		Integer iterator = 0;
		
		urlsInProccess.add(url);
		
		while (!urlsInProccess.isEmpty()) {
			
			url = urlsInProccess.pop();
			
			if (url.matches("http.*www.hej.sk\\/.*") && (!url.matches(".*doc.*")) && (!url.matches(".*/kosik.*"))) {
				
				url = canonicUrl(url);
				
				if (!urls.containsKey(url)) {

					//System.out.println(url);
					
					connection = Jsoup.connect(url).userAgent(browser);

					try {
						htmlDocument = connection.header("Accept-Encoding",
								"gzip, deflate").get();
					} catch (IOException e) {
						e.printStackTrace();
					}

					hrefsOnPage = htmlDocument.select("a[href]");

					// Som na detaile produktu
					if ((htmlDocument.select(".product-detail")).hasText()) {
						System.out.println("Som na detail page");
						System.out.println(url);
						
						Writer out = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream(iterator.toString()), "UTF-8"));
						iterator++;
						
						Elements breadcrumb = null;
						Elements heading = null;
						Elements price = null;
						Elements description = null;
						Elements reviews = null;
						Elements productFactory = null;
						Elements descType = null;
						Elements desc = null;
						int review = 0;
						
						//JSONS
						JSONObject object = new JSONObject(); 
						ArrayList<String> categories = new ArrayList<String>();
						ArrayList<String> infoText = new ArrayList<String>();
						ArrayList<String> infoType = new ArrayList<String>();
						ArrayList<String> infoMain = new ArrayList<String>();
						ArrayList<String> infoOther = new ArrayList<String>();
						JSONObject mainInfo = new JSONObject();
						//Kategoria DONE
						try {
							breadcrumb = htmlDocument.select(".breadcrumb li");
						} catch(Null e){
							e.printStackTrace();
						}
						
						try {
						//Nadpis DONE
						heading = htmlDocument.select(".product-summary .page-title");
						} catch(Null e) {
							e.printStackTrace();
						}
						
						//Cena DONE
						try {
							price = htmlDocument.select("#real_price");
						} catch(Null e) {
							e.printStackTrace();
						}
						
						//Popis vyrobku DONE
						try {
							description = htmlDocument.select("#productTabContent .wsw");
						} catch(Null e) {
							e.printStackTrace();
						}
						
						if (!(description.select("h3").text().equals("")))
							description.select("h3").remove();
						if (!(description.select("#dokumentace_list").text().equals("")))
							description.select("#dokumentace_list").remove();
						
						//Budem iba zistovat ci produkt ma recenziu alebo nie a podla toho napr. vyhladavat
						try {
							reviews = htmlDocument.select("#detail_tab_recenze");
						} catch (Null e) {
							e.printStackTrace();
						}
						
						//Cislo kolko je recenzii
						if (!(reviews.text().equals("")))
							review = Integer.parseInt(reviews.text().split("\\(")[1].split("\\)")[0].split(" ")[1]);
						
						//Aky je vyrobca produktu
						try {
							productFactory = htmlDocument.select(".product-summary-catalog-item span");
						} catch (Null e) {
							e.printStackTrace();
						}
						
						//Typ co to je napr: Zaruka, Typ procesora
						try {
							descType = htmlDocument.select(".tabullar_description .first");
						} catch (Null e){
							e.printStackTrace();
						}
						
						//Text v tabulke pri popisoch
						try {
							desc = htmlDocument.select(".tabullar_description td").not(".first").not(".headline").not("[colspan]");
						} catch (Null e){
							e.printStackTrace();
						}
						
						if (!(heading.text().equals("")))
							object.put("Názov", heading.text());
						
						if (!(price.text().equals("")))
							object.put("Cena", Integer.parseInt(price.text().split("\\.")[0].replace(" ","")));
						
						if (!(productFactory.text().equals("")))
							object.put("Výrobca", checkIfNull(productFactory));					
						
						if (!(breadcrumb.text().equals(""))) {
							
							for (Element el : breadcrumb) {
								categories.add(el.text());
							}
						
							categories.remove(0);
							categories.remove(categories.size() - 1);
						
							object.put("Kategória", categories);
						}
						
						//Popisy v tabulkach
						if (!(desc.text().equals(""))) {
							
							for (Element el: desc){
									infoText.add(el.text());
							}
						}
						
						if (!(descType.text().equals(""))) {
						//Aby tam nedavalo dvojito slova, kedze oni davaju do tvoch "a" tagov za sebou to iste
							for (Element el : descType) {
								if (!el.select("a").text().equals("")){
									infoType.add(el.select("a").not("[data-container=body]").text());
								}
								else
									infoType.add(el.text());
							}
						}
						
						for (int i = 0; i < (infoType.size() / 2); i++){
							infoMain.add(infoType.get(i) + ": " + infoText.get(i));
							
						}
						
						for (int i = infoType.size() / 2; i < infoType.size() - 1; i++){
							infoOther.add(infoType.get(i) + ": " + infoText.get(i));
							
						}
						
						mainInfo.put("Hlavné informácie", infoMain);
						mainInfo.put("Ïalšie informácie", infoOther);
	
						if (!(breadcrumb.text().equals(""))) {
							JSONObject infoProd = new JSONObject(object.put("Informácie o produkte", mainInfo));
						}
						
						object.put("Poèet recenzií", review);
						
						if (!(description.text().equals(""))) {
							
							if (!(description.select(".section-title").text().equals(""))) {
								object.put("Dokumentácia k produktu", "Áno");
								description.select(".section-title").remove();
							}
							else
								object.put("Dokumentácia k produktu", "Nie");
							
							object.put("Popis", description.text());
						}
						
						object.put("Odkaz", url.toString());
						
						try {
							out.append(object.toString());
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					for (int i = 0; i < hrefsOnPage.size(); i++) {
						if (!url.matches(canonicUrl(hrefsOnPage.get(i).absUrl("href"))))
							urlsInProccess.add(hrefsOnPage.get(i).absUrl("href"));
					}

					hrefsOnPage.clear();

					urls.put(url, 1);
					
				}
			}
		}
		
	}

	public String canonicUrl(String url) {

		String result;
		if ((url.contains("?") && !url.contains("stranka=") && !url.contains("limit="))
				|| url.contains("#")) {
			result = url.replaceAll("[\\?\\#].*", "");
			return result;
		} else
			return url;
	}
	
	public String checkIfNull(Elements html){
		
		if (html != null)
			return html.text();
		else
		return "";
		
	}

}
