package Display;
import Data.Game;
import Data.Thumbnail;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class ContentPage extends Page {
	JButton backButton;
	JLabel name;
	JLabel icon;
	
	public ContentPage(String name) {
		super(name);
	}
	
	public void AddData(Game g, Thumbnail thumb) {
		//Set icon
		ImageIcon image = new ImageIcon(g.GetIconRef());
		Image oldImage = image.getImage();
		Image newImage = oldImage.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
		image = new ImageIcon(newImage);
		
		icon = new JLabel(image);
		icon.setPreferredSize(new Dimension(300, 300));
		panel.add(icon);
		
		//Add name text
		name = new JLabel(g.GetName());
		panel.add(name);
		
		//Add back button
		backButton = new JButton("Back");
		panel.add(backButton);
	}
	
	//Gets a reference to the back button for action lisntener
	public JButton GetBackButton() {
		return backButton;
	}
}
