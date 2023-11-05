package Display;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<String, Request> requestMap;
    
    // Display
    private JList<String> requestList; // List of requests
    
    private JPanel pane; // Try
    private JPanel listPanel; // Hold separate to interact with afterwards
    private JPanel formPanel; // Hold separate to interact with afterwards
    
    private JButton backBtn;
    
    // Page to view all pending requests and allow users to approve/deny requests
    public RequestPage(String name) {
        super(name);
        
        // Check user info before continuing
        if (checkAdminStatus()) {
            // This is admin. Approved.
            // Initialize page
        }
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
                 Game game = new Game(gameName);
                 game.SetDescription(descriptionField.getText());
                 double price = Double.parseDouble(priceField.getText()); /// #REMINDER: Prone to error input
                 game.SetPrice(price);
                 addRequest(game);
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
        ArrayList<Request> allRequests = new ArrayList<Request>();
        allRequests.add(new Request("This"));
        allRequests.add(new Request("is"));
        allRequests.add(new Request("a game"));
        return allRequests;
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
        String[] spl = info.split("::");
        if (spl.length == 2) {
            accId = spl[0];
            gameName = spl[1];
        } else {
            accId = "0";
            gameName = "Default Game";
        }
    }
    
    public String getID() {
        return accId.toString();
    }
    
    public String getName() {
        return gameName.toString();
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
