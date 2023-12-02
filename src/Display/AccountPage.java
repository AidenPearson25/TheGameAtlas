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
import java.util.Map.Entry;

import Data.Account;

public class AccountPage extends Page {

    String accountName;

    // These are the buttons for the page
    JButton backButton;
    JButton login;
    JButton create;
    JButton logout;

    // These is the text on the page
    JLabel userLabel;
    JLabel passwordLabel;

    // These are the text boxes in the page
    public JTextField userText;
    public JTextField passwordText;

    public Map<String, Account> userDatabase = new HashMap<>(); // In-memory
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
        gbc.gridx = 2;
        panelWithLayout.add(userText, gbc);

        // Password Label
        passwordLabel = new JLabel("Password");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelWithLayout.add(passwordLabel, gbc);

        // Password Text
        passwordText = new JTextField(20);
        gbc.gridx = 2;
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
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets.top = 20;
        panelWithLayout.add(login, gbc);

        /*
         * // This calls the checkLogin() when the button is clicked.
         * login.addActionListener(new ActionListener() {
         * 
         * @Override public void actionPerformed(ActionEvent e) { // Call the
         * checkLogin method when the login button is clicked checkLogin(); }
         * });
         */

        // Create Account Button
        create = new JButton("Create Account");
        gbc.gridx = 3;
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
                if (parts.length == 3) {
                    Account acc = new Account(line);
                    userDatabase.put(parts[0], acc);
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
            for (Entry<String, Account> entry : userDatabase.entrySet()) {
                writer.println(entry.getValue().toString());
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
    public Account checkLogin() {
        // Capture the entered username and password
        String enteredUsername = userText.getText();
        String enteredPassword = passwordText.getText();
        String storedPassword = "";

        // Check if the entered username exists and the password matches
        if (userDatabase.containsKey(enteredUsername)) {
        	storedPassword = userDatabase.get(enteredUsername).getPassword();
        }
        
        Account acc;

        if (storedPassword != "" && storedPassword.equals(enteredPassword)) {
            // Successful login
            JOptionPane.showMessageDialog(null, "Login successful!");
            acc = userDatabase.get(enteredUsername);
        } else {
            // Failed login
            JOptionPane.showMessageDialog(null,
                    "Login failed. Please check your credentials.");
            acc = null;
        }

        userText.setText(""); // Clear the username text field
        passwordText.setText(""); // Clear the password text field

        return acc;
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

        if (userDatabase.containsKey(enteredUsername)
                || enteredUsername.trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Username is already in use. Please choose another option.");
            return;
        }

        Account acc = new Account(enteredUsername + ":" + enteredPassword + ":" + "0");
        userDatabase.put(enteredUsername, acc);
        JOptionPane.showMessageDialog(null,
                "Account created successfully. Please Login");

        userText.setText(""); // Clear the username text field
        passwordText.setText(""); // Clear the password text field

        saveUserData();

    }

    /**
     * This method logs out the user by resetting the account name.
     */
    public void logout() {
        accountName = null; // Reset the account name
        JOptionPane.showMessageDialog(null, "Logout successful!");
        // You may need to update other UI components or take additional actions
        // here
        // For example, you might want to hide certain buttons or panels when
        // logged out
        userText.setText(""); // Clear the username text field
        passwordText.setText(""); // Clear the password text field
    }

    // Gets a reference to the back button for action listener
    public JButton GetBackButton() {
        return backButton;
    }

    // Gets a reference to the accountName String for action listener
    public String getUsername() {
        return accountName;
    }
}