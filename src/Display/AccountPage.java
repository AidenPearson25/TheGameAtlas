package Display;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AccountPage extends Page {
    
    String accountName;

    // These are the buttons for the page
    JButton backButton;
    JButton login;
    JButton create;

    // These is the text on the page
    JLabel userLabel;
    JLabel passwordLabel;

    // These are the text boxes in the page
    protected JTextField userText;
    protected JTextField passwordText;

    protected Map<String, String> userDatabase = new HashMap<>(); // In-memory
                                                                // user storage

    public AccountPage(String name) {
        super(name);
        loadUserData(); // This method loads all usernames and passwords made
                        // beforehand.
    }

    /**
     * This method basically sets up the page. The GridBagLayout is a way of
     * formatting all the elements in the page.
     */
    public void initialize() {
        // Create a panel with GridBagLayout
        JPanel panelWithLayout = new JPanel(new GridBagLayout());

        // Create GridBagConstraints for component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets.left = 10;
        gbc.insets.top = 10;

        // User Label
        userLabel = new JLabel("Username");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelWithLayout.add(userLabel, gbc);

        // User Text
        userText = new JTextField(20);
        gbc.gridx = 1;
        panelWithLayout.add(userText, gbc);

        // Password Label
        passwordLabel = new JLabel("Password");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelWithLayout.add(passwordLabel, gbc);

        // Password Text
        passwordText = new JTextField(20);
        gbc.gridx = 1;
        panelWithLayout.add(passwordText, gbc);

        // Back Button
        backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets.top = 20;
        panelWithLayout.add(backButton, gbc);

        // Login Button
        login = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets.top = 20;
        panelWithLayout.add(login, gbc);

        /*
        // This calls the checkLogin() when the button is clicked.
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the checkLogin method when the login button is clicked
                checkLogin();
            }
        });
        */

        // Create Account Button
        create = new JButton("Create Account");
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets.top = 20;
        panelWithLayout.add(create, gbc);

        // This calls the createAccount() when the create account button is
        // clicked.
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the checkLogin method when the login button is clicked
                createAccount();
            }
        });

        // Add the panel with GridBagLayout to the main panel
        panel.add(panelWithLayout);
    }

    /**
     * This method loads all the previous user data from a text file called,
     * "user_data.txt"
     */
    public void loadUserData() {
        // Read the user data from a text file
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("user_data.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    userDatabase.put(parts[0], parts[1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            // File doesn't exist or cannot be read, so continue with an empty
            // userDatabase.
        }
    }

    /**
     * This method saves the username and password made in createAccount()
     */
    public void saveUserData() {
        try {
            PrintWriter writer = new PrintWriter(
                    new FileWriter("user_data.txt"));
            for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method checks the username and password typed in the text boxes with
     * the textfile to see if the user has an account to login with.
     */
    public String checkLogin() {
        // Capture the entered username and password
        String enteredUsername = userText.getText();
        String enteredPassword = passwordText.getText();

        // Check if the entered username exists and the password matches
        String storedPassword = userDatabase.get(enteredUsername);

        if (storedPassword != null && storedPassword.equals(enteredPassword)) {
            // Successful login
            JOptionPane.showMessageDialog(null, "Login successful!");
            accountName = enteredUsername;
        } else {
            // Failed login
            JOptionPane.showMessageDialog(null,
                    "Login failed. Please check your credentials.");
        }
        
        return accountName;
    }
    
    public JButton GetLoginButton() {
        return login;
    }

    /**
     * This method takes the username and password typed in the text boxes and
     * checks if the same username is in the textfile. If not, then a new
     * account is created and saved.
     */
    public void createAccount() {
        // Capture the entered username and password
        String enteredUsername = userText.getText();
        String enteredPassword = passwordText.getText();

        if (userDatabase.containsKey(enteredUsername) || enteredUsername.trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Username is already in use. Please choose another option.");
            return;
        }

        userDatabase.put(enteredUsername, enteredPassword);
        JOptionPane.showMessageDialog(null,
                "Account created successfully. Please Login");

        saveUserData();

    }

    // Gets a reference to the back button for action listener
    public JButton GetBackButton() {
        return backButton;
    }
    
    public String getUsername() {
        return accountName;
    }
}