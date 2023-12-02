package Display;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.util.ArrayList;
import Data.Game;
import Data.Thumbnail;

/**
 * Child of page, the main page of the Game Atlas.
 * @author Aiden Pearson
 */
public class SearchPage extends Page {
	
	JPanel header;
	JPanel content;
	JScrollPane scroll;
	GridBagLayout gbr;
	GridBagConstraints gbc;
	
	/**
	 * Constructor for search page, adds data.
	 * @param name
	 * @param games
	 */
	public SearchPage(String name, ArrayList<Game> games) {
		super(name);
		
		//Making gridbag components
		gbr = new GridBagLayout();
		panel.setLayout(gbr);
		gbc = new GridBagConstraints();
		
		//Initial constraints
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		
		//Create subpanels
		header = new JPanel();
		content = new JPanel();
		gbr.setConstraints(header, gbc);
		panel.add(header);
		
		//Setting for constraints for scrolling later
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weighty = 1.0;
	}
	
	//Might not need
	public void updateScroll(ArrayList<Thumbnail> thumbs) {
		//Need to remove old scroll but can't if it's not there
		if (scroll != null) {
			panel.remove(scroll);
		}
		
		//Set height (Change 6 to be adaptive to number of panels
		int ySize = (thumbs.size() / 6) + 1; // Sets vertical scroll length
    content.setPreferredSize(new Dimension(1051, ySize * 160));

    //Set scroll constraints
    scroll = new JScrollPane(content);
    scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(8);
    
    //Add to panel;
    gbr.setConstraints(scroll, gbc);
    panel.add(scroll);
	}
	
	/**
	 * Adds a thumbnail to content JPanel.
	 * @param g Game to add
	 * @return
	 */
	public Thumbnail addThumb(Game g) {
		Thumbnail thumbnail = new Thumbnail(g);
    content.add(thumbnail.Display());
    return thumbnail;
	}
	
	//Get sub panels
	public JPanel getHeader() {
		return header;
	}
	
	public JPanel getContent() {
		return content;
	}
}
