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
import java.util.Map;
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
import javax.swing.border.EmptyBorder;

import Data.Account;
import Data.Game;

public class RequestPage extends Page {
    private ArrayList<Request> requests;
    private Account currentUser;
    private HashMap<String, Request> requestMap;
    
    // Display
    private JList<String> requestList; // List of requests
    
    private JPanel pane; // Try
    private JPanel listPanel; // Hold separate to interact with afterwards
    private JPanel formPanel; // Hold separate to interact with afterwards
    
    private JButton backBtn;
    private static String DATABASE_FILE = "GameDatabase.txt";
    private static String REQUEST_FILE = "RequestDatabase.txt";
    
    // Page to view all pending requests and allow users to approve/deny requests
    public RequestPage(String name) {
        super(name);
        requests = getAllRequests();
        display();
    }
    
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
    
    // Generate list of request.
    // Called everytime a request is processed
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
    
    // Create the form when a request is approved
    private void createForm(String gameName) {
        // Add application form
        JPanel addGameP = new JPanel();
        formPanel.add(addGameP, BorderLayout.SOUTH);
        
        // Add form information
        JPanel formInfoP = new JPanel();
        formPanel.add(formInfoP, BorderLayout.CENTER);
        formInfoP.setLayout(new GridLayout(6, 1, 0, 0)); // Change first num if adding new field

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
                 double price = Double.parseDouble(priceField.getText()); /// #REMINDER: Prone to error input
                 addRequest(nameField.getText(), descriptionField.getText(), price, tagField.getText(), platformField.getText());
                 resetFormField();
              } 
            } );
        
        resetPanel(panel);
    }
    
    // Remove the form field on the right. Will work whether form exists or not.
    private void resetFormField() {
        formPanel.removeAll();
        resetPanel(panel);
    }
    
    // Add a label and field for text-based input on the form
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
    
    // Remove request for game name
    // #TODO: Implement the method
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
    
    // Check if user has mod access or better
    public boolean checkAdminStatus() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.checkAccess(1);
    }

    // Get all requests from the request file
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
    
    // Approve request, add game to list
    private boolean addRequest(String name, String description, double price, String tag, String platform) {
        // Adding the game into database file
        try {
            FileWriter database = new FileWriter(DATABASE_FILE, true);
            database.write("\n\n");
            database.write(name + "\n");
            database.write(description + "\n");
            database.write("hollowknight.jpg" + "\n"); // #TODO: Replace with an image
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
            return true;
            
        } catch (IOException e) {
            System.out.println("File not found");
        }
        return false;
        
    }
    
    // Reset the request display
    // Remove the form, reset the list of request
    private void resetDisplay() {
        formPanel.removeAll();
        listPanel.removeAll();
        requestList.clearSelection();
        generateList();
    }
    
    public JButton getBackButton() {
        return backBtn;
    }
    

    
    public void resetPanel(JPanel current) {
        current.revalidate();
        current.repaint();
    }
}

class Request {
    // Pseudo request
    String accId;
    String gameName;
    String gameLink;
    
    public Request(String info) {
        // Make request from info string
        // Pseudocode.
        String[] spl = info.split(":");
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