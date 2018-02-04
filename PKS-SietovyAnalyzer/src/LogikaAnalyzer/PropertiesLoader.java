package LogikaAnalyzer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesLoader {
	public String getPropValue(String property) throws IOException{
		String result = "";
		Properties prop = new Properties();
		String propFileName = "config.properties";
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property subor '" + propFileName + "' nenajdena na ceste");
		}
		
		result = prop.getProperty(property);

		return result;
		}
}

