package TDD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Data.Game;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CommentTDD {
	ArrayList<Game> games = new ArrayList<>();
	Map<String, String> comments;
	@BeforeEach
	public void gameSetup() {
		try {
      Scanner readGames = new Scanner(new File("GameDatabase.txt"));
      while (true) { // Trust me on this
          Game current = new Game(readGames.nextLine());
          current.SetDescription(readGames.nextLine());
          current.SetIconRef(readGames.nextLine());
          current.SetGenre(readGames.nextLine());

          String tempDouble = readGames.nextLine();
          Scanner readDouble = new Scanner(tempDouble);
          double total = 0;
          int divideBy = 0;
          for (int i = 0; i < 3; i++) {
              current.SetPrices(readDouble.nextDouble(), i + 1);
              if (current.GetPriceAll()[i + 1] != -1) {
                  total += current.GetPriceAll()[i + 1];
                  divideBy++;
              }
          }
          readDouble.close();

          total /= divideBy;
          current.SetPrices(total, 0);

          String tempBool = readGames.nextLine();
          Scanner readBool = new Scanner(tempBool);
          for (int i = 0; i < 3; i++) {
              current.SetPlatforms(readBool.nextBoolean(), i);
          }
          readBool.close();

          games.add(current);

          if (!readGames.hasNextLine()) {
              break;
          } else {
              readGames.nextLine();
          }
      }
		} catch (FileNotFoundException e) {
      e.printStackTrace();
		}
	}
	
	@Test
	public void makeCommentMapTest() {
		Properties prop = new Properties();
		comments = new HashMap<>();
		
		//Code copied from contentPage where comments are read
		try {
			prop.load(new FileInputStream("CommentDatabase/" + games.get(0).GetName() + ".txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Comment file for " + games.get(0).GetName() + " not added yet");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (String key : prop.stringPropertyNames()) {
		   comments.put(key, prop.get(key).toString());
		}
		
		assertNotEquals(comments, null);
		assertEquals(comments.get("Lets goooo"), "Typha");
		assertEquals(comments.get("This comment does not exist"), null);
	}
}
