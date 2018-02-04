

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.UIManager;


public class Window extends JFrame {

	private JPanel contentPane;
	public JTextField queryField;
	private JLabel lblEnterQuery;
	public JButton btnMatch;
	public JTextArea autoCompleteArea;
	private JLabel lblPriceFrom;
	private JLabel lblPriceTo;
	public JTextField priceFrom;
	public JTextField priceTo;
	public JCheckBox checkInfo;
	private JLabel lblZoraPoda;
	public JRadioButton priceSortButton;
	public JRadioButton reviewSortButton;
	public JCheckBox checkDocumentation;
	public JTable table;
	public JCheckBox checkCategory;
	public ButtonGroup group;
	public JCheckBox checkInFactory;
	public JCheckBox checkInTitles;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1406, 768);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		queryField = new JTextField();
		queryField.setBounds(10, 40, 254, 20);
		contentPane.add(queryField);
		queryField.setColumns(10);
		
		btnMatch = new JButton("H¾adaj");
		btnMatch.setBounds(285, 39, 89, 23);
		contentPane.add(btnMatch);
		
		lblEnterQuery = new JLabel("Zadaj query");
		lblEnterQuery.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEnterQuery.setForeground(Color.BLACK);
		lblEnterQuery.setBackground(Color.BLACK);
		lblEnterQuery.setBounds(10, 11, 254, 14);
		contentPane.add(lblEnterQuery);
		
		autoCompleteArea = new JTextArea();
		autoCompleteArea.setBounds(10, 297, 480, 407);
		autoCompleteArea.setEditable(false);
		contentPane.add(autoCompleteArea);
		
		lblPriceFrom = new JLabel("Cena od:");
		lblPriceFrom.setForeground(Color.BLACK);
		lblPriceFrom.setBounds(23, 71, 66, 14);
		contentPane.add(lblPriceFrom);
		
		lblPriceTo = new JLabel("Cena do:");
		lblPriceTo.setBackground(UIManager.getColor("Button.focus"));
		lblPriceTo.setForeground(Color.RED);
		lblPriceTo.setBounds(23, 96, 66, 14);
		contentPane.add(lblPriceTo);
		
		priceFrom = new JTextField();
		priceFrom.setBounds(99, 71, 86, 20);
		contentPane.add(priceFrom);
		priceFrom.setColumns(10);
		
		priceTo = new JTextField();
		priceTo.setBounds(99, 93, 86, 20);
		contentPane.add(priceTo);
		priceTo.setColumns(10);
		
		checkInfo = new JCheckBox("V popise");
		checkInfo.setBounds(10, 144, 89, 23);
		contentPane.add(checkInfo);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(255, 255, 255));
		separator.setBounds(260, 257, 1, -90);
		contentPane.add(separator);
		
		checkCategory = new JCheckBox("V kateg\u00F3ri\u00E1ch");
		checkCategory.setBounds(99, 144, 134, 23);
		contentPane.add(checkCategory);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 192, 474, 2);
		contentPane.add(separator_1);
		
		lblZoraPoda = new JLabel("Zoraï zostupne pod¾a:");
		lblZoraPoda.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblZoraPoda.setBounds(10, 231, 175, 14);
		contentPane.add(lblZoraPoda);
		
		priceSortButton = new JRadioButton("Ceny");
		priceSortButton.setBounds(199, 202, 122, 23);
		contentPane.add(priceSortButton);
		
		reviewSortButton = new JRadioButton("Poètu recenzií");
		reviewSortButton.setBounds(199, 228, 122, 23);
		contentPane.add(reviewSortButton);
		
		group = new ButtonGroup();
		group.add(priceSortButton);
		group.add(reviewSortButton);
		
		checkDocumentation = new JCheckBox("S dokument\u00E1ciou");
		checkDocumentation.setBounds(380, 39, 128, 23);
		contentPane.add(checkDocumentation);
		
		DefaultTableModel model = new DefaultTableModel() {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		model.addColumn("Názov");
		model.addColumn("Cena");
		model.addColumn("Výrobca");
		model.addColumn("Dokumentácia k produktu");
		model.addColumn("Poèet recenzií");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(508, 11, 848, 693);

		contentPane.add(scrollPane);
		table = new JTable(model);
		scrollPane.setViewportView(table);
		
		JLabel label = new JLabel("Zoraï vzostupne pod¾a:");
		label.setFont(new Font("Tahoma", Font.BOLD, 12));
		label.setBounds(10, 205, 175, 14);
		contentPane.add(label);
		
		checkInFactory = new JCheckBox("Vo v\u00FDrobcoch");
		checkInFactory.setBounds(235, 144, 139, 23);
		contentPane.add(checkInFactory);
		
		checkInTitles = new JCheckBox("V názvoch");
		checkInTitles.setBounds(387, 144, 97, 23);
		contentPane.add(checkInTitles);
		
		setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}
}
