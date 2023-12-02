package Display;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

import Data.Thumbnail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Filters {
	JCheckBox[] checks = new JCheckBox[3]; //Current check boxes for filters
	String[] checkText = { "Steam", "Epic Games", "Nintendo Switch" }; //Current text of said filters
	Map<String, ArrayList<Thumbnail>> data = new HashMap<String, ArrayList<Thumbnail>>(); //Map to take care of sorting
	
	/**
	 * Constructor class to add checks and button.
	 * @param main
	 */
	public Filters(JPanel main) {
		createChecks(main);
	}
	
	//Addes checkboxes, as many as needed
	void createChecks(JPanel main) {
		for (int i = 0; i < 3; i++) {
			checks[i] = new JCheckBox(checkText[i]);
			main.add(checks[i]);
		}
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
					}
				}
			}
		}
	}
	
	/**
	 * Uses the filtered map to return a specific list of thumbnails that match the keys
	 * @return
	 */
	public ArrayList<Thumbnail> ApplyFilters(ArrayList<Thumbnail> thumbsRaw) {
		ArrayList<Thumbnail> filtered = new ArrayList<>();
		int filter = -1;
		
		for (int i = 0; i < 3; i++) {
			if (checks[i].isSelected()) {
				filter = i;
				filtered = data.get(checkText[i]);
			}
		}
		
		switch (filter) {
			case 0: //Steam
				System.out.println("Filter results should be:");
				System.out.println("Hollow Knihgt, Celeste, Timeshift, Dark Souls, Spider-Man, Dark Souls 2,");
				System.out.println("A Hat in Time, Payday 2, Payday 3, Sonic, Dead Cells, Danganronpa,");
				System.out.println("Chicory, Scott Pilgrim, Dredge, My Friend Pedro");
				break;
				
			case 1: //Epic
				System.out.println("Filter results should be:");
				System.out.println("Hollow Knight, Dark SOuls, Dark Souls 2, Payday 2, Payday 3, Dead Cells,");
				System.out.println("Chicory, Scott Pilgrim");
				break;
				
			case 2: //Switch
				System.out.println("Filter results should be:");
				System.out.println("Hollow Knight, Celeste, Dark Souls, Mario Wonder, Kirby, A Hat in Time,");
				System.out.println("Payday 2, Sonic, Dead Cells, Danganronpa, Chicory, Smash Bros,");
				System.out.println("Scott Pilgrim, Dredge, My Friend Pedro");
				break;
				
			default:
				System.out.println("No filters applied.");
		}
		
		System.out.println();
		
		return (filtered.isEmpty()) ? thumbsRaw : filtered;
	}
}
