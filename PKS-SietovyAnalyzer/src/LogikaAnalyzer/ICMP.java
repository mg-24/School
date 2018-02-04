package LogikaAnalyzer;

import java.util.ArrayList;

public class ICMP {

	public static ArrayList<String> IpICMP; //vsetky ARPcky v liste
	public static ArrayList<Integer> CisloICMPRamec;//cislo ramca z analyzy ramcov
	public static ArrayList<String> PomIpICMP;
	public static ArrayList<String> Pom2IpICMP;
	public static ArrayList<String> PomTypeICMP;
	public static ArrayList<Integer> PomIpCisloICMPRamec;
	public static int pocetICMP;
	
public void ZaradICMP(){
		
		pocetICMP++;
		if (pocetICMP == 1){ 
			IpICMP = new ArrayList<String>();
		CisloICMPRamec = new ArrayList<Integer>();
		PomIpCisloICMPRamec= new ArrayList<Integer>();
		PomIpICMP = new ArrayList<String>();
		Pom2IpICMP = new ArrayList<String>();
		PomTypeICMP = new ArrayList<String>();
		}
		CisloICMPRamec.add(Analyzer.ramec); // tu sa nachadza cislo ramca na 0te pozicii je cislo 1!!!
	}

public void ICMPVypis(){
	
		AnalyzerVypis analyza = new AnalyzerVypis();

		for (int x = 0; x < IpICMP.size(); x++) {
			analyza.KonkretnyVypis(CisloICMPRamec.get(x));
			AnalyzerVypis.AnalyzaRamca.append("TYPE : " + PomTypeICMP.get(x));
			AnalyzerVypis.AnalyzaRamca.append("\n");
		}

	}
}
