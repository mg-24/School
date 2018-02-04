package LogikaAnalyzer;

import java.util.ArrayList;

public class ftpdata {
	
public static ArrayList<String> ftpRamce;	
public static ArrayList<Integer> CisloftpRamec;
public static int pocetftp;

public void Zaradftp(){
		
		
		pocetftp++;
		if (pocetftp== 1){ 
			ftpRamce = new ArrayList<String>();
			CisloftpRamec = new ArrayList<Integer>();
		}
		CisloftpRamec.add(Analyzer.ramec); // tu sa nachadza cislo ramca na 0te pozicii je cislo prveho ramca!!!
		
	}

public void Vypisftp(){
	
	AnalyzerVypis.AnalyzaRamca.append("pocet je: "+ftpRamce.size());
	
	AnalyzerVypis analyza = new AnalyzerVypis ();
	for (int x = 0; x < ftpRamce.size(); x++){
		
		analyza.KonkretnyVypis(CisloftpRamec.get(x));
		
		
		
	}
	
	
	
}

}
