package TDD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Data.Game;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class FilterTDD {

	ArrayList<Game> games = new ArrayList<>();
	
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
	public void filterTest() {
		assertTrue(games.get(0).GetPlatform(0));
		assertTrue(games.get(0).GetPlatform(1));
		assertTrue(games.get(0).GetPlatform(2));
		assertFalse(games.get(1).GetPlatform(1));
	}
}
