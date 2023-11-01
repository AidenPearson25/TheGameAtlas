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
import Display.SearchPage;
import Display.AccountPage;
import Display.ContentPage;
import Display.Filters;

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

    // The JButton needed for the request form page
    private JButton formButton;

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

        // Make search filters
        Filters searchFilter = new Filters(mainPage.GetRef());

        // Create thumbnails, content pages, and button actions
        for (Game g : games) {
            // Create thumbnail
        		Thumbnail t = mainPage.addThumb(g);
        		thumbsRaw.add(t);

            // Create content pages
            ContentPage contentPage = new ContentPage(g.GetName());
            contentPage.DisplayPage(contentPane);
            contentPage.AddData(g, t);

            // Action Listeners will run when buttons are pushed
            t.GetButton().addActionListener(
                    e -> ChangeActivePanel(contentPage.GetRef()));
            contentPage.GetBackButton().addActionListener(
                    e -> ChangeActivePanel(mainPage.GetRef(), scroll));
        }

        // Add filters data
        searchFilter.AddFilterData(thumbsRaw);
        searchFilter.GetButton().addActionListener(
                e -> ApplyFilters(searchFilter, mainPage));

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

        requestFormPage.SetUsername(activeUser); // Sends username of active user to the form

        formButton = new JButton("Request Form");
        formButton.addActionListener(
                e -> ChangeActivePanel(requestFormPage.GetRef()));
        mainPage.GetRef().add(formButton);
        requestFormPage.DisplayPage(contentPane);
        requestFormPage.displayInput();
        requestFormPage.GetBackButton().addActionListener(
                e -> ChangeActivePanel(mainPage.GetRef(), scroll));
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
     * Apply filters by adding different thumbnail array to page
     * 
     * @param filter
     * @param mainPage
     */
    void ApplyFilters(Filters filter, Page mainPage) {
        // Calls function in Filters class to apply sort
        thumbsFiltered = filter.ApplyFilters();

        // Clears pane of thumbnails
        for (Thumbnail t : thumbsRaw) {
            mainPage.GetRef().remove(t.GetButton());
        }

        // Checks to see if no filters are applied
        if (!thumbsFiltered.isEmpty()) {
            // Applies new array of filtered thumbnails
            for (Thumbnail t : thumbsFiltered) {
                mainPage.GetRef().add(t.GetButton());
            }
        } else {
            // Applies original array of thumbnails
            for (Thumbnail t : thumbsRaw) {
                mainPage.GetRef().add(t.GetButton());
            }
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
    private static ArrayList<Game> readGameData(String filename) {
        ArrayList<Game> games = new ArrayList<>();
        int i = 0; // Detlete later

        // Scanner read
        while (i < 7) { // Delete later, pads for scroll testing
            try {
                Scanner readGames = new Scanner(new File(filename));
                while (true) { // Trust me on this
                    Game current = new Game(readGames.nextLine());
                    current.SetDescription(readGames.nextLine());
                    current.SetIconRef(readGames.nextLine());
                    current.SetGenre(readGames.nextLine());
                    current.SetPrice(new double[] { 0.0 }); // Add later
                    readGames.nextLine(); // Delete later
                    String tempBool = readGames.nextLine();
                    Scanner check = new Scanner(tempBool);
                    for (int j = 0; j < 3; j++) {
                        current.SetPlatforms(check.nextBoolean(), j);
                    }

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
