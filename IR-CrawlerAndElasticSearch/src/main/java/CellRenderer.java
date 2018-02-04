import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class CellRenderer extends DefaultTableCellRenderer implements
		TableCellRenderer {

	Elastic elastic;
	ArrayList<String> highlightPhrases = new ArrayList<String>();

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		
		 super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		
			String str = value.toString();
			
		for (int i = 0; i < Elastic.highliter.size() - 1; i++) {
			
			String regex;
			regex = "\\b";
			
				regex += Elastic.highliter.get(i).toString();
			
	        System.out.println(regex);
	        
	        if (!regex.startsWith("{")) {
	        	
	        	Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(str);
	
	            if(matcher.find()){
	
	                String aux = matcher.group();
	                str = str.replace(aux, "<b>" + aux + "</b>");
	            }
	        }        
	}
		str = "<html>" + str + "</html>";
		
        setText(str);           
     
        return this;
    }
}
