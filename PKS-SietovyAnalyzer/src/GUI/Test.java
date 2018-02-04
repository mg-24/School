package GUI;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextArea;

import org.jnetpcap.Pcap;

import java.awt.event.ActionEvent;

import LogikaAnalyzer.ARP;
import LogikaAnalyzer.Analyzer;
import LogikaAnalyzer.AnalyzerVypis;
import LogikaAnalyzer.ICMP;
import LogikaAnalyzer.Protokoly_AF;
import LogikaAnalyzer.TFTP;
import LogikaAnalyzer.ftpdata;

import java.awt.event.ActionListener;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class Test extends JFrame {

	private JPanel contentPane;
	public static Pcap OpenedFile;
	private JComboBox <String>comboBox ;

	/**
	 * Launch the application.
	 */
	public void VytvorOkno() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test frame = new Test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Test() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 592, 311);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnVyberSubor = new JButton("Vyber subor");
		btnVyberSubor.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				VyberSubor subor = new VyberSubor();
				
				try {
					subor.Vyber();
					OpenedFile = VyberSubor.pcap;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		btnVyberSubor.setBounds(368, 220, 114, 23);
		contentPane.add(btnVyberSubor);
		
		/*JButton btnAnalyzuj = new JButton("Analyzuj");
		btnAnalyzuj.setBounds(301, 216, 89, 23);
		contentPane.add(btnAnalyzuj);*/
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 11, 546, 198);
		contentPane.add(scrollPane);
		
		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		/*JButton btnArpvypis = new JButton("ARPVypis");
		btnArpvypis.setBounds(439, 216, 89, 23);
		contentPane.add(btnArpvypis);*/
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(10, 221, 296, 20);
		contentPane.add(comboBox);
		
		comboBox.addItem("Analyza pri ARP a zakladnom vypise");
		comboBox.addItem("ARPVypis");
		comboBox.addItem("AnalyzaVypis");
		comboBox.addItem("HTTP");
		comboBox.addItem("HTTPS");
		comboBox.addItem("TELNET");
		comboBox.addItem("SSH");
		comboBox.addItem("FTP_riadiace");
		comboBox.addItem("FTP_datove");
		comboBox.addItem("TFTP");
		comboBox.addItem("ICMP");
		comboBox.addItem("ftpdatovevypis");
		//comboBox.setSelectedIndex(0);
		//Pre ARP Vypis
		
		comboBox.addActionListener(new ActionListener() {

			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				
				String Select = (String)comboBox.getSelectedItem();
				if (Select.equals("ARPVypis")){
					
					ARP arp = new ARP();
					Analyzer Analyzuj = new Analyzer();
					try {
						Analyzuj.VyberPort("");
						arp.ARPVypis();
					} catch (Exception e) {
						AnalyzerVypis.AnalyzaRamca.append("NAJPRV STLAC ANALYZA!");
						AnalyzerVypis.AnalyzaRamca.append("Nemame ziadne ARP");
					}
					textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
					
				}
				
				if (Select.equals("Analyza pri ARP a zakladnom vypise")){
					
					Analyzer Analyzuj = new Analyzer();
				
					try {
						Analyzuj.VyberPort("");
						Analyzuj.Analyza();
					} catch (Exception e) {
						AnalyzerVypis.AnalyzaRamca.append("Nemame vybraty subor!");
					}
					textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
					
					
				}
				if (Select.equals("AnalyzaVypis")){
					
					AnalyzerVypis AnVypis = new AnalyzerVypis();
					
					try {
						AnVypis.Vypis(0);
						AnVypis.VypisIP();
					} catch (Exception e) {
						AnalyzerVypis.AnalyzaRamca.append("NAJPRV STLAC ANALYZA!");
						
					}
					textArea.setText(AnVypis.AnalyzaRamca.toString());
				}
				
					if (Select.equals("HTTP")){
					
					Analyzer Analyzuj = new Analyzer();
					Protokoly_AF http = new Protokoly_AF();
					
					try {
						Analyzuj.VyberPort("http");
						Analyzuj.Analyza();
						http.AFVypis();
					} catch (Exception e) {
						AnalyzerVypis.AnalyzaRamca.append("NemameHTTP!");
					}
					textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
						
				}
					
					if (Select.equals("ICMP")){
						
						Analyzer Analyzuj = new Analyzer();
						ICMP icmp = new ICMP();
						
						try {
							Analyzuj.VyberPort("");
							Analyzuj.Analyza();
							icmp.ICMPVypis();
						} catch (Exception e) {
							AnalyzerVypis.AnalyzaRamca.append("NemameICMP!");
						}
						textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
							
					}
					
					if (Select.equals("TFTP")){
						
						Analyzer Analyzuj = new Analyzer();
						TFTP tftp = new TFTP();
						
						try {
							Analyzuj.VyberPort("tftp");
							Analyzuj.Analyza();
							tftp.TFTPVypis();
						} catch (Exception e) {
							AnalyzerVypis.AnalyzaRamca.append("NemameTFTP!");
						}
						textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
							
					}
					
					if (Select.equals("HTTPS")){
						
						Analyzer Analyzuj = new Analyzer();
						Protokoly_AF https = new Protokoly_AF();
						
						try {
							Analyzuj.VyberPort("https");
							Analyzuj.Analyza();
							https.AFVypis();
						} catch (Exception e) {
							AnalyzerVypis.AnalyzaRamca.append("NemameHTTPS!");
						}
						textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
							
					}
					
					if (Select.equals("TELNET")){
						
						Analyzer Analyzuj = new Analyzer();
						Protokoly_AF telnet = new Protokoly_AF();
						
						try {
							Analyzuj.VyberPort("telnet");
							Analyzuj.Analyza();
							telnet.AFVypis();
						} catch (Exception e) {
							AnalyzerVypis.AnalyzaRamca.append("NemameTELNET!");
						}
						textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
							
					}
					
					if (Select.equals("SSH")){
						
						Analyzer Analyzuj = new Analyzer();
						Protokoly_AF ssh = new Protokoly_AF();
						
						try {
							Analyzuj.VyberPort("ssh");
							Analyzuj.Analyza();
							ssh.AFVypis();
						} catch (Exception e) {
							AnalyzerVypis.AnalyzaRamca.append("NemameSSH!");
						}
						textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
							
					}
					
					if (Select.equals("FTP_riadiace")){
						
						Analyzer Analyzuj = new Analyzer();
						Protokoly_AF ftp = new Protokoly_AF();
						
						try {
							Analyzuj.VyberPort("ftp-control");
							Analyzuj.Analyza();
							ftp.AFVypis();
						} catch (Exception e) {
							AnalyzerVypis.AnalyzaRamca.append("NemameFTPriadiace!");
						}
						textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
							
					}
					
					if (Select.equals("FTP_datove")){
						
						Analyzer Analyzuj = new Analyzer();
						Protokoly_AF ftp = new Protokoly_AF();
						
						try {
							Analyzuj.VyberPort("ftp-data");
							Analyzuj.Analyza();
							ftp.AFVypis();
						} catch (Exception e) {
							AnalyzerVypis.AnalyzaRamca.append("NemameFTPdatove!");
						}
						textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
							
					}
					
					if (Select.equals("ftpdatovevypis")){
						
						Analyzer Analyzuj = new Analyzer();
						ftpdata ftp = new ftpdata();
						
						try {
							Analyzuj.VyberPort("ftp-data");
							Analyzuj.Analyza();
							ftp.Vypisftp();
						} catch (Exception e) {
							AnalyzerVypis.AnalyzaRamca.append("NemameFTPdatove!");
						}
						textArea.setText(AnalyzerVypis.AnalyzaRamca.toString());
							
					}
			}
			
		});
	}
}
