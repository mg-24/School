
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


public class MainWindow extends JFrame{

	public JFrame frame;
	public ButtonGroup group;
	public JButton applicationButton;
	public JButton pushElasticButton;
	public JButton crawlingButton;

	private JPanel contentPane;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1406, 768);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		crawlingButton = new JButton("Crawl 'hej.sk' site");
		crawlingButton.setBounds(148, 77, 194, 23);
		contentPane.add(crawlingButton);
		
		pushElasticButton = new JButton("Push data to elastic search");
		pushElasticButton.setBounds(148, 122, 194, 23);
		contentPane.add(pushElasticButton);
		
		applicationButton = new JButton("Search Application");
		applicationButton.setBounds(148, 170, 194, 23);
		contentPane.add(applicationButton);
		
		

		setVisible(true);
	}
}
