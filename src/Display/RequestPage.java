package Display;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Data.Game;

public class RequestPage extends Page {
    private ArrayList<Request> requests;
    private User currentUser;
    private Map<String, Request> requestMap;
    
    // Display
    private JList<String> requestList; // List of requests
    
    private JPanel listPanel; // Hold separate to interact with afterwards
    private JPanel formPanel; // Hold separate to interact with afterwards
    
    // Page to view all pending requests and allow users to approve/deny requests
    public RequestPage(String name) {
        super(name);
        
        // Check user info before continuing
        if (checkAdminStatus()) {
            // This is admin. Approved.
            // Initialize page
            requests = getAllRequests();
        }
    }
    
    public void display(JPanel contentPane) {
        // Setup initial layout
        JPanel panel = new JPanel();
        contentPane.add(panel);
        panel.setLayout(new BorderLayout(0, 0));
        
        JPanel navBar = new JPanel();
        panel.add(navBar, BorderLayout.NORTH);
        
        JButton backBtn = new JButton("Back");
        navBar.add(backBtn);
        
        listPanel = new JPanel();
        panel.add(listPanel, BorderLayout.CENTER);
        
        // Setup left panel
        generateList();
        
        // Setup right panel
        JPanel rightPanel = new JPanel();
        contentPane.add(rightPanel);
        rightPanel.setLayout(new BorderLayout(0, 0));
        
        formPanel = new JPanel();
        rightPanel.add(formPanel, BorderLayout.CENTER);
        formPanel.setLayout(new BorderLayout(0, 0));
        
    }
    
    public Map<String, Request> getAllRequest() {
        return null;
    }
    
    // Generate list of request.
    // Called everytime a request is processed
    private void generateList() {
        // Setup the list of all requests
        for (int i = 0; i < requests.size(); i++) {
            requestMap.put(requests.get(i).getName(), requests.get(i)); // Generate a map to get the info later
        }
        Set<String> names = requestMap.keySet();
        String[] requestNames = names.toArray(new String[0]); // Magic.
        
        // Create list
        requestList = new JList<>(requestNames);
        listPanel.add(requestList);
        
        // Add a bottom panel for widgets
        JPanel bottomPanel = new JPanel();
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
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
                createForm(selected);
            } 
          } );
    }
    
    // Create the form when a request is approved
    private void createForm(String gameName) {
        // Add application form
        JPanel addGameP = new JPanel();
        formPanel.add(addGameP, BorderLayout.SOUTH);
        
        // Add form information
        JPanel formInfoP = new JPanel();
        formPanel.add(formInfoP, BorderLayout.CENTER);
        formInfoP.setLayout(new GridLayout(3, 1, 0, 0));

        JPanel gameInfoP = new JPanel();
        formPanel.add(gameInfoP, BorderLayout.NORTH);
        
        JLabel gameLabel = new JLabel(gameName);
        gameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        gameInfoP.add(gameLabel);
        
        JTextField linkField = addTextInput("Game Link", formInfoP);
        JTextField priceField = addTextInput("Price", formInfoP);
        JTextField descriptionField = addTextInput("Game Description", formInfoP);
        
        // Add widgets to reset or add new things
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
                 Game game = new Game(gameName);
                 game.SetDescription(descriptionField.getText());
                 double price = Double.parseDouble(priceField.getText()); /// #REMINDER: Prone to error input
                 game.SetPrice(price);
                 addRequest(game);
                 resetFormField();
              } 
            } );
    }
    
    // Remove the form field on the right. Will work whether form exists or not.
    private void resetFormField() {
        formPanel.removeAll();
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
            // #TODO: Implement the method
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
    
    // Check if user has admin access
    public boolean checkAdminStatus() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getAdminStatus();
    }

    // Get all requests from the request file
    // #TODO: Implement method
    private ArrayList<Request> getAllRequests() {
        return null;
    }
    
    // Approve request, add game to list
    // #TODO: Implement method 
    private boolean addRequest(Game game) {
        // Add the game to list in Main App
        if (game != null) {
            return true;
        } else {
            return false;
        }
        
    }
    
    // Reset the request display
    // Remove the form, reset the list of request
    private void resetDisplay() {
        formPanel.removeAll();
        listPanel.removeAll();
        generateList();
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
        accId = "001";
        gameName = "sus";
        gameLink = "https://www.google.com";
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

class User {
    // Pseudo user
    String accId;
    boolean admin;
    
    boolean getAdminStatus() {
        return admin;
    }
}
