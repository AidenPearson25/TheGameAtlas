package Display;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class for the page where users can request a game
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
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints gridConstraint = new GridBagConstraints();
        layout.setLayout(grid);
        
        // Labels for the text prompts next to the text fields
        JLabel titlePrompt;
        JLabel linkPrompt;
        
        // Create GridBagConstraints for component placement
        gridConstraint.insets.left = 10;
        gridConstraint.insets.top = 10;
        
        // Adding the back button
        gridConstraint.weightx = 0;
        backButton = new JButton("Back");
        gridConstraint.gridx = 0;
        gridConstraint.insets.bottom = 20;
        grid.setConstraints(backButton, gridConstraint);
        layout.add(backButton);
        
        // Initial constraint
        gridConstraint.weightx = 1.0;
        gridConstraint.weighty = 0;
        gridConstraint.gridx = 1;
        
        // Title Prompt text
        titlePrompt = new JLabel("Game Title: ");
        gridConstraint.gridx = GridBagConstraints.CENTER - 1;
        gridConstraint.gridy = 1;
        titlePrompt.setPreferredSize(new Dimension(100, 50));
        grid.setConstraints(titlePrompt, gridConstraint);
        layout.add(titlePrompt);
        
        // Text field for title
        titleField = new JTextField(30);
        gridConstraint.weightx = 0.5;
        gridConstraint.gridx = GridBagConstraints.RELATIVE - 2;
        grid.setConstraints(titleField, gridConstraint);
        titleField.setPreferredSize(new Dimension(500, 50));
        layout.add(titleField);
        
        // Link Prompt text
        linkPrompt = new JLabel("Link to Game: ");
        gridConstraint.gridx = GridBagConstraints.CENTER - 1;
        gridConstraint.gridy = 3;
        linkPrompt.setPreferredSize(new Dimension(100, 50));
        grid.setConstraints(linkPrompt, gridConstraint);
        layout.add(linkPrompt);
        
        // Text field for link
        linkField = new JTextField(30);
        gridConstraint.gridx = GridBagConstraints.RELATIVE - 2;
        gridConstraint.weightx = 0.5;
        grid.setConstraints(linkField, gridConstraint);
        linkField.setPreferredSize(new Dimension(650, 25));
        layout.add(linkField);
        
        // Button to submit form
        submit = new JButton("Submit Form");
        gridConstraint.gridx = GridBagConstraints.CENTER + 2;
        gridConstraint.gridy = 4;
        gridConstraint.gridwidth = 1;
        grid.setConstraints(submit, gridConstraint);
        submit.setPreferredSize(new Dimension(125, 25));
        layout.add(submit);
        
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
                    new FileWriter("RequestDatabase.txt", true));
            write.println(title + "::" + link + "::" + username);
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        
        titleField.setText("");
        linkField.setText("");
        
        JOptionPane.showMessageDialog(null, "Form Submitted Successfully!");
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
