package Display;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class for the page where user's can request a game
 * to be added.
 * 
 * @author Josef Arnold
 *
 */
public class RequestFormPage extends Page {
    
    // Buttons for the page
    JButton submit;
    JButton backButton;
    
    // Text fields for the form
    public JTextField titleField;
    public JTextField linkField;
    
    // Username of active user
    public String username;
    
    /**
     * The constructor for the Request Form page.
     * 
     * @param name Name of the page.
     */
    public RequestFormPage(String name) {
        super(name);
    }
    
    /**
     * Displaying the input fields for the user.
     */
    public void displayInput() {
        
        JPanel layout = new JPanel(new GridBagLayout());
        
        // Labels for the text prompts next to the text fields
        JLabel titlePrompt;
        JLabel linkPrompt;
        
        // Create GridBagConstraints for component placement
        GridBagConstraints grid = new GridBagConstraints();
        grid.anchor = GridBagConstraints.WEST;
        grid.insets.left = 10;
        grid.insets.top = 10;
        
        backButton = new JButton("Back");
        grid.gridx = 0;
        grid.gridy = 0;
        grid.gridwidth = 2;
        grid.insets.bottom = 20;
        layout.add(backButton, grid);
        
        titlePrompt = new JLabel("Game Title: ");
        grid.gridx = 0;
        grid.gridy = 1;
        layout.add(titlePrompt, grid);
        
        titleField = new JTextField(30);
        grid.gridx = 2;
        layout.add(titleField, grid);
        
        linkPrompt = new JLabel("Link to Game: ");
        grid.gridx = 0;
        grid.gridy = 2;
        layout.add(linkPrompt, grid);
        
        linkField = new JTextField(60);
        grid.gridx = 2;
        layout.add(linkField, grid);
        
        submit = new JButton("Submit Form");
        grid.gridx = 0;
        grid.gridy = 3;
        grid.gridwidth = 2;
        grid.insets.top = 20;
        layout.add(submit, grid);
        
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubmitForm();
            }
        });
        
        panel.add(layout);
    }
    
    /**
     * Taking the information from the form and the username of the
     * user filling it out and writing it to a text file.
     */
    public void SubmitForm() {
        String title = titleField.getText();
        String link = linkField.getText();
        String username = this.username;
        
        try {
            PrintWriter write = new PrintWriter(
                    new File("RequestDatabase.txt"));
            write.println(title + ":" + link + ":" + username);
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the reference to the back button for the action listener.
     * 
     * @returns Reference to back button
     */
    public JButton GetBackButton() {
        return backButton;
    }
    
    /**
     * Retrieves the username of the person filling out the form.
     */
    public void SetUsername(String name) {
        username = name;
    }

}
