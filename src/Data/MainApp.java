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
	private JPanel contentPane;
	
	static JPanel currentPage;
	JScrollPane scroll;
	
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
	
	public MainApp() {
		ArrayList<Game> games = readGameData("GameDatabase.txt");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1051, 531);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		SearchPage mainPage = new SearchPage("searchPage");
		currentPage = mainPage.DisplayPage(contentPane);
		
		int ySize = (games.size() / 6) + 1;
		mainPage.GetRef().setPreferredSize(new Dimension(1051, ySize * 150));
		
		scroll = new JScrollPane(mainPage.GetRef());
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(8);
		contentPane.add(scroll);
		
		for (Game g : games) {
			Thumbnail thumbnail = new Thumbnail(g.GetName());
			mainPage.GetRef().add(thumbnail.Display(g));
			
			ContentPage contentPage = new ContentPage(g.GetName());
			contentPage.DisplayPage(contentPane);
			contentPage.AddData(g, thumbnail);
			
			thumbnail.GetButton().addActionListener(e -> ChangeActivePanel(contentPage.GetRef()));
			contentPage.GetBackButton().addActionListener(e -> ChangeActivePanel(mainPage.GetRef(), scroll));
		}
	}
	
	void ChangeActivePanel(JPanel current) {
		scroll.setVisible(false);
		current.setVisible(true);
		MainApp.currentPage = current;
	}
	
	void ChangeActivePanel(JPanel current, JScrollPane currentScroll) {
		MainApp.currentPage.setVisible(false);
		current.setVisible(true);
		currentScroll.setVisible(true);
		MainApp.currentPage = current;
	}
	
	private static ArrayList<Game> readGameData(String filename) {
		ArrayList<Game> games = new ArrayList<>();
		int i = 0;
		
		while(i < 7) {
		try {
			Scanner readGames = new Scanner(new File(filename));
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
		i++;
		}
		
		return games;
	}
	
	
}
