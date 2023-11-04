package Display;
import java.util.ArrayList;
import Data.Thumbnail;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;

/**
 * Class for the search bar, able to search games by name.
 * @author Aiden Pearsron
 */
public class SearchBar {
	//Button and textfield variables
	JButton button;
	public JTextField searchText;
	
	//Create JObjects and add
	public SearchBar(JPanel mainPage) {
		button = new JButton("Search");
		searchText = new JTextField(20);
		mainPage.add(searchText);
		mainPage.add(button);
	}
	
	//Get button reference
	public JButton getButton() {
		return button;
	}
	
	//Sort through thumbnails, sorting by how close the name is to the search text
	public ArrayList<Thumbnail> sortThumbs(ArrayList<Thumbnail> thumbsRaw) {		
		ArrayList<Thumbnail> thumbsSorted = new ArrayList<>();
		if (searchText.getText().equals("")) {
			return thumbsRaw;
		}
		
		//thumbsSorted.add(thumbsRaw.get(0));
		for (Thumbnail t : thumbsRaw) {
			for (int i = 0; i < thumbsSorted.size(); i++) {
				int newCompare = compareToSearch(t.GetGame().GetName());
				int oldCompare = compareToSearch(thumbsSorted.get(i).GetGame().GetName());
				if (newCompare < oldCompare) {
					thumbsSorted.add(i, t);
					break;
				}
			}
			
			//5 is how lenient the sort is for typos, might make higher later
			if (!thumbsSorted.contains(t) && compareToSearch(t.GetGame().GetName()) < 5) {
				thumbsSorted.add(t);
			}
		}
		
		return thumbsSorted;
	}
	
	//Custom compare code to have total string difference instead of cumulative char difference
	int compareToSearch(String compare) {
		int value = 0;
		String search = searchText.getText();
		int min = (compare.length() < search.length()) ? compare.length() : search.length();
		
		for (int i = 0; i < min; i++) {
			value += Math.abs(Character.compare(compare.toLowerCase().charAt(i), search.toLowerCase().charAt(i)));
		}
		
		return value;
	}
}
