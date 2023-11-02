package Data;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import java.awt.Image;

public class Thumbnail{
	Game game;
	ImageIcon image;
	JButton thumbnail;
	
	public Thumbnail(Game game) {
		this.game = game;
	}
	
	//Might not need this
	public void Clicked() {
		
	}
	
	public JButton Display() {
		image = new ImageIcon(game.GetIconRef());
		Image oldImage = image.getImage();
		Image newImage = oldImage.getScaledInstance(150, 150,  java.awt.Image.SCALE_REPLICATE);
		image = new ImageIcon(newImage);
		
		thumbnail = new JButton("", image);
		thumbnail.setPreferredSize(new Dimension(150, 150));
		
		//Add caption to thumbnail
		//JLabel nameLabel = new JLabel(g.GetName());
		//thumbnail.add(nameLabel);
		
		return thumbnail;
	}
	
	public JButton GetButton() {
		return thumbnail;
	}
	
	//Get game
	public Game GetGame() {
		return game;
	}
}
