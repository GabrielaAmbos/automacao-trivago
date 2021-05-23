package tests.hooks;

import io.cucumber.java.*;
import utils.Browser;

public class Hooks {

    @Before
    public void setUp() {
        Browser.setCurrentBrowser(Browser.Type.CHROME, false);
    }

    @After
    public void tearDown() {
        Browser.quitBrowser();
    }
}
