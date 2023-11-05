package Data;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
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
import Display.RequestFormPage;
import Display.RequestPage;
import Display.SearchPage;
import Display.AccountPage;
import Display.ContentPage;
import Display.Filters;
import Display.SearchBar;

public class MainApp extends JFrame {

    private static final long serialVersionUID = 1L;
    // Main pane of the frame
    private JPanel contentPane;

    // The current jpanel that's active
    static JPanel currentPage;

    // The current jscrollpane that's active (Might have to edit if more things
    // require scrolls later)
    JScrollPane scroll;

    // The JButton needed for the account page
    private JButton accountButton;

    // The Page for the request form
    public RequestFormPage requestFormPage;
    
    // The Page for request review
    public RequestPage requestPage;

    // The JButton needed for the request form page
    private JButton formButton;
    
    // The JButton needed for the request page
    private JButton reqPageButton;

    // Current user
    String activeUser = "";

    ArrayList<Thumbnail> thumbsRaw = new ArrayList<>();
    ArrayList<Thumbnail> thumbsFiltered = new ArrayList<>();

    /**
     * Main, sets up and runs program.
     * 
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
        // Initial Setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1051, 531);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new CardLayout(0, 0));
        
        // Setup for mainPage JPanel
        SearchPage mainPage = new SearchPage("searchPage");
        currentPage = mainPage.DisplayPage(contentPane); // Display page adds it
                                                         // to content pane

        // Read game data from text doc
        ArrayList<Game> games = readGameData("GameDatabase.txt");

        // Setup scroll
        scroll = mainPage.ScrollSetup(games);
        contentPane.add(scroll);

        // Add an "Account button
        AccountPage accountPage = new AccountPage("accountPage");
        accountButton = new JButton("Account"); // Creates a button that leads
                                                // to the Account page
        accountButton.addActionListener(
                e -> ChangeActivePanel(accountPage.GetRef()));
        mainPage.GetRef().add(accountButton); // Make sure to add to mainPage
                                              // instead of contentPane
        accountPage.DisplayPage(contentPane); // Displays the accountPage
        accountPage.initialize(); // Formats the accountPage
        accountPage.GetBackButton().addActionListener(
                e -> ChangeActivePanel(mainPage.GetRef(), scroll)); // Makes the
                                                                    // back
                                                                    // button
                                                                    // lead back
                                                                    // to the
                                                                    // main
                                                                    // page.
        accountPage.GetLoginButton()
                .addActionListener(e -> SetActiveUser(accountPage, mainPage));

        // Make search bar and filters
        Filters searchFilter = new Filters(mainPage.GetRef());
        SearchBar searchBar = new SearchBar(mainPage.GetRef());

        // Create thumbnails, content pages, and button actions
        for (Game g : games) {
            // Create thumbnail
        		Thumbnail t = mainPage.addThumb(g);
        		thumbsRaw.add(t);

            // Create content pages
            ContentPage contentPage = new ContentPage(g, t);
            contentPage.DisplayPage(contentPane);

            // Action Listeners will run when buttons are pushed
            t.GetButton().addActionListener(
                    e -> EnableContentPage(contentPage));
            contentPage.GetBackButton().addActionListener(
                    e -> ChangeActivePanel(mainPage.GetRef(), scroll));
        }

        // Add filters data and search button
        searchFilter.AddFilterData(thumbsRaw);
        searchBar.getButton().addActionListener(
        				e -> ApplySearch(searchFilter, searchBar, mainPage));

    }

    /**
     * This method will change the account button to display the user's
     * username. It will also allow the request form page when the user is
     * logged in. The request page will be at the bottom of the page. Need to
     * fix later.
     * 
     * @param ap       The accountPage that will have the login info needed
     * @param mainPage The mainPage that allows the requestPage to display
     */
    void SetActiveUser(AccountPage ap, SearchPage mainPage) {
        activeUser = ap.checkLogin(); // Checks to see if the user is logged in
        accountButton.setText(activeUser); // Changes the account button's name to the active user

        requestFormPage = new RequestFormPage("requestFormPage"); // Creates a new requestFormPage object
        requestPage = new RequestPage("requestPage");
        
        requestFormPage.SetUsername(activeUser); // Sends username of active user to the form

        // Make a button for request Form
        formButton = new JButton("Request Form");
        formButton.addActionListener(
                e -> ChangeActivePanel(requestFormPage.GetRef()));
        

        // Make a button for request Page
        reqPageButton = new JButton("Request Page");
        reqPageButton.addActionListener(
                e -> ChangeActivePanel(requestPage.GetRef()));
        
        mainPage.GetRef().add(formButton);
        mainPage.GetRef().add(reqPageButton);
        
        requestPage.DisplayPage(contentPane);
        requestPage.getBackButton().addActionListener(
                e -> ChangeActivePanel(mainPage.GetRef(), scroll));
        
        requestFormPage.DisplayPage(contentPane);
        requestFormPage.displayInput();
        requestFormPage.GetBackButton().addActionListener(
                e -> ChangeActivePanel(mainPage.GetRef(), scroll));
    }
    
    void EnableContentPage(ContentPage self) {
    	self.showAddComment(activeUser);
    	ChangeActivePanel(self.GetRef());
    }

    /**
     * Change the active panel
     * 
     * @param current
     */
    void ChangeActivePanel(JPanel current) {
        scroll.setVisible(false);
        current.setVisible(true);
        MainApp.currentPage = current;
    }

    /**
     * Change the active panel with scroll check (Might have to edit later)
     * 
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
     * Apply the search filters and search bar.
     * @param filter Filter object
     * @param searchBar Search Bar object
     * @param mainPage Main page ref
     */
    void ApplySearch(Filters filter, SearchBar searchBar, Page mainPage) {
    	//Run both filters and search, returning a full list if neither apply
    	thumbsFiltered = filter.ApplyFilters(thumbsRaw);
    	thumbsFiltered = searchBar.sortThumbs(thumbsFiltered);
    	
    	for (Thumbnail t : thumbsRaw) {
    		mainPage.GetRef().remove(t.GetButton());
    	}
    	
    	for (Thumbnail t : thumbsFiltered) {
        mainPage.GetRef().add(t.GetButton());
    	}

    	// Updates to display thumbnails
    	contentPane.updateUI();
    }
    

    /**
     * Reads the game data from a text doc, creates game objects, returns array.
     * 
     * @param filename
     * @return
     */
    public static ArrayList<Game> readGameData(String gameFilename) {
        ArrayList<Game> games = new ArrayList<>();

        // Scanner read
        try {
            Scanner readGames = new Scanner(new File(gameFilename));
            while (true) { // Trust me on this
                Game current = new Game(readGames.nextLine());
                current.SetDescription(readGames.nextLine());
                current.SetIconRef(readGames.nextLine());
                current.SetGenre(readGames.nextLine());
                current.SetPrice(new double[] { 0.0 }); // Add later
                readGames.nextLine(); // Delete later
                String tempBool = readGames.nextLine();
                Scanner check = new Scanner(tempBool);
                for (int i = 0; i < 3; i++) {
                    current.SetPlatforms(check.nextBoolean(), i);
                }

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

        return games;
    }

}
