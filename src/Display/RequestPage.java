package Display;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.FileDialog;
import java.awt.Dialog;
import java.awt.Window;

import java.nio.file.Path;
import java.nio.file.Files;

import Data.Account;

public class RequestPage extends Page {
    private ArrayList<Request> requests;
    private Account currentUser;
    private HashMap<String, Request> requestMap;
    
    // Display
    private JList<String> requestList; // List of requests
    
    private JPanel pane;
    private JPanel listPanel; // Hold separate to interact with afterwards
    private JPanel formPanel; // Hold separate to interact with afterwards
    
    private JButton backBtn;
    private static String DATABASE_FILE = "GameDatabase.txt";
    private static String REQUEST_FILE = "RequestDatabase.txt";
    
    private JFrame mainApp;
    private static Path ICON_PATH = new File("iconImages").toPath();

    private FileDialog fd;
    private File thumbnail;
    
    /**
     * Main Constructor.
     * @param name Name of the page
     */
    public RequestPage(String name, JFrame app) {
        super(name);
        requests = getAllRequests();
        mainApp = app;
        display();
    }
    
    /**
     * Display the request page.
     */
    public void display() {
        // Setup initial layout
        panel.setLayout(new GridLayout(0, 2, 0, 0));
        
        pane = new JPanel();
        pane.setLayout(new BorderLayout(0, 0));
        panel.add(pane);
       
        /*
        JPanel leftPanel = new JPanel();
        pane.add(leftPanel);*/
        
        JPanel navBar = new JPanel();
        pane.add(navBar, BorderLayout.NORTH);
        
        backBtn = new JButton("Back");
        navBar.add(backBtn);
        
        listPanel = new JPanel();
        pane.add(listPanel, BorderLayout.CENTER);
        
        // Setup left panel
        generateList();

        
        // Setup right panel
        JPanel rightPanel = new JPanel();
        panel.add(rightPanel);
        rightPanel.setLayout(new BorderLayout(0, 0));
        
        formPanel = new JPanel();
        rightPanel.add(formPanel, BorderLayout.CENTER);
        formPanel.setLayout(new BorderLayout(0, 0));

        
        // Add a bottom panel for widgets
        JPanel bottomPanel = new JPanel();
        pane.add(bottomPanel, BorderLayout.SOUTH);
        
        JButton rejectBtn = new JButton("Reject");
        bottomPanel.add(rejectBtn);
        rejectBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                // Reject request, delete the request
                String selected = requestList.getSelectedValue();
                removeRequest(selected);
              } 
            } );
        
        JButton aprvBtn = new JButton("Approve");
        bottomPanel.add(aprvBtn);
        aprvBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                // Approve request, proceed to form
                resetFormField(); // Safety.
                String selected = requestList.getSelectedValue();
                if (selected != null) {
                    createForm(selected);
                }
            } 
          } );
        
    }

    /**
     * Generate the list of requests.
     */
    private void generateList() {
        // Setup the list of all requests
        requestMap = new HashMap<String, Request>();
        for (int i = 0; i < requests.size(); i++) {
            requestMap.put(requests.get(i).getName(), requests.get(i)); // Generate a map to get the info later
        }
        Set<String> names = requestMap.keySet();
        String[] requestNames = names.toArray(new String[0]); // Magic.
        
        // Create list
        requestList = new JList<>(requestNames);
        requestList.setListData(requestNames);
        listPanel.add(requestList);
        
        resetPanel(panel);
    }

    /**
     * Create the form to accept request and add game.
     * @param gameName Name of the game
     */
    private void createForm(String gameName) {
        // Add application form
        JPanel addGameP = new JPanel();
        formPanel.add(addGameP, BorderLayout.SOUTH);
        
        // Add form information
        JPanel formInfoP = new JPanel();
        formPanel.add(formInfoP, BorderLayout.CENTER);
        formInfoP.setLayout(new GridLayout(7, 1, 0, 0)); // Change first num if adding new field

        JPanel gameInfoP = new JPanel();
        formPanel.add(gameInfoP, BorderLayout.NORTH);
        
        JLabel gameLabel = new JLabel(gameName);
        gameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        gameInfoP.add(gameLabel);
        
        JTextField nameField = addTextInput("Game Name", formInfoP);
        JTextField linkField = addTextInput("Game Link", formInfoP);
        JTextField priceField = addTextInput("Price", formInfoP);
        JTextField descriptionField = addTextInput("Game Description", formInfoP);
        JTextField tagField = addTextInput("Tags", formInfoP);
        JTextField platformField = addTextInput("Platform", formInfoP);
        
        JSplitPane fieldPane = new JSplitPane();
        fieldPane.setResizeWeight(0.3);
        formInfoP.add(fieldPane);
        
        JLabel fieldLabel = new JLabel("Thumbnail File:");
        fieldLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fieldLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        fieldPane.setLeftComponent(fieldLabel);
        
        JButton fileBtn = new JButton("Select File");
        //File thumbnail = null;
        fieldPane.setRightComponent(fileBtn);
        fileBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                // Thank you Salvatorelab https://stackoverflow.com/questions/7211107/how-to-use-filedialog
                fd = new FileDialog(mainApp, "Choose a file", FileDialog.LOAD);
                fd.setDirectory("C:\\");
                fd.setFile("*.jpg");
                fd.setVisible(true);
                thumbnail = fd.getFiles()[0];
                fileBtn.setText(thumbnail.getName());
            } 
          } );

        // Add widgets to reset or add new things
        JButton cancelBtn = new JButton("Cancel");
        addGameP.add(cancelBtn);
        cancelBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                resetFormField();
            } 
          } );
        
        JButton resetBtn = new JButton("Reset");
        addGameP.add(resetBtn);
        resetBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                // Reset the form
                linkField.setText("");
                priceField.setText("");
                descriptionField.setText("");
            } 
          } );
        
        JButton addBtn = new JButton("Add");
        addGameP.add(addBtn);
        addBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                if (thumbnail != null) {
                    System.out.println(thumbnail.getPath());
                    try {
                        double price = Double.parseDouble(priceField.getText());
                        addRequest(nameField.getText(), descriptionField.getText(), price, 
                                tagField.getText(), platformField.getText());
                        removeRequest(gameName);
                        resetFormField();
                    } catch (Exception exception) {
                        // Exceptions
                        // Do nothing
                    }
                }
            } 
        } );
        
        resetPanel(panel);
    }
    
    /**
     * Reset the fields in the form.
     */
    private void resetFormField() {
        formPanel.removeAll();
        resetPanel(panel);
    }
    
    /**
     * Add text field input.
     * @param label Label of the field
     * @param formInfoP Panel to attach the text field to.
     * @return Text field to get input.
     */
    private JTextField addTextInput(String label, JPanel formInfoP) {
        JSplitPane fieldPane = new JSplitPane();
        fieldPane.setResizeWeight(0.3);
        formInfoP.add(fieldPane);
        
        JLabel fieldLabel = new JLabel(label + ":");
        fieldLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fieldLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        fieldPane.setLeftComponent(fieldLabel);
        
        JTextField fieldInput = new JTextField();
        fieldInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
        fieldPane.setRightComponent(fieldInput);
        fieldInput.setColumns(10);
        return fieldInput;
    }
    
    /**
     * Remove the request from the list. Method unimplemented for now.
     * @param name Name of the request
     */
    private void removeRequest(String name) {
        Request request = requestMap.get(name);
        if (request != null) {
            requests.remove(request);
        } else {
            System.out.println("Error. Request not found");
        }
        
        // Run through request list and remove any other requests of the same game
        for (Request req : requests) {
            if (req.getName().equals(name)) {
                requests.remove(req);
            }
        }
        resetDisplay();
    }
    
    /**
     * Check if the user is admin or not.
     * @return Admin status
     */
    public boolean checkAdminStatus() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.checkAccess(1);
    }

    /**
     * Get all the available requests from the database.
     * @return List of requests
     */
    private ArrayList<Request> getAllRequests() {
        ArrayList<Request> allRequests = new ArrayList<Request>();
        try {
            Scanner requestFile = new Scanner(new File(REQUEST_FILE));
            while (requestFile.hasNextLine()) {
                allRequests.add(new Request(requestFile.nextLine()));
            }
            requestFile.close();
            return allRequests;
        } catch (FileNotFoundException e) {
            // No.
        }
        return null;
    }
    
    /**
     * Approve the request and add the game to the database.
     * @param name Name of the game
     * @param description Description of the game
     * @param price Price of the game
     * @param tag Tags and genres of the game
     * @param platform Platform the game is available. Format: "ttt"
     * @return True if the game is successfully added
     */
    private boolean addRequest(String name, String description, double price, String tag, String platform) {
        // Adding the game into database file
        try {
            FileWriter database = new FileWriter(DATABASE_FILE, true);
            database.write("\n\n");
            database.write(name + "\n");
            database.write(description + "\n");
            
            // Copy file to icon folder
            Files.copy(thumbnail.toPath(), ICON_PATH.resolve(thumbnail.getName()));
            database.write(thumbnail.getName() + "\n");
            
            database.write(tag + "\n");
            
            // Set price by platform
            String prices = "";
            String plats = "";
            for (int i = 0; i < 3; i++) {
                if (platform.toLowerCase().charAt(i) == 't') {
                    prices += price + " ";
                    plats += "true ";
                } else {
                    prices += "-1 ";
                    plats += "false ";
                }
            }
            database.write(prices + "\n");
            database.write(plats);
            database.close();
            thumbnail = null;
            return true;
            
        } catch (IOException e) {
            System.out.println("File not found");
        }
        return false;
        
    }
    
    /**
     * Reset the page display.
     */
    private void resetDisplay() {
        formPanel.removeAll();
        listPanel.removeAll();
        requestList.clearSelection();
        generateList();
    }
    
    /**
     * Get the back button.
     * @return back button
     */
    public JButton getBackButton() {
        return backBtn;
    }
    
    /**
     * Reset the panel to update the list.
     * @param current Reset the panel
     */
    public void resetPanel(JPanel current) {
        current.revalidate();
        current.repaint();
    }
}

class Request {
    // Request object to hold information
    String accId;
    String gameName;
    String gameLink;
    
    public Request(String info) {
        // Make request from info string
        // Pseudocode.
        String[] spl = info.split("::");
        if (spl.length == 3) {
            gameName = spl[0];
            gameLink = spl[1];
            accId = spl[2];
        } else {
            accId = "0";
            gameName = "Default Game";
            gameLink = "null";
        }
    }
    
    public String getID() {
        return accId.toString();
    }
    
    public String getName() {
        return gameName.toString();
    }
    
    public String getLink() {
        return gameLink.toString();
    }
}