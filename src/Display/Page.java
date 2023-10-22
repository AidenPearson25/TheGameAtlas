package Display;

import java.awt.FlowLayout;
import javax.swing.JPanel;

public class Page {
	String name;
	JPanel panel;
	
	//Constructor
	public Page(String name) {
		this.name = name;
		panel = new JPanel();
		FlowLayout fl_mainPage = (FlowLayout) panel.getLayout();
		fl_mainPage.setAlignment(FlowLayout.LEFT);
	}
	
	//Adds the JPanel to the content pane
	public JPanel DisplayPage(JPanel contentPane) {
		contentPane.add(panel, "name_35423506904000");

		return panel;
	}
	
	//Get or set name
	public void SetName(String name) {
		this.name = name;
	}
	
	public String GetName() {
		return name;
	}
	
	//Get reference to panel
	public JPanel GetRef() {
		return panel;
	}
}
