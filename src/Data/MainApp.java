package Data;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.CardLayout;
import java.awt.EventQueue;

import Display.RequestFormPage;
import Display.RequestPage;
import Display.SearchPage;
import Display.SortFilter;
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
    Account activeUser;

    ArrayList<Thumbnail> thumbsRaw = new ArrayList<>();
    ArrayList<Thumbnail> thumbsFiltered = new ArrayList<>();

    /**
     * Main, sets up and runs program.
     * 
     * @param args
     */
    public static void main(String[] args) {
        FlatDarkLaf.setup();
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
        contentPane.setLayout(new CardLayout(0, 0));
        setContentPane(contentPane);

        // Read game data from text doc
        ArrayList<Game> games = readGameData("GameDatabase.txt");

        // Setup for mainPage JPanel
        SearchPage mainPage = new SearchPage("searchPage", games);
        currentPage = mainPage.DisplayPage(contentPane); // Display page adds it
                                                         // to content pane
        contentPane.add(mainPage.GetRef());

        // Add an "Account button
        AccountPage accountPage = new AccountPage("accountPage");
        accountButton = new JButton("Account"); // Creates a button that leads
                                                // to the Account page
        accountButton.addActionListener(
                e -> ChangeActivePanel(accountPage.GetRef()));
        mainPage.getHeader().add(accountButton); // Make sure to add to mainPage
                                                 // instead of contentPane
        accountPage.DisplayPage(contentPane); // Displays the accountPage
        accountPage.initialize(); // Formats the accountPage
        accountPage.GetBackButton().addActionListener(
                e -> ChangeActivePanel(mainPage.GetRef())); // Makes the
                                                            // back
                                                            // button
                                                            // lead back
                                                            // to the
                                                            // main
                                                            // page.
        accountPage.GetLoginButton()
                .addActionListener(e -> SetActiveUser(accountPage, mainPage));

        // Make search bar and filters
        Filters searchFilter = new Filters(mainPage.getHeader());
        SortFilter sortFilter = new SortFilter(mainPage.getHeader());
        SearchBar searchBar = new SearchBar(mainPage.getHeader());

        // Create thumbnails, content pages, and button actions
        for (Game g : games) {
            // Create thumbnail
            Thumbnail t = mainPage.addThumb(g);
            thumbsRaw.add(t);

            // Create content pages
            ContentPage contentPage = new ContentPage(g);
            contentPage.DisplayPage(contentPane);

            // Action Listeners will run when buttons are pushed
            t.GetButton().addActionListener(
                    e -> EnableContentPage(contentPage));
            contentPage.GetBackButton().addActionListener(
                    e -> ChangeActivePanel(mainPage.GetRef()));
        }

        mainPage.updateScroll(thumbsRaw);

        // Add filters data and search button
        searchFilter.AddFilterData(thumbsRaw);
        searchBar.getButton().addActionListener(
                e -> ApplySearch(searchFilter, sortFilter, searchBar,
                        mainPage));

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
        activeUser = ap.checkLogin();

        if (activeUser != null) {

            accountButton.setText("Logout");

            accountButton.addActionListener(e -> logout(ap, mainPage));

            requestFormPage = new RequestFormPage("requestFormPage");
            

            requestFormPage.SetUsername(activeUser.getName());

            formButton = new JButton("Request Form");
            formButton.addActionListener(
                    e -> ChangeActivePanel(requestFormPage.GetRef()));


            mainPage.getHeader().add(formButton);


            requestFormPage.DisplayPage(contentPane);
            requestFormPage.displayInput();
            requestFormPage.GetBackButton().addActionListener(
                    e -> ChangeActivePanel(mainPage.GetRef()));

            if (activeUser.checkAccess(2)) { // User is admin
                requestPage = new RequestPage("requestPage");

                reqPageButton = new JButton("Request Page");
                reqPageButton.addActionListener(
                        e -> ChangeActivePanel(requestPage.GetRef()));

                mainPage.getHeader().add(reqPageButton);
                
                requestPage.DisplayPage(contentPane);
                requestPage.getBackButton().addActionListener(
                        e -> ChangeActivePanel(mainPage.GetRef()));
            }
        }
    }

    /**
     * This method allows the user to logout of the program and loses all the
     * actions of a logged in user.
     * 
     * @param ap       The accountPage that will have the login info needed
     * @param mainPage The mainPage that allows the requestPage to display
     */
    void logout(AccountPage ap, SearchPage mainPage) {
        ap.logout();

        accountButton.setText("Account");
        accountButton
                .removeActionListener(accountButton.getActionListeners()[0]); // Remove
                                                                              // the
                                                                              // logout
                                                                              // action
                                                                              // listener

        // Remove requestPage and requestFormPage buttons
        mainPage.getHeader().remove(formButton);
        if (reqPageButton != null) { // User might not have req page button
            mainPage.getHeader().remove(reqPageButton);
        }

        // Optionally, you may want to hide or remove other UI elements related
        // to the logged-in state
        // ...

        activeUser = null; // Reset activeUser
    }

    void EnableContentPage(ContentPage self) {
        self.showAddComment(activeUser);
        self.showDeleteButton(activeUser);
        ChangeActivePanel(self.GetRef());
    }

    /**
     * Change the active panel
     * 
     * @param current
     */
    void ChangeActivePanel(JPanel current) {
        currentPage.setVisible(false);
        current.setVisible(true);
        MainApp.currentPage = current;
    }

    /**
     * Apply the search filters and search bar.
     * 
     * @param filter    Filter object
     * @param searchBar Search Bar object
     * @param mainPage  Main page ref
     */
    void ApplySearch(Filters filter, SortFilter sortFilter, SearchBar searchBar,
            SearchPage mainPage) {
        // Run both filters and search, returning a full list if neither apply
        thumbsFiltered = filter.ApplyFilters(thumbsRaw);
        thumbsFiltered = searchBar.sortThumbs(thumbsFiltered);
        thumbsFiltered = sortFilter.applyFilters(thumbsFiltered);

        for (Thumbnail t : thumbsRaw) {
            mainPage.getContent().remove(t.GetButton());
        }

        for (Thumbnail t : thumbsFiltered) {
            mainPage.getContent().add(t.GetButton());
        }

        // Updates to display thumbnails
        mainPage.updateScroll(thumbsFiltered);
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
        int repeat = 4;
        while (repeat < 5) {
            // Scanner read
            try {
                Scanner readGames = new Scanner(new File(gameFilename));
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

            repeat++;
        }
        return games;
    }

}
