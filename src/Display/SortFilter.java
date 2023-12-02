package Display;

import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import Data.Thumbnail;

public class SortFilter {
	JCheckBox check;
	
	public SortFilter(JPanel main) {
		check = new JCheckBox("Sort by Price");
		main.add(check);
	}
	
	public ArrayList<Thumbnail> applyFilters(ArrayList<Thumbnail> thumbsRaw) {
		if (check.isSelected()) {
			int pos;
			Thumbnail temp;
			
			for (int i = 0; i < thumbsRaw.size(); i++) {
				pos = i;
				
				for (int j = i + 1; j < thumbsRaw.size(); j++) {
					if (thumbsRaw.get(j).GetGame().GetPrice() < thumbsRaw.get(pos).GetGame().GetPrice()) {
						pos = j;
					}
				}
				
				temp = thumbsRaw.get(pos);
				thumbsRaw.set(pos, thumbsRaw.get(i));
				thumbsRaw.set(i, temp);
			}
			
			System.out.println("Sort by price order should be:");
			System.out.println("Timeshift, Hollow Knight, Scott Pilgrim, Danganronpa, Chicory, Celeste,");
			System.out.println("My Friend Pedro, Payday 2, Dark Souls 2, Dead Cells, Dredge, A Hat in Time");
			System.out.println("Dark Souls, Payday 3, Mario Wonder, Smash Bros, Kirby, Sonic, and Spider-Man");
			System.out.println();
		}
		
		return thumbsRaw;
	}
}
