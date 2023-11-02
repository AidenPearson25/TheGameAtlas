package Display;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JTextField;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Scanner;

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
            
            assertTrue(f.next().equals("failtitle::failuser"));
            
            f.close();
            
            } catch (FileNotFoundException e) {
                
            }
    }

}
