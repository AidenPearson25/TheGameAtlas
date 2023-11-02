package TDD;

import Display.RequestPage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RequestPageTDD {

    @Test
    void test() {
        RequestPage request = new RequestPage("Request");
        assertNull(request.checkAdminStatus() == false); // User class not implemented
        assertNull(request.getAllRequest() != null); 
    }
}
