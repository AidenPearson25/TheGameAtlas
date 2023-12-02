package TDD;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class SearchTDD {
	
	@Test
	public void searchTest() {
		String searchText = "Hollow Knight";
		String game1Name = "Hollow Knight";
		String game2Name = "Celeste";
		
		assertTrue(compareToSearch(searchText, game1Name) < (compareToSearch(searchText, game2Name)));
	}
	
	//Function copied from searchbar
	public int compareToSearch(String search, String compare) {
		int value = 0;
		int min = (compare.length() < search.length()) ? compare.length() : search.length();
		
		for (int i = 0; i < min; i++) {
			value += Math.abs(Character.compare(compare.toLowerCase().charAt(i), search.toLowerCase().charAt(i)));
		}
		
		return value;
	}
}
