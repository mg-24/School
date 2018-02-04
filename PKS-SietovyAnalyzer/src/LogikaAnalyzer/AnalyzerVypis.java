package LogikaAnalyzer;  
   
import java.util.*;
  
public class AnalyzerVypis {  

	public static StringBuilder AnalyzaRamca = new StringBuilder();
	
	public void KonkretnyVypis (int i) {
		
		int poc = 1;
        int pocet = 0;
        int x = 12;

        if (i > 1)
			AnalyzaRamca.append("\n");
        
        AnalyzaRamca.append("rámec " +(i));
		AnalyzaRamca.append("\n");
		

		AnalyzaRamca.append("dåžka rámca poskytnutá paketovým drajverom - "
				+ Analyzer.DlzkaRamca.get((i-1) + (i-1)) + "B");
		AnalyzaRamca.append("\n");

			AnalyzaRamca.append("dåžka rámca prenášaného po médiu - "
					+ Analyzer.DlzkaRamca.get((i-1) + (i-1) + 1) + "B");
			AnalyzaRamca.append("\n");
        
		//Ak naslo Ipcku , teda tu ktoru chceme (ETHERNET II)
		
		//podmienky podla toho aky je to typ/dlzka ramca
		if (Analyzer.Decimal.get(i-1) >= 1536) {//ARP.CisloArpRamec.get(i)-1 preto -1 lebo chcem poziciu v liste a to je od 0
			AnalyzaRamca.append("Ethernet II");
			AnalyzaRamca.append("\n");
		}

		if (Analyzer.Decimal.get(i-1) <= 1500) {
			
			if (Analyzer.Ramce.get(i-1).substring(28, 32).equals("ffff")){
				AnalyzaRamca.append("IEEE 802.3 – Raw");
				AnalyzaRamca.append("\n");
			}

			else if (Analyzer.Ramce.get(i-1).substring(28, 32).equals("aaaa")){
				AnalyzaRamca.append("IEEE 802.3- LLC - SNAP");
				AnalyzaRamca.append("\n");
			}

			else{
				AnalyzaRamca.append("IEEE 802.3 - LLC");
				AnalyzaRamca.append("\n");
			}
		}

		AnalyzaRamca.append("Zdrojová MAC adresa: ");
        		
		while (x != 24){
			AnalyzaRamca.append(Analyzer.Ramce.get(i-1).substring(x,(x+2)).toUpperCase()+" ");
			x = x + 2;
			}
		x = 0;
		
		AnalyzaRamca.append("\n");

		AnalyzaRamca.append("Cie¾ová MAC adresa: ");

		while (x != 12){
		AnalyzaRamca.append(Analyzer.Ramce.get(i-1).substring(x,(x+2)).toUpperCase()+" ");
		x = x + 2;
		}
		x = 12;
		AnalyzaRamca.append("\n");

		//Formatovany vypis podla zadania
		while (pocet != Analyzer.DlzkaRamca.get((i-1) + (i-1))*2) {

			AnalyzaRamca.append(Analyzer.Ramce.get(i-1).substring(pocet,(pocet+2)).toUpperCase()+" ");

			if (poc % 8 == 0)
				AnalyzaRamca.append("  ");
			
			if (poc % 16 == 0)
				AnalyzaRamca.append("\n");
			
			pocet = pocet+2;
			poc++;
		}
		
		AnalyzerVypis.AnalyzaRamca.append("\n");
		pocet = 0;
		poc = 1;
    }
    
	
	public void Vypis(int i) throws Exception {  
    	
		int j = 0,poc = 1;
    	int where = 0;
        int pocet = 0;
        int x = 12;
        
        for (i = 0; i < Analyzer.Ramce.size(); i++){
			if (i > 0){
				
			AnalyzaRamca.append("\n");
			AnalyzaRamca.append("\n");
			}
			AnalyzaRamca.append("rámec" +(i+1));
			AnalyzaRamca.append("\n");

			AnalyzaRamca.append("dåžka rámca poskytnutá paketovým drajverom - "
					+ Analyzer.DlzkaRamca.get(j) + "B");
			AnalyzaRamca.append("\n");

				AnalyzaRamca.append("dåžka rámca prenášaného po médiu - "
						+ Analyzer.DlzkaRamca.get(j+1) + "B");
				AnalyzaRamca.append("\n");
				
			j = j + 2; // na dlzku ramca prenasaneho po mediu z listu
            
			//Ak naslo Ipcku , teda tu ktoru chceme (ETHERNET II)
			
			//podmienky podla toho aky je to typ/dlzka ramca
			if (Analyzer.Decimal.get(i) >= 1536) {
				AnalyzaRamca.append("Ethernet II");
				AnalyzaRamca.append("\n");
			}

			if (Analyzer.Decimal.get(i) <= 1500) {
				
				if (Analyzer.Ramce.get(i).substring(28, 32).equals("ffff")){
					AnalyzaRamca.append("IEEE 802.3 – Raw");
					AnalyzaRamca.append("\n");
				}

				else if (Analyzer.Ramce.get(i).substring(28, 32).equals("aaaa")){
					AnalyzaRamca.append("IEEE 802.3- LLC - SNAP");
					AnalyzaRamca.append("\n");
				}

				else{
					AnalyzaRamca.append("IEEE 802.3 - LLC");
					AnalyzaRamca.append("\n");
				}
			}

			AnalyzaRamca.append("Zdrojová MAC adresa: ");
            		
			while (x != 24){
				AnalyzaRamca.append(Analyzer.Ramce.get(i).substring(x,(x+2)).toUpperCase()+" ");
				x = x + 2;
				}
			x = 0;
			
			AnalyzaRamca.append("\n");

			AnalyzaRamca.append("Cie¾ová MAC adresa: ");

			while (x != 12){
			AnalyzaRamca.append(Analyzer.Ramce.get(i).substring(x,(x+2)).toUpperCase()+" ");
			x = x + 2;
			}
			x = 12;
			AnalyzaRamca.append("\n");

			//Formatovany vypis podla zadania
			while (pocet != Analyzer.DlzkaRamca.get(where)*2) {

				AnalyzaRamca.append(Analyzer.Ramce.get(i).substring(pocet,(pocet+2)).toUpperCase()+" ");

				if (poc % 8 == 0)
					AnalyzaRamca.append("  ");
				
				if (poc % 16 == 0)
					AnalyzaRamca.append("\n");
				
				pocet = pocet+2;
				poc++;
			}

			pocet = 0;
			poc = 1;
			where = where + 2;
        }
        
    }
	
	public void VypisIP() throws Exception{
		

		//najdenie maximalnej velkosti prenosov bajtov a ich vypis
		try {
			int max = Collections.max(Analyzer.velkosti);
			AnalyzaRamca.append("\n");
			AnalyzaRamca.append("\n");
			
			AnalyzaRamca.append("IP adresy vysielajúcich uzlov:");
			AnalyzaRamca.append("\n");
			
			for (int i = 0; i < Analyzer.ip2.size(); i++) {
			
				AnalyzaRamca.append(Integer.parseInt(Analyzer.ip2.get(i).substring(0, 2), 16)
						+ "." + Integer.parseInt(Analyzer.ip2.get(i).substring(2, 4), 16)
						+ "." + Integer.parseInt(Analyzer.ip2.get(i).substring(4, 6), 16)
						+ "." + Integer.parseInt(Analyzer.ip2.get(i).substring(6, 8), 16));
				AnalyzaRamca.append("\n");
			}
			int index = Analyzer.velkosti.indexOf(max);//zistenie pozicie IP-cky s najvacsim poctom bajtov

			AnalyzaRamca.append("\n"+"Adresa uzla s najväèším poètom odvysielaných bajtov: "+"\n"
			        + Integer.parseInt(Analyzer.ip2.get(index).substring(0, 2), 16)
					+ "." + Integer.parseInt(Analyzer.ip2.get(index).substring(2, 4), 16)
					+ "." + Integer.parseInt(Analyzer.ip2.get(index).substring(4, 6), 16)
					+ "." + Integer.parseInt(Analyzer.ip2.get(index).substring(6, 8), 16)
					+ "   " + max + " bajtov");  
			AnalyzaRamca.append("\n");
		
		} catch (Exception e) {
			
			AnalyzaRamca.append("\n");
			AnalyzaRamca.append("Nemame analyzovat ARP Ipcky!");
			
		}
		
		
	}
}

