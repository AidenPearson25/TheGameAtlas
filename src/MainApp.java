import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainApp {
	public static void main(String[] args) {
		ArrayList<Game> games = new ArrayList<>();
		
		try {
			Scanner readGames = new Scanner(new File("GameDatabase.txt"));
			while (true) {
				Game current = new Game(readGames.nextLine());
				current.SetDescription(readGames.nextLine());
				current.SetIconRef(readGames.nextLine());
				current.SetGenre(readGames.nextLine());
				current.SetPrice(new double[] {0.0}); //Add later
				current.SetPlatforms(new boolean[] {false}); //Add later
				readGames.nextLine(); //Delete later
				readGames.nextLine(); //Delete later
				games.add(current);
				
				if (!readGames.hasNextLine()) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(games.get(0).GetName());
		System.out.println(games.get(1).GetName());
	}
}
