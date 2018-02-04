import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.json.simple.JSONObject;


public class Crawl implements Runnable {

	HashMap<String, Integer> urls = new HashMap<String, Integer>();
	LinkedList<String> urlsInProccess = new LinkedList();
	Connection connection = null;
	Document htmlDocument;
	Elements hrefsOnPage;
	JSONObject obj = new JSONObject();

	@SuppressWarnings("unchecked")
	public void crawl(String browser, String url) {
		
		System.out.println("This is currently running on the main thread, " +  
	            "the id is: " + Thread.currentThread().getId());  
	    Crawl worker = new Crawl();   
	    Thread thread = new Thread(worker);  
	    thread.start();  

		urlsInProccess.add(url);

		while (!urlsInProccess.isEmpty()) {
			
			url = urlsInProccess.pop();
			obj.put("Nazov", "foo");
			System.out.println(obj);
			
			if (url.matches(".*alza.sk\\/.*")) {
				
				url = canonicUrl(url);
				
				if (!urls.containsKey(url)) {
					
					System.out.println(url);
					connection = Jsoup.connect(url).userAgent(browser);

					try {
						htmlDocument = connection.get();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					hrefsOnPage = htmlDocument.select("#left #tpf #litp18852653 a[href]"); //iba notebooky skuska
					
					if (hrefsOnPage.isEmpty()){
						hrefsOnPage = htmlDocument.select("#boxes a[href]");
						if (hrefsOnPage.isEmpty()){
							hrefsOnPage = htmlDocument.select("a [href]");
						}
					}
					
					//Som na detaile produktu
					if ((htmlDocument.select("#detailItem")).hasText()){
						System.out.println("Som na detail page");
						System.out.println("Nazov: " + htmlDocument.select("h1").text());
						System.out.println("Popis" + htmlDocument.select("#description_pp"));
						System.out.println(htmlDocument.select(".params .name a").text() + ":" + htmlDocument.select("span.value").text());
						
					}
					// #detailItem - vtedy som na produkte presne
					for (int i = 0; i < hrefsOnPage.size(); i++) {
						urlsInProccess.add(hrefsOnPage.get(i).absUrl("href"));
					}
					
					hrefsOnPage.clear();

					urls.put(url, 1);
					/*
					 * System.out.println(urlsInProccess.get(0)); for
					 * (HashMap.Entry m : urls.entrySet()) {
					 * System.out.println(m.getKey() + " " + m.getValue()); }
					 */
				}
			}
		}
		for (HashMap.Entry m : urls.entrySet()) {
			System.out.println(m.getKey() + " " + m.getValue());
		}
	}
	
	public String canonicUrl(String url){
		
		String result;
		if (url.contains("?")){
			result = url.replaceAll("\\?.*","");
			return result;
		}
		else return url;
	}

	@Override
	public void run() {
		System.out.println("This is currently running on a separate thread, " +  
	            "the id is: " + Thread.currentThread().getId()); 
		
	}

}
