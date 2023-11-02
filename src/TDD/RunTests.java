package TDD;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import TDD.AccountPageTDD;
import TDD.RequestFormPageTDD;
import TDD.GameTDD;
//Add more classes if testing

@RunWith(Suite.class)
@SuiteClasses({ AccountPageTDD.class, 
        RequestFormPageTDD.class,
        GameTDD.class
        //Same classes as imported to test
})

public class RunTests { }
