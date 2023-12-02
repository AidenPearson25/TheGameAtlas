package TDD;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JTextField;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;
import Display.RequestFormPage;

/**
 * @author Josef Arnold
 *
 */
class RequestFormPageTDD {
    
    private RequestFormPage form;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() {
        form = new RequestFormPage("requestFormPage");
        form.titleField = new JTextField();
        form.linkField = new JTextField();
    }

    @Test
    public void testCheckRequestStringSuccess() {
        form.username = "testuser";
        
        form.titleField.setText("testtitle");
        form.linkField.setText("testlink.com");
        
        form.SubmitForm();
        
        try {
        Scanner f = new Scanner(new File("RequestDatabase.txt"));
        
        // Testing to see the String is written to the file correctly
        assertTrue(f.next().equals("testtitle:testlink.com:testuser"));
        
        f.close();
        
        } catch (FileNotFoundException e) {
            
        }
        
    }
    
    @Test
    public void testCheckRequestStringFailure() {
        form.username = "failuser";
        
        form.titleField.setText("failtitle");
        
        form.SubmitForm();
        
        try {
            Scanner f = new Scanner(new File("RequestDatabase.txt"));
            
            // Only one text field is filled out
            assertTrue(f.next().equals("failtitle::failuser"));
            
            f.close();
            
            } catch (FileNotFoundException e) {
                
            }
    }

}
