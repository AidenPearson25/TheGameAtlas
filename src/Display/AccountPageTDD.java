package Display;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import javax.swing.JTextField;



public class AccountPageTDD {
    private AccountPage accountPage;

    @BeforeEach
    public void setUp() {
        accountPage = new AccountPage("TestAccount");
        accountPage.userText = new JTextField();
        accountPage.passwordText = new JTextField();
    }

    @Test
    public void testCheckLoginSuccess() {
        // Add a test user to the userDatabase
        accountPage.userDatabase.put("testuser", "testpassword");

        // Set the text fields with test data
        accountPage.userText.setText("testuser");
        accountPage.passwordText.setText("testpassword");

        String result = accountPage.checkLogin();
        assertEquals("testuser", result);
    }

    @Test
    public void testCheckLoginFailure() {
        // Set the text fields with incorrect test data
        accountPage.userText.setText("testuser");
        accountPage.passwordText.setText("wrongpassword");

        String result = accountPage.checkLogin();
        assertNull(result); // If login fails, the accountName should be null
    }
    
    @Test
    public void testCreateAccountSuccess() {
        accountPage.userText.setText("newuser");
        accountPage.passwordText.setText("newpassword");

        accountPage.createAccount();

        // Check that the userDatabase now contains the new user
        assertTrue(accountPage.userDatabase.containsKey("newuser"));
    }

    @Test
    public void testCreateAccountExistingUser() {
        // Add an existing user to the userDatabase
        accountPage.userDatabase.put("existinguser", "existingpassword");

        accountPage.userText.setText("existinguser");
        accountPage.passwordText.setText("newpassword");

        accountPage.createAccount();

        // Check that the userDatabase still contains the existing user
        assertTrue(accountPage.userDatabase.containsKey("existinguser"));
    }
}
