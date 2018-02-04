package LogikaAnalyzer;
import java.util.ArrayList;

public class ARP {
	
	public static ArrayList<String> IpARP; //vsetky ARPcky v liste
	public static ArrayList<Integer> CisloArpRamec;//cislo ramca z analyzy ramcov
	public static ArrayList<String> PomIpARP;
	public static ArrayList<String> Pom2IpARP;
	public static ArrayList<Integer> PomIpCisloArpRamec;

	static int komunikacia;
	public static int pocetARP;
	
	public void ZaradARP(){
		
		pocetARP++;
		if (pocetARP == 1){ 
			IpARP = new ArrayList<String>();
		CisloArpRamec = new ArrayList<Integer>();
		PomIpCisloArpRamec = new ArrayList<Integer>();
		PomIpARP = new ArrayList<String>();
		Pom2IpARP = new ArrayList<String>();
		}
		CisloArpRamec.add(Analyzer.ramec); // tu sa nachadza cislo ramca na 0te pozicii je cislo 1!!!
	}
	
	
	
	public void ARPVypis() throws Exception{
		
		int i = 0;
		int pozicia = 0;
		int located = 0;
		int done = 0;
		int pridanie = 0;
		
		while (i < IpARP.size() - 1){
			
		
		AnalyzerVypis analyza = new AnalyzerVypis();
		
		if ((Integer.parseInt(IpARP.get(i).substring(40,44))) == Integer.parseInt(Analyzer.load.getPropValue("Reply")))
			i++;
		
		if ((Integer.parseInt(IpARP.get(i).substring(40,44))) == Integer.parseInt(Analyzer.load.getPropValue("Request"))){ //ak je to 1 tak je to request
			
			Pom2IpARP.add(IpARP.get(i)); // sem si dam ten ramec hned ktory prisiel aby som pri dalsom vyhladani uz tento nebral do uvahy
			for (int j = i+1; j < IpARP.size(); j++){//kontrola cez vsetky ARPcky
					
				if (PomIpARP.isEmpty()){
					PomIpARP.add(IpARP.get(i));
					if (i == 0) PomIpCisloArpRamec.add(CisloArpRamec.get(i));
					//vlozim cislo a ramec ARP ak je prazny list toho prveho
					if (i > 0) PomIpCisloArpRamec.add(CisloArpRamec.get(i));
				}
				
				if (((Integer.parseInt(IpARP.get(j).substring(40,44))) == Integer.parseInt(Analyzer.load.getPropValue("Request"))) && ((IpARP.get(j).substring(56,64).equals(IpARP.get(i).substring(56,64)))) && (IpARP.get(j).substring(76,84).equals(IpARP.get(i).substring(76,84))))
				{
					
					PomIpARP.add(IpARP.get(j)); // ulozim rovnake ARP
					if (i == 0)PomIpCisloArpRamec.add(CisloArpRamec.get(j));
					if (i > 0) PomIpCisloArpRamec.add(CisloArpRamec.get(j));
					
				} if (((Integer.parseInt(IpARP.get(j).substring(40,44))) == Integer.parseInt(Analyzer.load.getPropValue("Request"))) && (!(IpARP.get(j).substring(56,64).equals(IpARP.get(i).substring(56,64))) || !(IpARP.get(j).substring(76,84).equals(IpARP.get(i).substring(76,84)))))
				{ //ak sa nerovnaju IP tych ramcov
					
					if (pridanie == 0){
					for (int x = 0; x < Pom2IpARP.size();x++){
						if (((Pom2IpARP.get(x).substring(56,64).equals(IpARP.get(j).substring(56,64)))) && (Pom2IpARP.get(x).substring(76,84).equals(IpARP.get(j).substring(76,84)))){
							located = 1; //najdena zhoda ARP ramca z predchadzajucim
							
						}else if ((!(Pom2IpARP.get(x).substring(56,64).equals(IpARP.get(j).substring(56,64))) || !(Pom2IpARP.get(x).substring(76,84).equals(IpARP.get(j).substring(76,84)))))
							located = 0;
						
					}
				}
					if (located == 0){
					pozicia = j; // sem si ulozim index od ktoreho potom budem hladat nasledne
					pridanie ++;
					located = 1;
					}
				}
				//if (Array[0] == 0)
				/*if (((Integer.parseInt(IpARP.get(j).substring(40,44))) == 2 ) && (!(IpARP.get(i).substring(56,64).equals(IpARP.get(j).substring(76,84))) && !(IpARP.get(i).substring(76,84).equals(IpARP.get(j).substring(56,64)))))*/
					
					if(((Integer.parseInt(IpARP.get(j).substring(40,44))) == (Integer.parseInt(Analyzer.load.getPropValue("Reply"))) ) && (IpARP.get(i).substring(56,64).equals(IpARP.get(j).substring(76,84))) && (IpARP.get(i).substring(76,84).equals(IpARP.get(j).substring(56,64))))
					{
						done++;
						komunikacia ++; //cislo komunikacie
						//Pom2IpARP.remove(i);
						AnalyzerVypis.AnalyzaRamca.append("\n");
						AnalyzerVypis.AnalyzaRamca.append("Komunikácia è."+ komunikacia);
						AnalyzerVypis.AnalyzaRamca.append("\n");
						Pom2IpARP.remove(IpARP.get(i));
						
						if (PomIpARP.size() > 20){
							
							for (int y = 0 ; y < 10; y++){
								AnalyzerVypis.AnalyzaRamca.append("ARP-Request, "+"IP Adresa: "+ Integer.parseInt(PomIpARP.get(y).substring(76, 78), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(78, 80), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(80, 82), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(82, 84), 16)
										+ " ,MAC adresa: ???");
								AnalyzerVypis.AnalyzaRamca.append("\n");
								
								AnalyzerVypis.AnalyzaRamca.append("Zdrojová IP: "+ Integer.parseInt(PomIpARP.get(y).substring(56, 58), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(58, 60), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(60, 62), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(62,64), 16)
										+" ,Cie¾ová IP: " + Integer.parseInt(PomIpARP.get(y).substring(76, 78), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(78, 80), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(80, 82), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(82, 84), 16));
								AnalyzerVypis.AnalyzaRamca.append("\n");
								
								/*System.out.println("ramec"+CisloArpRamec.get(i));*/ //cislo ARPrequestu
								analyza.KonkretnyVypis(PomIpCisloArpRamec.get(y));
								AnalyzerVypis.AnalyzaRamca.append("\n");
								
								}
							for (int y = PomIpARP.size() - 10 ; y < PomIpARP.size(); y++){
								AnalyzerVypis.AnalyzaRamca.append("ARP-Request, "+"IP Adresa: "+ Integer.parseInt(PomIpARP.get(y).substring(76, 78), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(78, 80), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(80, 82), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(82, 84), 16)
										+ " ,MAC adresa: ???");
								AnalyzerVypis.AnalyzaRamca.append("\n");
								
								AnalyzerVypis.AnalyzaRamca.append("Zdrojová IP: "+ Integer.parseInt(PomIpARP.get(y).substring(56, 58), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(58, 60), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(60, 62), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(62,64), 16)
										+" ,Cie¾ová IP: " + Integer.parseInt(PomIpARP.get(y).substring(76, 78), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(78, 80), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(80, 82), 16)
										+ "." + Integer.parseInt(PomIpARP.get(y).substring(82, 84), 16));
								AnalyzerVypis.AnalyzaRamca.append("\n");
								
								/*System.out.println("ramec"+CisloArpRamec.get(i));*/ //cislo ARPrequestu
								analyza.KonkretnyVypis(PomIpCisloArpRamec.get(y));
								AnalyzerVypis.AnalyzaRamca.append("\n");
								
								}
							
							
							
						}
						else {
						for (int y = 0 ; y < PomIpARP.size(); y++){
						AnalyzerVypis.AnalyzaRamca.append("ARP-Request, "+"IP Adresa: "+ Integer.parseInt(PomIpARP.get(y).substring(76, 78), 16)
								+ "." + Integer.parseInt(PomIpARP.get(y).substring(78, 80), 16)
								+ "." + Integer.parseInt(PomIpARP.get(y).substring(80, 82), 16)
								+ "." + Integer.parseInt(PomIpARP.get(y).substring(82, 84), 16)
								+ " ,MAC adresa: ???");
						AnalyzerVypis.AnalyzaRamca.append("\n");
						
						AnalyzerVypis.AnalyzaRamca.append("Zdrojová IP: "+ Integer.parseInt(PomIpARP.get(y).substring(56, 58), 16)
								+ "." + Integer.parseInt(PomIpARP.get(y).substring(58, 60), 16)
								+ "." + Integer.parseInt(PomIpARP.get(y).substring(60, 62), 16)
								+ "." + Integer.parseInt(PomIpARP.get(y).substring(62,64), 16)
								+" ,Cie¾ová IP: " + Integer.parseInt(PomIpARP.get(y).substring(76, 78), 16)
								+ "." + Integer.parseInt(PomIpARP.get(y).substring(78, 80), 16)
								+ "." + Integer.parseInt(PomIpARP.get(y).substring(80, 82), 16)
								+ "." + Integer.parseInt(PomIpARP.get(y).substring(82, 84), 16));
						AnalyzerVypis.AnalyzaRamca.append("\n");
						
						/*System.out.println("ramec"+CisloArpRamec.get(i));*/ //cislo ARPrequestu
						analyza.KonkretnyVypis(PomIpCisloArpRamec.get(y));
						AnalyzerVypis.AnalyzaRamca.append("\n");
						
							}
						}
						
						AnalyzerVypis.AnalyzaRamca.append("Komunikácia è."+ komunikacia);
						AnalyzerVypis.AnalyzaRamca.append("\n");
						
						AnalyzerVypis.AnalyzaRamca.append("ARP-Reply, "+"IP Adresa: "+ Integer.parseInt(IpARP.get(j).substring(56, 58), 16)
								+ "." + Integer.parseInt(IpARP.get(j).substring(58, 60), 16)
								+ "." + Integer.parseInt(IpARP.get(j).substring(60, 62), 16)
								+ "." + Integer.parseInt(IpARP.get(j).substring(62,64), 16)
								+" ,MAC adresa: "+ IpARP.get(j).substring(12,14).toUpperCase()
								+ " " + IpARP.get(j).substring(14,16).toUpperCase()
								+ " " + IpARP.get(j).substring(16,18).toUpperCase()
								+ " " + IpARP.get(j).substring(18,20).toUpperCase()
								+ " " + IpARP.get(j).substring(20,22).toUpperCase()
								+ " " + IpARP.get(j).substring(22,24).toUpperCase());
						AnalyzerVypis.AnalyzaRamca.append("\n");
						
						AnalyzerVypis.AnalyzaRamca.append("Zdrojová IP: "+ Integer.parseInt(IpARP.get(j).substring(56, 58), 16)
								+ "." + Integer.parseInt(IpARP.get(j).substring(58, 60), 16)
								+ "." + Integer.parseInt(IpARP.get(j).substring(60, 62), 16)
								+ "." + Integer.parseInt(IpARP.get(j).substring(62,64), 16)
								+" ,Cie¾ová IP: " + Integer.parseInt(IpARP.get(j).substring(76, 78), 16)
								+ "." + Integer.parseInt(IpARP.get(j).substring(78, 80), 16)
								+ "." + Integer.parseInt(IpARP.get(j).substring(80, 82), 16)
								+ "." + Integer.parseInt(IpARP.get(j).substring(82, 84), 16));
						AnalyzerVypis.AnalyzaRamca.append("\n");
						
						analyza.KonkretnyVypis(CisloArpRamec.get(j));
					}
					if (done == 1){ // ak bol vypis spolu s reply
						done = 0;
						PomIpARP.clear();
						PomIpCisloArpRamec.clear();
						pridanie = 0;
						if (pozicia == i){
							i = j+1;
							pozicia = j+1;
						}
						else i = pozicia;
						
						break;
					}
					if (j == IpARP.size() - 1) { // ak je na konci a nenasiel
													// reply
						if (pozicia == i) {
							i = pozicia + 1;
						} else {
							i = pozicia;
						}
						located = 0;
						pridanie = 0;
						PomIpARP.clear();
						PomIpCisloArpRamec.clear();
					}

				}

			}

		}

	}

}
