package Display;
import Data.Game;
import Data.Thumbnail;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class ContentPage extends Page {
	JButton backButton;
	JLabel icon; //Do these need to be class variables?
	JLabel name; //See above
	ArrayList<JPanel> commentPanels = new ArrayList<>(); //Use for moderation later
	Map<String, String> comments;
	String activeUser;
	JPanel addComment;
	
	JPanel commentSection;
	JScrollPane commentScroll;
	
	GridBagLayout gbrGame;
	GridBagConstraints gbcGame;
	
	public ContentPage(Game g) {
		super(g.GetName());
		commentSection = new JPanel();
		commentScroll = new JScrollPane();
		
		//Add info to sub panels
		addGameInfo(g);
		addCommentSection();
		readCommentData();
		updateScroll();
		
		gbrGame.setConstraints(commentScroll, gbcGame);
    panel.add(commentScroll);
	}
	
	public JLabel initializeImage(Game g) {
		ImageIcon image = new ImageIcon(g.GetIconRef());
		Image oldImage = image.getImage();
		Image newImage = oldImage.getScaledInstance(300, 300, java.awt.Image.SCALE_REPLICATE);
		image = new ImageIcon(newImage);
		
		icon = new JLabel(image);
		icon.setPreferredSize(new Dimension(300, 300));
		return icon;
	}
	
	public void addGameInfo(Game g) {
		//Setup
		gbrGame = new GridBagLayout();
		gbcGame = new GridBagConstraints();	
		panel.setLayout(gbrGame);
		int gridXCounter = 1;
		
		//Add back button
		gbcGame.weightx = 0;
		backButton = new JButton("Back");
		gbcGame.gridx = 0;
		gbrGame.setConstraints(backButton, gbcGame);
		panel.add(backButton);
		
		//Initial constraints
		gbcGame.gridx = gridXCounter;
		gbcGame.weightx = 1.0;
		gbcGame.weighty = 0;
		
		//Add name text
		name = new JLabel(g.GetName());
		gbrGame.setConstraints(name, gbcGame);
		panel.add(name);
		
		//Create image
		JLabel imageIcon = initializeImage(g);
		gbcGame.gridheight = 4;
		gbrGame.setConstraints(imageIcon, gbcGame);
		panel.add(imageIcon);
		
		//Change collumn
		gbcGame.gridheight = 1;
		gridXCounter++;
		gbcGame.gridx = gridXCounter;
		
		//Add average before others
		JLabel avgLabel = new JLabel("Average Price");
		gbrGame.setConstraints(avgLabel, gbcGame);
		panel.add(avgLabel);
		JLabel avgPrice = new JLabel(Double.toString(g.GetPrice()));
		gbrGame.setConstraints(avgPrice, gbcGame);
		panel.add(avgPrice);
		gridXCounter++;
		gbcGame.gridx = gridXCounter;
		
		for (int i = 0; i < g.GetPlatforms().length; i++) {
			//Add Platforms
			JLabel currentLabel = new JLabel(g.GetPlatformName()[i]);
			gbrGame.setConstraints(currentLabel, gbcGame);
			panel.add(currentLabel);
			
			JLabel currentPrice = new JLabel();
			//Add Price
			if (g.GetPlatform(i)) {
				currentPrice.setText(Double.toString(g.GetPriceAll()[i + 1]));
			} else {
				currentPrice.setText("Not Available");
			}
			
			gbrGame.setConstraints(currentPrice, gbcGame);
			panel.add(currentPrice);
			
			gridXCounter++;
			gbcGame.gridx = gridXCounter;
		}
		
		//Next row
		gbcGame.gridx = gridXCounter - (g.GetPlatforms().length + 1);
		gbcGame.gridwidth = g.GetPriceAll().length;
		gbcGame.weighty = 1.0;
		
		//Add genre
		JLabel genre = new JLabel("Genre: " + g.GetGenre());
		gbrGame.setConstraints(genre, gbcGame);
		panel.add(genre);
		
		//Add Description
		JTextArea description = new JTextArea(5, 65);
		//Style text area
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);
		description.setBackground(null);
		
		description.setText(g.GetDescription());
		gbrGame.setConstraints(description, gbcGame);
		panel.add(description);
		
		//Scroll setup
		gbcGame.fill = GridBagConstraints.BOTH;
		gbcGame.weighty = 2.0;
		gbcGame.gridheight = GridBagConstraints.REMAINDER;
	}
	
	public void updateScroll() {
		//commentSection.setPreferredSize(new Dimension(500, 200));
		
    //Set scroll constraints
		commentScroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		commentScroll.getVerticalScrollBar().setUnitIncrement(8);
		commentScroll.setPreferredSize(new Dimension(0, 200));
    commentScroll.setViewportView(commentSection);
	}
	
	/**
	 * Adds the add comment data
	 */
	public void addCommentSection() {
		//Get gridbag
		GridBagLayout gbrAddComment = new GridBagLayout();
		GridBagConstraints gbcAddComment = new GridBagConstraints();
		
		commentSection.setLayout(new BoxLayout(commentSection, BoxLayout.Y_AXIS));
		//commentSection.setLayout(new FlowLayout());
		
		//Create components
		addComment = new JPanel();
		addComment.setLayout(gbrAddComment);
		JTextField commentText = new JTextField(50);
		JButton submitComment = new JButton("Add Comment");
		
		//Add comment text
		gbcAddComment.weightx = 1;
		gbcAddComment.gridx = 0;
		gbrAddComment.setConstraints(commentText, gbcAddComment);
		addComment.add(commentText);
		
		//Add comment button
		gbcAddComment.gridx = 1;
		gbrAddComment.setConstraints(submitComment, gbcAddComment);
		addComment.add(submitComment);
		submitComment.addActionListener(e -> submitComment(commentText));
		
		commentSection.add(addComment);
	}
	
	/**
	 * Shows the add comment area only if you're logged in.
	 * @param activeUser
	 */
	public void showAddComment(String activeUser) {
		this.activeUser = activeUser;
		
		addComment.setVisible(!activeUser.equals(""));
	}
	
	/**
	 * Shows the delete comment button only if you're a moderator.
	 * @param activeUser
	 */
	public void showDeleteButton(String activeUser) {
	    for (JPanel j : commentPanels) {
	        j.getComponent(2).setVisible(!activeUser.equals(""));
	    }
	}
	
	/**
	 * When pushed by the user, submits area in the text field as a comment.
	 * @param commentFieldText
	 */
	public void submitComment(JTextField commentFieldText) {
		comments.put(commentFieldText.getText(), activeUser);
		
		//Add new panel and clear comment text
		JPanel commentPanel = new JPanel();
		JTextArea commentText = new JTextArea();
		commentText.setPreferredSize(new Dimension(450, 100));
		commentText.setText(commentFieldText.getText());
		commentText.setLineWrap(true);
		commentText.setWrapStyleWord(true);
		commentText.setEditable(false);
		
		JButton deleteButton = new JButton("X");
		deleteButton.setPreferredSize(new Dimension(50, 50));
		deleteButton.addActionListener(
                e -> DeleteComment(commentPanel, commentText.getText()));
		
		JTextArea nameText = new JTextArea(); //Change to icon later
		nameText.setPreferredSize(new Dimension(100, 100));
		nameText.setText(comments.get(commentFieldText.getText()));
		nameText.setLineWrap(true);
		nameText.setWrapStyleWord(true);
		nameText.setEditable(false);
		
		commentPanel.add(nameText);
		commentPanel.add(commentText);
		commentPanel.add(deleteButton);
		commentPanels.add(commentPanel);
		commentSection.add(commentPanel, 1);
		commentFieldText.setText("");
		panel.updateUI();
		
		Properties prop = new Properties();
		prop.putAll(comments);
		
		//Add comment data to text file
		try {
			prop.store(new FileOutputStream("CommentDatabase/" + GetName() + ".txt"), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		updateScroll();
	}
	
	/**
	 * Adds existing comments under the game page
	 */
	void readCommentData() {
		//Setup
		Properties prop = new Properties();
		comments = new HashMap<>();
		
		//Import map from file
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
			JTextArea commentText = new JTextArea();
			commentText.setPreferredSize(new Dimension(450, 100));
			commentText.setText(comment);
			commentText.setLineWrap(true);
			commentText.setWrapStyleWord(true);
			commentText.setEditable(false);
			//commentText.setBackground(null);
			
			JButton deleteButton = new JButton("X");
			deleteButton.setPreferredSize(new Dimension(50, 50));
	        deleteButton.addActionListener(
	                e -> DeleteComment(commentPanel, commentText.getText()));
	        
			JTextArea nameText = new JTextArea(); //Change to icon later
			nameText.setPreferredSize(new Dimension(100, 100));
			nameText.setText(comments.get(comment));
			nameText.setLineWrap(true);
			nameText.setWrapStyleWord(true);
			nameText.setEditable(false);
			//nameText.setBackground(null);
			
			commentPanel.add(nameText);
			commentPanel.add(commentText);
			commentPanel.add(deleteButton);
			commentPanels.add(commentPanel);
			commentSection.add(commentPanel);
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
        commentSection.remove(display);
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
        
        updateScroll();
    }
}
