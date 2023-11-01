package Display;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.util.ArrayList;
import Data.Game;
import Data.Thumbnail;

public class SearchPage extends Page {
	public SearchPage(String name) {
		super(name);
	}
	
	public JScrollPane ScrollSetup(ArrayList<Game> games) {
		int ySize = (games.size() / 6) + 1; // Sets vertical scroll length
    panel.setPreferredSize(new Dimension(1051, ySize * 150));

    JScrollPane scroll = new JScrollPane(panel);
    scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(8);
    
    return scroll;
	}
	
	public Thumbnail addThumb(Game g) {
		Thumbnail thumbnail = new Thumbnail(g);
    panel.add(thumbnail.Display());
    return thumbnail;
	}
}
