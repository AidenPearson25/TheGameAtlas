package Display;
import Data.Game;
import Data.Thumbnail;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ContentPage extends Page {
	JButton backButton;
	JLabel name;
	JLabel icon;
	ArrayList<JPanel> commentPanels = new ArrayList<>();
	
	public ContentPage(String name) {
		super(name);
	}
	
	public void AddData(Game g, Thumbnail thumb) {
		//Set icon
		ImageIcon image = new ImageIcon(g.GetIconRef());
		Image oldImage = image.getImage();
		Image newImage = oldImage.getScaledInstance(300, 300, java.awt.Image.SCALE_REPLICATE);
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
		
		AddCommentData(g.GetCommentData());
	}
	
	public void AddCommentData(String data) {
		System.out.println(data);
		Map<String, String> comments = new HashMap<>();
		data = data.substring(1);
		String username = "";
		String comment = "";
		boolean colon = false;
		while(true) { //Trust me on this
			if (data.substring(0, 1).equals("}")) {
				comments.put(comment, username);
				break;
			}
			
			if (!colon) {
				if (data.substring(0, 1).equals("=")) {
					colon = true;
				} else {
					username += data.substring(0, 1);
				}
			} else {
				if (data.substring(0, 1).equals(",")) {
					colon = false;
					comments.put(comment, username);
					username = "";
					comment = "";
					data = data.substring(1);
				} else {
					comment += data.substring(0, 1);
				}
			}
			data = data.substring(1);
		}
		
		for (String name : comments.keySet()) {
			JPanel commentPanel = new JPanel();
			JLabel nameText = new JLabel(name);
			JLabel commentText = new JLabel(comments.get(name));
			commentPanel.add(nameText);
			commentPanel.add(commentText);
			commentPanels.add(commentPanel);
			panel.add(commentPanel);
		}
	}
	
	//Gets a reference to the back button for action lisntener
	public JButton GetBackButton() {
		return backButton;
	}
}
