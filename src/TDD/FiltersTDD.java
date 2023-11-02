package TDD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.util.Scanner;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import Display.Filters;
import Data.MainApp;
import java.util.ArrayList;
import Data.Game;
import Data.Thumbnail;

public class FiltersTDD {
	ArrayList<Game> games;
	ArrayList<Thumbnail> thumbsRaw;
	ArrayList<Thumbnail> thumbsFiltered;
	
	@BeforeEach
	public void Setup() {
		MainApp mainApp = new MainApp();
		games = mainApp.readGameData("GameDatabase.txt");
		
	}
}
