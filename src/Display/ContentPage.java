package Display;
import Data.Game;
import Data.Thumbnail;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class ContentPage extends Page {
	JButton backButton;
	JLabel icon;
	JLabel name;
	ArrayList<JPanel> commentPanels = new ArrayList<>(); //Use for moderation later
	Map<String, String> comments;
	String activeUser;
	JPanel addComment;
	
	public ContentPage(Game g, Thumbnail thumb) {
		super(g.GetName());
		
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
		
		addComment = new JPanel();
		JTextField commentText = new JTextField(20);
		JButton submitComment = new JButton("Add Comment");
		addComment.add(commentText);
		addComment.add(submitComment);
		submitComment.addActionListener(e -> submitComment(commentText));
		panel.add(addComment);
		
		readCommentData();
	}
	
	public void showAddComment(String activeUser) {
		this.activeUser = activeUser;
		
		addComment.setVisible(!activeUser.equals(""));
	}
	
	public void showDeleteButton(String activeUser) {
	    for (JPanel j : commentPanels) {
	        j.getComponent(2).setVisible(!activeUser.equals(""));
	    }
	}
	
	public void submitComment(JTextField commentFieldText) {
		comments.put(commentFieldText.getText(), activeUser);
		
		//Add new panel and clear comment text
		JPanel commentPanel = new JPanel();
		JLabel commentText = new JLabel(commentFieldText.getText());
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(
                e -> DeleteComment(commentPanel, commentText.getText()));
		JLabel nameText = new JLabel(comments.get(commentFieldText.getText()));
		commentPanel.add(nameText);
		commentPanel.add(commentText);
		commentPanel.add(deleteButton);
		commentPanels.add(commentPanel);
		panel.add(commentPanel);
		commentFieldText.setText("");
		panel.updateUI();
		
		Properties prop = new Properties();
		prop.putAll(comments);
		
		try {
			prop.store(new FileOutputStream("CommentDatabase/" + GetName() + ".txt"), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void readCommentData() {
		//Read from file
		Properties prop = new Properties();
		comments = new HashMap<>();
		
		try {
			prop.load(new FileInputStream("CommentDatabase/" + GetName() + ".txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Comment file for " + GetName() + " not added yet");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (String key : prop.stringPropertyNames()) {
		   comments.put(key, prop.get(key).toString());
		}
		
		//Add panels for comments
		for (String comment : comments.keySet()) {
			JPanel commentPanel = new JPanel();
			JLabel commentText = new JLabel(comment);
			JButton deleteButton = new JButton("Delete");
	        deleteButton.addActionListener(
	                e -> DeleteComment(commentPanel, commentText.getText()));
			JLabel nameText = new JLabel(comments.get(comment));
			commentPanel.add(nameText);
			commentPanel.add(commentText);
			commentPanel.add(deleteButton);
			commentPanels.add(commentPanel);
			panel.add(commentPanel);
		}
	}
	
	//Gets a reference to the back button for action lisntener
	public JButton GetBackButton() {
		return backButton;
	}
	
	// Deletes the comment after the "Delete" button is clicked
	void DeleteComment(JPanel display, String commentText) {
        
        comments.remove(commentText);
        
        commentPanels.remove(display);
        panel.remove(display);
        panel.updateUI();
        
        Properties prop = new Properties();
        prop.putAll(comments);
        
        try {
            prop.store(new FileOutputStream("CommentDatabase/" + GetName() + ".txt"), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
