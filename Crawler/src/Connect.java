import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Connect {
	
	static String browser = "Chrome/61.0.3163.10";
	static String url = "https://www.alza.sk/dell-inspiron-15-5000-cierny-d4738198.htm";
	
	public static void main(String[] args) throws IOException {
		
		Crawl crawl = new Crawl();
		crawl.crawl(browser, url);
		//(linksOnPage.get(0).absUrl("href"));
		
		//System.out.println(linksOnPage);
	}

}
