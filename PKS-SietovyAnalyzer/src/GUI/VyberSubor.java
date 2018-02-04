package GUI;

import java.util.Scanner;

import javax.swing.JFileChooser;

import org.jnetpcap.Pcap;

import LogikaAnalyzer.AnalyzerVypis;


public class VyberSubor {

	//public static int vybrate; 
	public static String suborik;
	JFileChooser fileChooser = new JFileChooser();
	public StringBuilder sb = new StringBuilder();
	public static Pcap pcap;
	AnalyzerVypis zacni = new AnalyzerVypis();
	
	@SuppressWarnings("static-access")
	public void Vyber() throws Exception{
		
		final StringBuilder errbuf = new StringBuilder();
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			
			java.io.File file = fileChooser.getSelectedFile();
			Scanner input = new Scanner(file);
			suborik = file.toString(); 
			suborik = suborik.substring(suborik.lastIndexOf("\\") + 1);
	
			while (input.hasNext()){
				sb.append(input.nextLine());
				sb.append("\n");
				
			}
			input.close();
			
			//System.out.printf("Opening file for reading: %s%n", suborik);  
		    
	        pcap = Pcap.openOffline(suborik, errbuf);  
	  
	        if (pcap == null) {  
	        	zacni.AnalyzaRamca.append("ERROR"); 
	            return;  
	        }  
			
		}
		
		else 
			zacni.AnalyzaRamca.append("no file");
		
	}
	
}
