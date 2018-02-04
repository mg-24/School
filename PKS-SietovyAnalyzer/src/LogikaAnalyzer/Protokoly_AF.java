package LogikaAnalyzer;

import java.io.IOException;
import java.util.ArrayList;

//trieda pre vsetky komunikacie s protokolmy v a-f
public class Protokoly_AF {
	
	public static ArrayList<String> IpAF; //vsetky HTTPcky v liste
	public static ArrayList<Integer> CisloAFRamec;//cislo ramca z analyzy ramcov
	public static ArrayList<String> PomIpAF;
	public static ArrayList<String> Pom2AF;
	public static ArrayList<String> RamecRovnake;
	public static ArrayList<String> Kompletna;
	public static ArrayList<String> Nekompletna;
	public static ArrayList<Integer> Pom2CisloAFRamec;
	public static ArrayList<Integer> NekompletnaCisloRamca;
	public static ArrayList<Integer> PomCisloAFRamecRovnake;
	public static ArrayList<Integer> KompletnaCisloRamca;
	public static ArrayList<String> Zhody;
	public static ArrayList<Integer> DlzkaRamcov;
	
	public static int pocetAF;
	
	public void ZaradAF(){
		
		
		pocetAF++;
		if (pocetAF== 1){ 
		Zhody = new ArrayList<String>();
		KompletnaCisloRamca = new ArrayList<Integer>();
		NekompletnaCisloRamca = new ArrayList<Integer>();
		DlzkaRamcov = new ArrayList<Integer>();
		RamecRovnake = new ArrayList<String>();
		Kompletna = new ArrayList<String>();
		Nekompletna = new ArrayList<String>();
		IpAF = new ArrayList<String>();
		CisloAFRamec = new ArrayList<Integer>();
		Pom2CisloAFRamec = new ArrayList<Integer>();
		PomCisloAFRamecRovnake = new ArrayList<Integer>();
		PomIpAF = new ArrayList<String>();
		Pom2AF = new ArrayList<String>();
		}
		CisloAFRamec.add(Analyzer.ramec); // tu sa nachadza cislo ramca na 0te pozicii je cislo prveho ramca!!!
		
	}
	public void AFVypis() throws IOException {
		
		
		int i = 0;
		int pocet = 0;
		int velkost = 0;
		int statis0 = 0, statis1 =0, statis2 = 0, statis3 = 0, statis4 = 0,statis5 =0,statis6=0,statis7=0,statis8=0,statis9=0,statis10=0;
		int serverkom = 0;
		int klientkom = 0;
		int servernekom = 0;
		int klientnekom = 0;
		int hotovo = 0;
		int pom = 0; // na zachytenie flagov
		String flag;
		int moznost1=0, moznost2=0, moznost3=0,moznost4=0,moznost5=0,moznost6 = 0;
		int ukoncenakom = 0;
		int ukoncenanekom = 0;
		int threekoniec = 0;
		int done = 0;
		int inakomu = 0;
		int threeway = 0; // zaciatok
		int fourway = 0; // koniec
		//int Hex = Integer.parseInt(IpAF.get(0).substring(92,96),16);
		//System.out.println(IpAF.get(0).substring(92,96));
		/*System.out.println( Integer.toBinaryString(Hex).substring(9,15));*/ // substring poslednych 6ich bitov flagu TCP
		// v poradi : Urgent, ACK,push,RST,SYN,FIN
		
		//zistit ktora je klient a server IP
		/*System.out.println(IpAF.get(0).substring(60,68));
		System.out.println(Analyzer.MyPort);*/
		
		AnalyzerVypis analyza = new AnalyzerVypis();
		
		for (int o = 0; o < 11; o++)
			DlzkaRamcov.add(0);
		
		while (i < IpAF.size() - 1 ){
			
			/*Zhody.add(IpAF.get(i)); */// aby som ho potom nebral do uvahy
			for (int j = i+1; j < IpAF.size(); j++){
				/*System.out.println(IpAF.size());
				System.out.println(j);*/
			if ((((Integer.parseInt(IpAF.get(i).substring(68,72),16)) == Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort))) // server serversource port
				&& ((Integer.parseInt(IpAF.get(j).substring(68,72),16)) == Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort))) // server source port
				&&	((Integer.parseInt(IpAF.get(i).substring(72,76),16)) == (Integer.parseInt(IpAF.get(j).substring(72,76),16))) //dest port servera
				&& (IpAF.get(i).substring(52,60).equals(IpAF.get(j).substring(52,60))) //source == source IP
				&& (IpAF.get(i).substring(60,68).equals(IpAF.get(j).substring(60,68)))) //dest == dest IP
				|| (((Integer.parseInt(IpAF.get(i).substring(72,76),16)) == Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort))) // klient klient
				&& ((Integer.parseInt(IpAF.get(j).substring(72,76),16)) == Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))
				&& ((Integer.parseInt(IpAF.get(i).substring(68,72),16)) == (Integer.parseInt(IpAF.get(j).substring(68,72),16)))
				&& (IpAF.get(i).substring(52,60).equals(IpAF.get(j).substring(52,60))) //source == source IP
				&& (IpAF.get(i).substring(60,68).equals(IpAF.get(j).substring(60,68))))
				|| (((Integer.parseInt(IpAF.get(i).substring(72,76),16)) == Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort))) //klient-server
				&& ((Integer.parseInt(IpAF.get(j).substring(68,72),16)) == Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))
				&& ((Integer.parseInt(IpAF.get(i).substring(68,72),16)) == (Integer.parseInt(IpAF.get(j).substring(72,76),16)))
				&& (IpAF.get(i).substring(52,60).equals(IpAF.get(j).substring(60,68))) // source == dest port
				&& (IpAF.get(i).substring(60,68).equals(IpAF.get(j).substring(52,60))))
				|| (((Integer.parseInt(IpAF.get(i).substring(68,72),16)) == Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort))) //server-klient
				&& ((Integer.parseInt(IpAF.get(j).substring(72,76),16)) == Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))
				&& ((Integer.parseInt(IpAF.get(i).substring(72,76),16)) == (Integer.parseInt(IpAF.get(j).substring(68,72),16)))
				&& (IpAF.get(i).substring(60,68).equals(IpAF.get(j).substring(52,60))) // source == dest port
				&& (IpAF.get(i).substring(52,60).equals(IpAF.get(j).substring(60,68))))) 
				 //viem ze to bude rovnaka komunikacia
			{
				if (RamecRovnake.isEmpty()){
					pocet++;
				RamecRovnake.add(IpAF.get(i)); //ulozim si prvy ramec
				PomCisloAFRamecRovnake.add(CisloAFRamec.get(i)); 
				
				}
				pocet++;
				RamecRovnake.add(IpAF.get(j));
				PomCisloAFRamecRovnake.add(CisloAFRamec.get(j)); // ulozim si cislo ramca z analyzovaneho suboru
				done = 1;
				
				}
			if (done == 0 && hotovo == 0){ inakomu = j; 
			
			hotovo = 1;
				}// do premennej si ulozim cislo ramca ak je ina komunikacia len raz!
			
			if ((j == IpAF.size() - 1) && (hotovo == 0))
				inakomu = j; // ak prejdem cele a nenajdem inu komunikaciu tak sa nastavim na koniec a end
			
			if (hotovo == 0) done = 0;
			}
			if (inakomu > 0) i = inakomu; // od tadeto zacnem ten while
			
			
			for (int y = 0; y < RamecRovnake.size(); y++){ // zistujem pociatocny 3-way handshake SYN, SYN/ACK, ACK
				if (threeway == 0 || threeway == 1 || threeway == 2) {// zistime ci je to klient a hladame mu SYN
					if ((Integer.parseInt(RamecRovnake.get(y).substring(72, 76),16)) 
							== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ 
					pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
					flag = (Integer.toBinaryString(pom).substring(9,15));
					
					if ((flag.substring(4,5).equals("1")) && (flag.substring(1,2).equals("0"))){ // ak klient ma SYN
						Pom2AF.add(RamecRovnake.get(y));
						Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
						threeway = 1; // mam SYN
						}
					}
					
				}

				if (threeway == 1 || threeway == 2){ // ak mame SYN hladame SERVER SYN/ACK alebo rovnake SYN/ack
					if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16)) 
							== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme SERVER
						pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
						flag = (Integer.toBinaryString(pom).substring(9,15));
						
						if ((flag.substring(4,5).equals("1")) && (flag.substring(1,2).equals("1"))){ // ak SERVER ma SYN/ACK
							Pom2AF.add(RamecRovnake.get(y));
							Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
							threeway = 2; // mam SYN/ACK
						}
						
					}
					
				}

				if (threeway == 2 || threeway == 3 ){ // ak mame SYN/ACK hladame KLIENT ACK alebo rovnake ACK
					if ((Integer.parseInt(RamecRovnake.get(y).substring(72, 76),16)) 
							== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme KLIENT
						pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
						flag = (Integer.toBinaryString(pom).substring(9,15));
						
						if ((flag.substring(5,6).equals("1"))) threeway = 4; // pre FIN aby nesla podmienka
						
						if (threeway == 2 || threeway == 3){
							if ((flag.substring(1,2).equals("1")) && (flag.substring(4,5).equals("0")) && (flag.substring(5,6).equals("0"))){ // ak KLIENT ma ACK
							Pom2AF.add(RamecRovnake.get(y));
							Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
							threeway = 3; // mam ACK - komunikacia je zacata ak je threeway = 3 
							}
						}
						
					}
					if (threeway == 3 ){
						if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16)) 
							== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme SERVER
						pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
						flag = (Integer.toBinaryString(pom).substring(9,15));
						
							if ((flag.substring(5,6).equals("1"))) threeway = 4;
							
							if (threeway == 3){
								if ((flag.substring(1,2).equals("1")) && (flag.substring(4,5).equals("0")) && (flag.substring(5,6).equals("0"))){ // ak SERVER ma ACK
									Pom2AF.add(RamecRovnake.get(y));
									Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
									threeway = 3; // mam ACK - komunikacia je zacata ak je threeway = 3 
								}
							}
						
						}
					}
				
				}
				
				
					if (threeway == 3 || threeway == 4){ // ak mame 3way , tak je zacata mozme sa pozerat na ukoncenie
						
						if (fourway == 0 || fourway == 1){
						if ((Integer.parseInt(RamecRovnake.get(y).substring(72, 76),16))// 1.moznost - -Klient FIN-1.,3.moznost, Server ACK, ServerFIN, Klient ACK
								== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme KLIENT FIN
							pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
							flag = (Integer.toBinaryString(pom).substring(9,15));
							
							if ((flag.substring(5,6).equals("1")) && (flag.substring(3,4).equals("0"))){ // ak KLIENT ma FIN
								Pom2AF.add(RamecRovnake.get(y));
								Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
								 // vieme ze to je prva moznost a hladame k nemu patriace bity
								moznost1 = 1 ;moznost3 = 1;
								threekoniec = 1;
								fourway = 1; // mame FIN
								}
							
							}else if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16))
									== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // 2 .moznost,4., - zacina SERVER FIN
								pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
								flag = (Integer.toBinaryString(pom).substring(9,15));
								
								if ((flag.substring(5,6).equals("1")) && (flag.substring(3,4).equals("0"))){ // ak SERVER ma FIN
									Pom2AF.add(RamecRovnake.get(y));
									Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
									moznost2= 1; moznost4 = 1;
									threekoniec = 1;
									fourway = 1; // mame FIN alebo FIN/ACK
									//System.out.println("naslo finack");
								
								}
							}
						
						}
						 if (fourway == 0 || fourway == 1 || fourway == 2 || fourway == 3 || fourway == 4){ // RST moze prist hocikedy
							 if ((Integer.parseInt(RamecRovnake.get(y).substring(72, 76),16))
								== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // 5 .moznost - konci Klient RST
							pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
							flag = (Integer.toBinaryString(pom).substring(9,15));
							
								if ((flag.substring(3,4).equals("1"))){ // ak KLIENT ma RST tak koniec
								Pom2AF.add(RamecRovnake.get(y));
								Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
								moznost5 = 1;
								 
								fourway = 4; // mame RST je ukoncena!!! ale chcem hladat este za nim dalsie RST alebo RST/ACK, FIN/RST
							//break;
									}
								}
						 }
						 
						 if (fourway == 0 || fourway == 1 || fourway == 2 || fourway == 3 || fourway == 4){ // RST moze prist hocikedy
							 if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16))
								== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // 6 .moznost - konci SERVER RST
							pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
							flag = (Integer.toBinaryString(pom).substring(9,15));
							
								if ((flag.substring(3,4).equals("1"))){ // ak KLIENT ma RST tak koniec
								Pom2AF.add(RamecRovnake.get(y));
								Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
							    moznost6 = 1;
								fourway = 4; // mame RST je ukoncena!!! ale chcem hladat este za nim dalsie RST alebo RST/ACK, FIN/RST
							//break;
									}
								}
						 }
						 
						
						if (fourway == 1 || fourway == 2){
							
							if (moznost1 == 1 || moznost3 == 1){ //vieme ze je to prvy pripad tak hladame SERVER ACK
							if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16))// 1.moznost - Klient FIN, -Server ACK-, ServerFIN, Klient ACK
									== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme SERVER ACK
								pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
								flag = (Integer.toBinaryString(pom).substring(9,15));
								
								if ((flag.substring(1,2).equals("1")) && (flag.substring(3,4).equals("0")) && (flag.substring(5,6).equals("0"))){ // ak SERVER ma ACk a RST = 0
									Pom2AF.add(RamecRovnake.get(y));
									Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
									moznost1 = 2;
									fourway = 2; // mame ACK
									}
								
								}
							else if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16))// 3.moznost - SERVER FINACK
									== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme SERVER FINACK
								pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
								flag = (Integer.toBinaryString(pom).substring(9,15));
								
								if ((flag.substring(1,2).equals("1")) && (flag.substring(3,4).equals("0")) && (flag.substring(5,6).equals("1"))){ // ak KLIENT ma ACk a RST = 0
									Pom2AF.add(RamecRovnake.get(y));
									Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
									threekoniec = 2;
									moznost3 = 2;
									fourway = 2; // mame FInACK SERVERA
									}
								
								}
							}
							if (moznost2 == 1 || moznost4 == 1){
								if ((Integer.parseInt(RamecRovnake.get(y).substring(72, 76),16))// 2.moznost - Klient ACK
									== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme Klient ACK
								pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
								flag = (Integer.toBinaryString(pom).substring(9,15));
								
								if ((flag.substring(1,2).equals("1")) && (flag.substring(3,4).equals("0")) && (flag.substring(5,6).equals("0"))){ // ak KLIENT ma ACk a RST = 0
									Pom2AF.add(RamecRovnake.get(y));
									Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
									moznost2 = 2;
									fourway = 2; // mame ACK KLIENTA
									}
								
								}
								else if ((Integer.parseInt(RamecRovnake.get(y).substring(72, 76),16))// 4.moznost - Klient FINACK
										== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme Klient FINACK
									pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
									flag = (Integer.toBinaryString(pom).substring(9,15));
									
									if ((flag.substring(1,2).equals("1")) && (flag.substring(3,4).equals("0")) && (flag.substring(5,6).equals("1"))){ // ak KLIENT ma FIN ACk a RST = 0
										Pom2AF.add(RamecRovnake.get(y));
										Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
										moznost4 = 2;
										threekoniec = 2;
										fourway = 2; // mame FINACK KLIENTA
										}
									
									}
							
							}
						}
						

						if (fourway == 2 || fourway == 3){
							if (moznost1 == 2 || moznost3 == 2){
								if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16))// 1.moznost - Klient FIN, Server ACK, -ServerFIN-, Klient ACK
									== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme SERVER FIN
								pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
								flag = (Integer.toBinaryString(pom).substring(9,15));
								
								if ((flag.substring(5,6).equals("1")) && (flag.substring(3,4).equals("0"))){ // ak SERVER ma FIN a RST = 0
									Pom2AF.add(RamecRovnake.get(y));
									Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
									moznost1 = 3;
									fourway = 3; // mame FIN
									}
								
								}
								else if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16))// 3.moznost - KlientACK
										== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme KlientACK
									pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
									flag = (Integer.toBinaryString(pom).substring(9,15));
									
									if ((flag.substring(1,2).equals("1")) && (flag.substring(3,4).equals("0")) && (flag.substring(5,6).equals("0"))){ // ak Klient ma ACK a RST = 0
										Pom2AF.add(RamecRovnake.get(y));
										Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
										threekoniec = 3; // je koniec konecneho threewayu
										moznost3 = 3;
										fourway = 3; // mame ACK
										System.out.println("moznost3");
										}
									
									}
							
							}
							
							if (moznost2 == 2 || moznost4 == 2){
								if ((Integer.parseInt(RamecRovnake.get(y).substring(72, 76),16))// 2.moznost - Klient FIN
									== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme KLIENT FIN
								pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
								flag = (Integer.toBinaryString(pom).substring(9,15));
								
								if ((flag.substring(5,6).equals("1")) && (flag.substring(3,4).equals("0"))){ // ak KLIENT ma FIN a RST = 0
									Pom2AF.add(RamecRovnake.get(y));
									Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
									moznost2 = 3;
									fourway = 3; // mame FIN
									}
								
								}
								else if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16))// 4.moznost -SERVER ACK
										== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme SERVER ACK
									pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
									flag = (Integer.toBinaryString(pom).substring(9,15));
									
									if ((flag.substring(1,2).equals("1")) && (flag.substring(3,4).equals("0")) && (flag.substring(5,6).equals("0"))){ // ak SERVEr ma ACk a RST = 0
										Pom2AF.add(RamecRovnake.get(y));
										Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
										moznost4 = 3;
										threekoniec = 3; // koniec konecneho threewayu
										fourway = 3; // mame ACK
										}
									
									}
							}
						}
						

						if (fourway == 3 || fourway == 4){
								if ((moznost1 == 3 || moznost2 == 3) && (threekoniec != 3)){
									if ((Integer.parseInt(RamecRovnake.get(y).substring(72, 76),16))// 1.moznost - Klient FIN, Server ACK, ServerFIN, -Klient ACK-
									== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme KLIENT ACK
								pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
								flag = (Integer.toBinaryString(pom).substring(9,15));
								
									if ((flag.substring(1,2).equals("1")) && (flag.substring(3,4).equals("0")) && (flag.substring(5,6).equals("0"))){ // ak KLIENT ma ACK a RST =0
									Pom2AF.add(RamecRovnake.get(y));
									Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
									moznost1 = 4; //koniec 
									fourway = 4; // mame ACK, mame uz komunikaciu ukoncenu, mozme ulozit a nastavit premennu na 1, aby sa to
									//nevykonalo druhykrat kedze nam staci jedna kompletna
									System.out.println("moznost1 alebo 3");
										}
								
								
									}
									
									else if ((Integer.parseInt(RamecRovnake.get(y).substring(68, 72),16))// 2.moznost - Server ACK
											== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){ // najdeme SERVER ACK
										pom = Integer.parseInt(RamecRovnake.get(y).substring(92,96),16); // tu mame flag v 16tkovej sustave ako int
										flag = (Integer.toBinaryString(pom).substring(9,15));
										
											if ((flag.substring(1,2).equals("1")) && (flag.substring(3,4).equals("0")) && (flag.substring(5,6).equals("0"))){ // ak KLIENT ma ACK a RST =0
											Pom2AF.add(RamecRovnake.get(y));
											Pom2CisloAFRamec.add(PomCisloAFRamecRovnake.get(y));
											moznost1 = 4; //koniec 
											fourway = 4; // mame ACK, mame uz komunikaciu ukoncenu, mozme ulozit a nastavit premennu na 1, aby sa to
											//nevykonalo druhykrat kedze nam staci jedna kompletna
												}
										
										
											}
								}
							}
						
						
						
					}
				
			}
			
			// ak mame ukoncenu komunikaciu kompletnu
			if ((fourway == 4 && ukoncenakom == 0) || (threekoniec == 3 && ukoncenakom ==0)){
				
				for (int x = 0; x < Pom2AF.size(); x++){
					Kompletna.add(Pom2AF.get(x)); // sem si ulozim ramce kompletnej komunikacie 
					KompletnaCisloRamca.add(Pom2CisloAFRamec.get(x)); // tu mam cislo ramca originalne ako v subore 
					
					
				}
				ukoncenakom = 1;
				
				
			}
			
			if ((((threeway == 4 || threeway == 3)&& ukoncenanekom == 0) && (fourway < 4))
					&& (((threeway == 4 || threeway == 3)&& ukoncenanekom == 0) && (threekoniec < 3)))
					 {
				
				for (int x = 0; x < Pom2AF.size(); x++){
					Nekompletna.add(Pom2AF.get(x));
					NekompletnaCisloRamca.add(Pom2CisloAFRamec.get(x));
					
				}
				ukoncenanekom = 1;
				
				
			}
			if (ukoncenanekom == 1 && ukoncenakom == 1)
				break;
			
		moznost1=0;moznost2=0; moznost3=0;moznost4=0;moznost5=0;moznost6 = 0;
		hotovo = 0;
		RamecRovnake.clear();
		PomCisloAFRamecRovnake.clear();
		Pom2AF.clear();
		Pom2CisloAFRamec.clear();
		threeway = 0;
		threekoniec = 0;
		fourway = 0;
		
		}
		
		if (!(Kompletna.isEmpty())){
			
			for (int x = 0; x < Kompletna.size(); x++){

				if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 0
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 20){
					statis1++;
					DlzkaRamcov.add(0,statis1);
					
				}else if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 20
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 40){
					statis2++;
					DlzkaRamcov.add(1,statis2);
					
				}else if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 40
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 80){
					statis3++;
					DlzkaRamcov.add(2,statis3);
					
				}else if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 80
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 160){
					statis4++;
					DlzkaRamcov.add(3,statis4);
				}
				else if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 160
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 320){
					statis5++;
					DlzkaRamcov.add(4,statis5);
				}
				else if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 320
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 640){
					statis6++;
					DlzkaRamcov.add(5,statis6);
				}
				else if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 640
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 1280){
					statis7++;
					DlzkaRamcov.add(6,statis7);
				}
				else if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 1280
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 2560){
					statis8++;
					DlzkaRamcov.add(7,statis8);
				}
				else if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 2560
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 5120){
					statis9++;
					DlzkaRamcov.add(8,statis9);
				}
				else if (Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) > 5120
						&& Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1)) < 10240){
					statis10++;
					DlzkaRamcov.add(9,statis10);
				}
				
				
				
				
				
			}
			
			if (Kompletna.size() > 20){
				for (int x = 0; x < 10; x++){
					if (x == 0) AnalyzerVypis.AnalyzaRamca.append ("Komunikacia kompletna");
					
					if (klientkom == 0){
						if ((Integer.parseInt(Kompletna.get(x).substring(72, 76),16))
					== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){
				klientkom++;
				AnalyzerVypis.AnalyzaRamca.append ("\n");
				AnalyzerVypis.AnalyzaRamca.append ("Klient: " + Integer.parseInt(Kompletna.get(x).substring(52, 54), 16)
						+ "." + Integer.parseInt(Kompletna.get(x).substring(54, 56), 16)
						+ "." + Integer.parseInt(Kompletna.get(x).substring(56, 58), 16)
						+ "." + Integer.parseInt(Kompletna.get(x).substring(58, 60), 16)
						+ ":" + Integer.parseInt(Kompletna.get(x).substring(68,72), 16));
				
					}
				}
				if (serverkom == 0){
					if ((Integer.parseInt(Kompletna.get(x).substring(72, 76),16))
						== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){
					serverkom++;
					AnalyzerVypis.AnalyzaRamca.append ("	Server: " + Integer.parseInt(Kompletna.get(x).substring(60, 62), 16)
							+ "." + Integer.parseInt(Kompletna.get(x).substring(62,64), 16)
							+ "." + Integer.parseInt(Kompletna.get(x).substring(64, 66), 16)
							+ "." + Integer.parseInt(Kompletna.get(x).substring(66, 68), 16)
							+ ":" + Analyzer.MyPort
							+ "("+Analyzer.load.getPropValue(Analyzer.MyPort)+")");
					AnalyzerVypis.AnalyzaRamca.append ("\n");
					
					}
				}
				
				analyza.KonkretnyVypis(KompletnaCisloRamca.get(x));
				
				
			
			}
				for (int x = Kompletna.size() - 10 ; x < Kompletna.size(); x++){
					analyza.KonkretnyVypis(KompletnaCisloRamca.get(x));
					
					}
				
				
					
				}
			else {
				for (int x = 0 ; x < Kompletna.size(); x++){
					if (x == 0) AnalyzerVypis.AnalyzaRamca.append ("Komunikacia kompletna");
					if (klientkom == 0){
						if ((Integer.parseInt(Kompletna.get(x).substring(72, 76),16))
					== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){
				klientkom++;
				AnalyzerVypis.AnalyzaRamca.append ("\n");
				AnalyzerVypis.AnalyzaRamca.append ("Klient: " + Integer.parseInt(Kompletna.get(x).substring(52, 54), 16)
						+ "." + Integer.parseInt(Kompletna.get(x).substring(54, 56), 16)
						+ "." + Integer.parseInt(Kompletna.get(x).substring(56, 58), 16)
						+ "." + Integer.parseInt(Kompletna.get(x).substring(58, 60), 16)
						+ ":" + Integer.parseInt(Kompletna.get(x).substring(68,72), 16));
				
					}
				}
				if (serverkom == 0){
					if ((Integer.parseInt(Kompletna.get(x).substring(72, 76),16))
						== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){
					serverkom++;
					AnalyzerVypis.AnalyzaRamca.append ("	Server: " + Integer.parseInt(Kompletna.get(x).substring(60, 62), 16)
							+ "." + Integer.parseInt(Kompletna.get(x).substring(62,64), 16)
							+ "." + Integer.parseInt(Kompletna.get(x).substring(64, 66), 16)
							+ "." + Integer.parseInt(Kompletna.get(x).substring(66, 68), 16)
							+ ":" + Analyzer.MyPort
							+ "("+Analyzer.load.getPropValue(Analyzer.MyPort)+")");
					AnalyzerVypis.AnalyzaRamca.append ("\n");
					
					}
				}
				
				analyza.KonkretnyVypis(KompletnaCisloRamca.get(x));
					
				//Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(x) -1)+ (KompletnaCisloRamca.get(x) -1));
				}
			}
		}
		//System.out.println(DlzkaRamcov.get(0));
		
		AnalyzerVypis.AnalyzaRamca.append("Štatistika dåžky rámcov v bajtoch: ");
		AnalyzerVypis.AnalyzaRamca.append("\n");
		int premenna = 0;
		for (int x = 0 ; x < 10;x++){
			if (x==0) {
				AnalyzerVypis.AnalyzaRamca.append(premenna+"-"+(premenna+19)+":"+DlzkaRamcov.get(x));
				premenna = premenna + 20;
				AnalyzerVypis.AnalyzaRamca.append("\n");
			}
			else{ 
				AnalyzerVypis.AnalyzaRamca.append(premenna+"-"+((premenna*2)-1)+":"+DlzkaRamcov.get(x));
			AnalyzerVypis.AnalyzaRamca.append("\n");
			premenna = premenna*2;
			}
			
			
		}
		
		//System.out.println(Analyzer.DlzkaRamca.get((KompletnaCisloRamca.get(0) -1) + (KompletnaCisloRamca.get(0) -1)));
		
		
		
		if (!(Nekompletna.isEmpty())){
			
			if (Nekompletna.size() > 20){
				for (int x = 0; x < 10; x++){
					
					if (x == 0) AnalyzerVypis.AnalyzaRamca.append ("Komunikacia nekompletna");
					if (klientnekom == 0){
						if ((Integer.parseInt(Nekompletna.get(x).substring(72, 76),16))
					== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){
				klientnekom++;
				AnalyzerVypis.AnalyzaRamca.append ("\n");
				AnalyzerVypis.AnalyzaRamca.append ("Klient: " + Integer.parseInt(Nekompletna.get(x).substring(52, 54), 16)
						+ "." + Integer.parseInt(Nekompletna.get(x).substring(54, 56), 16)
						+ "." + Integer.parseInt(Nekompletna.get(x).substring(56, 58), 16)
						+ "." + Integer.parseInt(Nekompletna.get(x).substring(58, 60), 16)
						+ ":" + Integer.parseInt(Nekompletna.get(x).substring(68,72), 16));
				
					}
				}
				if (servernekom == 0){
					if ((Integer.parseInt(Nekompletna.get(x).substring(72, 76),16))
						== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){
					servernekom++;
					AnalyzerVypis.AnalyzaRamca.append ("	Server: " + Integer.parseInt(Nekompletna.get(x).substring(60, 62), 16)
							+ "." + Integer.parseInt(Nekompletna.get(x).substring(62,64), 16)
							+ "." + Integer.parseInt(Nekompletna.get(x).substring(64, 66), 16)
							+ "." + Integer.parseInt(Nekompletna.get(x).substring(66, 68), 16)
							+ ":" + Analyzer.MyPort
							+ "("+Analyzer.load.getPropValue(Analyzer.MyPort)+")");
					AnalyzerVypis.AnalyzaRamca.append ("\n");
					
					}
				}
				
				analyza.KonkretnyVypis(NekompletnaCisloRamca.get(x));
				
				}
				for (int x = Nekompletna.size() - 10 ; x < Nekompletna.size(); x++){
					analyza.KonkretnyVypis(NekompletnaCisloRamca.get(x));
					
					}
				
				
				
			}
			
			else {
			for (int x = 0 ; x < Nekompletna.size(); x++){
				if (x == 0) AnalyzerVypis.AnalyzaRamca.append ("Komunikacia nekompletna");
				if (klientnekom == 0){
					if ((Integer.parseInt(Nekompletna.get(x).substring(72, 76),16))
				== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){
			klientnekom++;
			AnalyzerVypis.AnalyzaRamca.append ("\n");
			AnalyzerVypis.AnalyzaRamca.append ("Klient: " + Integer.parseInt(Nekompletna.get(x).substring(52, 54), 16)
					+ "." + Integer.parseInt(Nekompletna.get(x).substring(54, 56), 16)
					+ "." + Integer.parseInt(Nekompletna.get(x).substring(56, 58), 16)
					+ "." + Integer.parseInt(Nekompletna.get(x).substring(58, 60), 16)
					+ ":" + Integer.parseInt(Nekompletna.get(x).substring(68,72), 16));
			
				}
			}
			if (servernekom == 0){
				if ((Integer.parseInt(Nekompletna.get(x).substring(72, 76),16))
					== (Integer.parseInt(Analyzer.load.getPropValue(Analyzer.MyPort)))){
				servernekom++;
				AnalyzerVypis.AnalyzaRamca.append ("	Server: " + Integer.parseInt(Nekompletna.get(x).substring(60, 62), 16)
						+ "." + Integer.parseInt(Nekompletna.get(x).substring(62,64), 16)
						+ "." + Integer.parseInt(Nekompletna.get(x).substring(64, 66), 16)
						+ "." + Integer.parseInt(Nekompletna.get(x).substring(66, 68), 16)
						+ ":" + Analyzer.MyPort
						+ "("+Analyzer.load.getPropValue(Analyzer.MyPort)+")");
				AnalyzerVypis.AnalyzaRamca.append ("\n");
				
					}
				}
			
			analyza.KonkretnyVypis(NekompletnaCisloRamca.get(x));
			
		
				}
			}
		
		}
		
	}
		
}

