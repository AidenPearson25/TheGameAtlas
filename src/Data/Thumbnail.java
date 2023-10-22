package Data;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import java.awt.Image;

public class Thumbnail{
	String name;
	String iconRef;
	ImageIcon image;
	JButton thumbnail;
	
	public Thumbnail(String name) {
		this.name = name;
	}
	
	//Might not need this
	public void Clicked() {
		
	}
	
	public JButton Display(Game g) {
		image = new ImageIcon(g.GetIconRef());
		Image oldImage = image.getImage();
		Image newImage = oldImage.getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH);
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
	
	//Get and set name
	public void SetName(String name) {
		this.name = name;
	}
	
	public String GetName() {
		return name;
	}
	
	//Get and set iconRef
	public void SetIconRef(String iconRef) {
		this.iconRef = iconRef;
	}
	
	public String GetIconRef() {
		return iconRef;
	}
}
