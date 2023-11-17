package TDD;
import Data.Account;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class AccountTDD {
	@Test
	public void testAccount() {
		Account admin = new Account("Test,TestAcount,TestPassword,2");
		Account mod = new Account("Test,TestAcount,TestPassword,1");
		Account user = new Account("Test,TestAcount,TestPassword,0");
		
		assertEquals(admin.getPassword(), "TestPassword");
		assertTrue(admin.checkAccess(2));
		assertFalse(mod.checkAccess(2));
		assertTrue(mod.checkAccess(1));
		assertFalse(user.checkAccess(1));
		assertTrue(user.checkAccess(0));
		assertTrue(admin.checkAccess(0));
	}
}
