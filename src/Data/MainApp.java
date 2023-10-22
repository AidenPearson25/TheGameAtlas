package Data;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import Display.Page;
import Display.SearchPage;
import Display.ContentPage;

public class MainApp extends JFrame {
	
	private static final long serialVersionUID = 1L;
	//Main pane of the frame
	private JPanel contentPane;
	
	//The current jpanel that's active
	static JPanel currentPage;
	
	//The current jscrollpane that's active (Might have to edit if more things require scrolls later)
	JScrollPane scroll;
	
	/**
	 * Main, sets up and runs program.
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainApp frame = new MainApp();
					frame.setVisible(true);
					frame.setTitle("The Game Atlas");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Constructor, makes the JFrame.
	 */
	public MainApp() {
		//Initial Setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1051, 531);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		//Setup for mainPage JPanel
		SearchPage mainPage = new SearchPage("searchPage");
		currentPage = mainPage.DisplayPage(contentPane); //Display page adds it to content pane
		
		//Read game data from text doc
		ArrayList<Game> games = readGameData("GameDatabase.txt");
		
		//Setup scroll 
		int ySize = (games.size() / 6) + 1; //Sets vertical scroll length
		mainPage.GetRef().setPreferredSize(new Dimension(1051, ySize * 150));
		
		scroll = new JScrollPane(mainPage.GetRef());
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(8);
		contentPane.add(scroll);
		
		//Create thumbnails, content pages, and button actions
		for (Game g : games) {
			//Create thumbnail
			Thumbnail thumbnail = new Thumbnail(g.GetName());
			mainPage.GetRef().add(thumbnail.Display(g)); //Will have to add GetRef to get the JPane;
			
			//Create content page
			ContentPage contentPage = new ContentPage(g.GetName());
			contentPage.DisplayPage(contentPane);
			contentPage.AddData(g, thumbnail);
			
			//Action Listeners will run when buttons are pushed
			thumbnail.GetButton().addActionListener(e -> ChangeActivePanel(contentPage.GetRef()));
			contentPage.GetBackButton().addActionListener(e -> ChangeActivePanel(mainPage.GetRef(), scroll));
		}
	}
	
	/**
	 * Change the active panel
	 * @param current
	 */
	void ChangeActivePanel(JPanel current) {
		scroll.setVisible(false);
		current.setVisible(true);
		MainApp.currentPage = current;
	}
	
	/**
	 * Change the active panel with scroll check (Might have to edit later)
	 * @param current
	 * @param currentScroll
	 */
	void ChangeActivePanel(JPanel current, JScrollPane currentScroll) {
		MainApp.currentPage.setVisible(false);
		current.setVisible(true);
		currentScroll.setVisible(true);
		MainApp.currentPage = current;
	}
	
	/**
	 * Reads the game data from a text doc, creates game objects, returns array.
	 * @param filename
	 * @return
	 */
	private static ArrayList<Game> readGameData(String filename) {
		ArrayList<Game> games = new ArrayList<>();
		int i = 0; //Detlete later
		
		//Scanner read
		while(i < 7) { //Delete later, pads for scroll testing
		try {
			Scanner readGames = new Scanner(new File(filename));
			while (true) { //Trust me on this
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
		i++; // Delete later
		}
		
		return games;
	}
	
	
}
