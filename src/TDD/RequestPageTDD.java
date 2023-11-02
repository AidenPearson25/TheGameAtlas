package TDD;

import Display.RequestPage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RequestPageTDD {

    @Test
    void test() {
        RequestPage request = new RequestPage("Request");
        assertTrue(request.checkAdminStatus() == false); // User class not implemented
        assertTrue(request.getAllRequest() != null); 
        assertTrue(request.makeGameFromRequest() != null);
    }
}
