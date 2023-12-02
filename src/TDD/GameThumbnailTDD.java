package TDD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.util.Scanner;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import Data.Game;
import Data.Thumbnail;

public class GameThumbnailTDD {
	final String DESCRIPTION = "Forge your own path in Hollow Knight!" +
														 " An epic action adventure through a vast" +
														 " ruined kingdom of insects and heroes. Explore" +
														 " twisting caverns, battle tainted creatures and" +
														 " befriend bizarre bugs, all in a classic," +
														 " hand-drawn 2D style.";
	Game game;
	
	@BeforeEach
	public void Setup() throws FileNotFoundException {
		Scanner read = new Scanner(new File("GameDatabase.txt"));
		game = new Game(read.nextLine());
		game.SetDescription(read.nextLine());
    game.SetIconRef(read.nextLine());
    game.SetGenre(read.nextLine());
    game.SetPrice(Double.parseDouble(read.nextLine()));
		
		String tempBool = read.nextLine();
		Scanner temp = new Scanner(tempBool);
		for (int i = 0; i < 3; i++) {
      game.SetPlatforms(temp.nextBoolean(), i);
		}
		temp.close();
	}
	
	@Test
	public void testReadGameDatabase(){
		assertEquals(game.GetName(), "Hollow Knight");
		assertEquals(game.GetDescription(), DESCRIPTION);
		assertEquals(game.GetIconRef(), "iconImages/hollowknight.jpg");
		assertEquals(game.GetGenre(), "Metroidvania");
		assertEquals(game.GetPrice(), 14.99, 0.0f);
		assertEquals(game.GetPlatform(0), true);
		assertEquals(game.GetPlatform(1), true);
		assertEquals(game.GetPlatform(2), true);
	}
	
	@Test
	public void testThumbnail() {
		Thumbnail thumbnail = new Thumbnail(game);
    thumbnail.Display();

    assertEquals(thumbnail.GetButton().getPreferredSize(), new Dimension(150, 150));
	}
}
