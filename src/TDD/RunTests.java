package TDD;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountPageTDD.class, 
        RequestFormPageTDD.class,
        GameThumbnailTDD.class,
        //Add classes to test
})

public class RunTests { }
