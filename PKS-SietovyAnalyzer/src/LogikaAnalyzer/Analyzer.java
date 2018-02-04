package LogikaAnalyzer;

import java.io.IOException;
import java.util.ArrayList;

import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;

import GUI.Test;

public class Analyzer {
	
	public static StringBuilder hexa = new StringBuilder();//String builder na ulozenie vsetkych ramcov
	public static int ramec = 0;
	public static ArrayList<String> ip2;//ulozene vsetky IPcky ramcov
	public static ArrayList<Integer> velkosti;//ulozene vsetky velkosti IPciek
	public static ArrayList<String> Ramce;// Ulozene vsetky ramce zo suboru
	public static ArrayList<Integer> DlzkaRamca;//i-ta pozicia - poskytnuta , i+1 pozicia - prenasana
	public static ArrayList<Integer> Decimal;//decimalne cislo na urcovanie typu/length
	public int dec = 0;
	public static PropertiesLoader load; // hlavny load suboru
	public static String MyPort;
	
public void VyberPort(String port) throws Exception{
	
	//System.out.println(port);
	load = new PropertiesLoader();
	MyPort = port;
	System.out.println(MyPort);
	
}
    

public void Analyza() throws Exception{
	
		PcapPacket packet = new PcapPacket(JMemory.POINTER);
        
        ip2 = new ArrayList<String>();
		velkosti = new ArrayList<Integer>();
		DlzkaRamca = new ArrayList<Integer>();
		Ramce = new ArrayList<String> ();
		Decimal = new ArrayList<Integer> ();
		
		//System.out.println(load.getPropValue(MyPort));
        int poc = 0;
        int FoundnewIP = 0;
        int pocet_ip = -1;
        String ipcka = "";
        
		while (Test.OpenedFile.nextEx(packet) != -2) {//while cyklus na nacitanie vsetkych paketov az dokonca
			
			for (int i = 0; i < packet.size(); i++) {
				hexa.append(String.format("%02x", packet.getUByte(i)));//pre analyzu v ramci while-u, jedneho ramca
				
			}
			
			Ramce.add(hexa.toString());// ulozene vsetky ramce v liste za sebou
			/*System.out.println(hexa);
			System.out.println(Ramce.get(0));*/
			ramec++; //pocitadlo poctu ramcov
			DlzkaRamca.add(packet.size()); //dlzka poskytnuta paketovym drajverom
			
			if (packet.size() >= 60) {
				DlzkaRamca.add(packet.size() + 4);//prenasana po mediu
				
			} else {

				DlzkaRamca.add(64);//prenasana po mediu
				
			}
			
			for (int i = 12; i < 13; i++) {

				dec += ((packet.getUByte(i) & 0x0f) * 16 * 16);//spodny polbyte
				dec += (((packet.getUByte(i) & 0xf0) >> 4) * 16 * 16 * 16);// horny polbyte v desiatkovej sustave
				
			}
			/* System.out.printf("%01x ",((packet.getUByte(7) & 0xf0) >> 4)); 
			/* System.out.println("-->"+((packet.getUByte(7) & 0xf0) >> 4)); */// to je spravny horny polbyte
		
			for (int i = 13; i < 14; i++)
				dec += packet.getUByte(i);
	
			Decimal.add(dec);//vlozim do listu decimalny tvar a podla toho urcujem Type/dlzku
         
		 if ((dec > 1536) && (hexa.substring(24, 28).equals(load.getPropValue("IPv4")))) {
				ipcka = hexa.substring(52, 60);
				
				/*System.out.println(ramec);
				System.out.println(hexa.substring(68,72));*/
				if ((Integer.parseInt(hexa.substring(46,48), 16)) == (Integer.parseInt(load.getPropValue("TCP"))))
				{
					//System.out.println("mamTCP");
					/*System.out.println(ramec);
					System.out.println((Integer.parseInt(hexa.substring(68,72), 16)));*/
					if (((Integer.parseInt(hexa.substring(68,72), 16))) == (Integer.parseInt(load.getPropValue(MyPort)))){
						Protokoly_AF Protokol = new Protokoly_AF();
						Protokol.ZaradAF();
						Protokoly_AF.IpAF.add(hexa.toString());
					}
					else if (((Integer.parseInt(hexa.substring(72,76), 16))) == (Integer.parseInt(load.getPropValue(MyPort)))){
						Protokoly_AF Protokol = new Protokoly_AF();
						Protokol.ZaradAF();
						Protokoly_AF.IpAF.add(hexa.toString());
					}

					if (((Integer.parseInt(hexa.substring(68,72), 16))) == (Integer.parseInt(load.getPropValue(MyPort)))){
						ftpdata ftp = new ftpdata();
						ftp.Zaradftp();
						ftpdata.ftpRamce.add(hexa.toString());
					}else if (((Integer.parseInt(hexa.substring(72,76), 16))) == (Integer.parseInt(load.getPropValue(MyPort)))){
						ftpdata ftp = new ftpdata();
						ftp.Zaradftp();
						ftpdata.ftpRamce.add(hexa.toString());
					}
					
					
				}
				if ((Integer.parseInt(hexa.substring(46,48), 16)) == (Integer.parseInt(load.getPropValue("ICMP")))){
					ICMP icmp = new ICMP();
					icmp.ZaradICMP();
					ICMP.IpICMP.add(hexa.toString());
					ICMP.PomTypeICMP.add((load.getPropValue(Integer.toString(Integer.parseInt(hexa.substring(68,70),16))))); // tu mam cislo type ICMP
				}
				
				if ((Integer.parseInt(hexa.substring(46,48), 16)) == (Integer.parseInt(load.getPropValue("UDP")))){
					TFTP tftp = new TFTP();
					tftp.ZaradTFTP();
					TFTP.TFTPRamce.add(hexa.toString());
					
				}
					
				FoundnewIP = 1;
			} else if ((dec > 1536) && (hexa.substring(24, 28).equals(load.getPropValue("ARP")))) {
				ARP ArpHeader = new ARP ();
				ArpHeader.ZaradARP(); // vytvori objekt pre ListARP
				ARP.IpARP.add(hexa.toString());
				
				//ArpHeader.ARPVypis();
				/*ipcka = hexa.substring(56, 64);
				FoundnewIP = 1; *///nemame vypisovat ARP
			} else if ((dec > 1536) && ((hexa.substring(24, 28).toUpperCase()).equals(load.getPropValue("IPv6")))) {
				ipcka = hexa.substring(44, 52);
				FoundnewIP = 1;
			}
		 
		 	dec = 0;

			//Ak naslo Ipcku , teda tu ktoru chceme (ETHERNET II)
			if (FoundnewIP == 1) {

				pocet_ip++;
				if (ip2.isEmpty()) { //Ak je list prazdny tak vlozim IP a aj velkost
					ip2.add(ipcka);
					pocet_ip++;
					poc++;
					velkosti.add(packet.size());

				}
            		
            		
				if (pocet_ip > 1) {

					for (int i = 0; i < ip2.size(); i++) {

						if ((ipcka.equals(ip2.get(i)))) { //Porovnanie vsetkych IPciek, ak sa 
							//najdu rovnake tak pridame do listu len velkost
							
							poc++;
							int Oldvalue = velkosti.get(i);
							int Newvalue = Oldvalue + packet.size();
							velkosti.set(i, Newvalue);

						}

					}
				}
				//Nenajdu sa rovnake tak pridam dalej do listov
				if (poc == 0) {
					ip2.add(ipcka);
					velkosti.add(packet.size());
					pocet_ip++;
				}
			}

			FoundnewIP = 0;
			poc = 0;
			
			hexa.delete(0, packet.size() * packet.size());
		
		}
		
	}

}
