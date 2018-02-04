import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.json.JSONException;


public class Connect {

		static String browser = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
		static String url = "https://www.hej.sk/";
		
		public static void main(String[] args) throws IOException, JSONException, InterruptedException {
			
			
			final MainWindow mainWindow = new MainWindow();
			
			mainWindow.applicationButton.addActionListener( new ActionListener()
			{
			    public void actionPerformed(ActionEvent e)
			    {
			    	Elastic elasticConnect = new Elastic();
			    	mainWindow.dispose();
			    	try {
						try {
							elasticConnect.connect();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
			    }
			
				
			});
			
			mainWindow.crawlingButton.addActionListener( new ActionListener()
			{
			    public void actionPerformed(ActionEvent e)
			    {
			    	Crawler crawler = new Crawler();
					mainWindow.dispose();
					try {
						crawler.crawl(browser, url);
					} catch (JSONException e1) {
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			    }
			
				
			});
			
			mainWindow.pushElasticButton.addActionListener( new ActionListener()
			{
			    public void actionPerformed(ActionEvent e)
			    {
			    	Elastic elasticConnect = new Elastic();
			    	try {
						elasticConnect.elasticDataPush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			    	mainWindow.dispose();
			    }
			
				
			});
		}

	}
