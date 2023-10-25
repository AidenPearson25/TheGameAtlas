package Display;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import Data.Thumbnail;
import Data.Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Filters {
	JCheckBox[] checks = new JCheckBox[3]; //Current check boxes for filters
	String[] checkText = { "Steam", "Epic Games", "Nintendo Switch" }; //Current text of said filters
	JButton apply; //Apply filter button
	Map<String, ArrayList<Thumbnail>> data = new HashMap<String, ArrayList<Thumbnail>>(); //Map to take care of sorting
	
	/**
	 * Constructor class to add checks and button.
	 * @param main
	 */
	public Filters(JPanel main) {
		createChecks(main);
		apply = new JButton("Apply Filters");
		main.add(apply);
	}
	
	//Addes checkboxes, as many as needed
	void createChecks(JPanel main) {
		for (int i = 0; i < 3; i++) {
			checks[i] = new JCheckBox(checkText[i]);
			main.add(checks[i]);
		}
	}
	
	//Returns button for action listener purposes
	public JButton GetButton() {
		return apply;
	}
	
	/**
	 * Adds the data from the thumbnails list to create a map of thumbnails for filtering
	 * @param raw
	 */
	public void AddFilterData(ArrayList<Thumbnail> raw) {
		//ArrayList<Thumbnail> filtered = new ArrayList<>();
		for (Thumbnail t : raw) {
			boolean[] platforms = t.GetGame().GetPlatforms();
			for (int i = 0; i < 3; i++) {
				if (platforms[i]) {
					if (!data.containsKey(checkText[i])) {
						ArrayList<Thumbnail> addNew = new ArrayList<>();
						addNew.add(t);
						data.put(checkText[i], addNew);
					} else {
						data.get(checkText[i]).add(t);
						//data.put(checkText[i], data.get(checkText[i]).add(t));
					}
				}
			}
		}
	}
	
	/**
	 * Uses the filtered map to return a specific list of thumbnails that match the keys
	 * @return
	 */
	public ArrayList<Thumbnail> ApplyFilters() {
		ArrayList<Thumbnail> filtered = new ArrayList<>();
		
		for (int i = 0; i < 3; i++) {
			if (checks[i].isSelected()) {
				filtered = data.get(checkText[i]);
			}
		}
		
		return filtered;
	}
}
