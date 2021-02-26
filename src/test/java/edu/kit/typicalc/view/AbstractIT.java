package edu.kit.typicalc.view;

import org.junit.Before;
import org.junit.Rule;

import com.vaadin.testbench.IPAddress;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchTestCase;

import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Base class for all our integration tests, allowing us to change the applicable driver,
 * test URL or other configurations in one place.
 */
public abstract class AbstractIT extends TestBenchTestCase {

    @Rule
    public ScreenshotOnFailureRule rule = new ScreenshotOnFailureRule(this,
            true);

    @Before
    public void setUp() {
        setDriver(new FirefoxDriver());
        String ip;
        if (System.getProperty("testbench.use127001") != null) {
            ip = "127.0.0.1";
        } else {
            ip = IPAddress.findSiteLocalAddress();
        }
        getDriver().get("http://" + ip + ":8080");
    }

}
