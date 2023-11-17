package Display;

import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import Data.Game;
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
		}
		
		return thumbsRaw;
	}
}
