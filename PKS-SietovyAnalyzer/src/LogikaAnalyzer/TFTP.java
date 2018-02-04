package LogikaAnalyzer;

import java.io.IOException;
import java.util.ArrayList;

public class TFTP {
	
	public static int pocetTFTP;
	public static ArrayList<Integer> CisloTFTPRamec;
	public static ArrayList<String> TFTPRamce;
	public static ArrayList<String> PomTFTPRamce;
	public static ArrayList<Integer> PomCisloTFTPRamec;
	
public void ZaradTFTP(){
		
		pocetTFTP++;
		if (pocetTFTP == 1){ 
		CisloTFTPRamec = new ArrayList<Integer>();
		TFTPRamce = new ArrayList<String>();
		PomTFTPRamce = new ArrayList<String>();
		PomCisloTFTPRamec = new ArrayList<Integer>();
		}
		CisloTFTPRamec.add(Analyzer.ramec); // tu sa nachadza cislo ramca na 0te pozicii je cislo 1!!!
	}

public void TFTPVypis() throws IOException{
	
	String Port="";
	//System.out.println(Analyzer.MyPort);
	AnalyzerVypis analyza = new AnalyzerVypis();
	for (int i = 0 ; i < TFTPRamce.size() ; i++){
		
	if ((Integer.parseInt(TFTPRamce.get(i).substring(68, 72),16)) == (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){
		Port = (TFTPRamce.get(i).substring(72, 76)); // tu mam ten zvlastny port
		PomTFTPRamce.add(TFTPRamce.get(i));
		PomCisloTFTPRamec.add(CisloTFTPRamec.get(i));
		
	}
	else if ((Integer.parseInt(TFTPRamce.get(i).substring(72, 76),16)) == (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort))))
			{
			Port = (TFTPRamce.get(i).substring(68, 72));
			PomTFTPRamce.add(TFTPRamce.get(i));
			PomCisloTFTPRamec.add(CisloTFTPRamec.get(i));
			//analyza.KonkretnyVypis(CisloTFTPRamec.get(i));
			}
	else if ((Port.equals(TFTPRamce.get(i).substring(72, 76))) || (Port.equals(TFTPRamce.get(i).substring(68, 72))) ){
		PomTFTPRamce.add(TFTPRamce.get(i));
		PomCisloTFTPRamec.add(CisloTFTPRamec.get(i));
		
	}
		
		
		
		}
	for (int x = 0 ; x < PomTFTPRamce.size(); x++){
		
		analyza.KonkretnyVypis(PomCisloTFTPRamec.get(x));
		
	}
	
	}

}
