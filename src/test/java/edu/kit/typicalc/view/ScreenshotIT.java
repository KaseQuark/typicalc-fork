package edu.kit.typicalc.view;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.commands.TestBenchCommandExecutor;
import edu.kit.typicalc.view.pageobjects.ControlPanelElement;
import edu.kit.typicalc.view.pageobjects.ExampleDialogElement;
import edu.kit.typicalc.view.pageobjects.InputBarElement;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * This example contains usage examples of screenshot comparison feature.
 * <p>
 */
public class ScreenshotIT extends AbstractIT {

    private static final String IDENTITY_TERM = "λx.x";

    /**
     * We'll want to perform some additional setup functions, so we override the
     * setUp() method.
     */
    @Override
    public void setUp() {
        super.setUp();

        // Set a fixed viewport size so the screenshot is always the same
        // resolution
        testBench().resizeViewPortTo(1600, 800);

        // Define the directory for reference screenshots and for error files
        Parameters.setScreenshotReferenceDirectory("src/test/resources/screenshots");
        Parameters.setScreenshotErrorDirectory("target/screenshot_errors");
    }

    @Test
    public void initialView() throws Exception {
        // Change this calculation after running the test once to see how errors
        // in screenshots are verified.
        // The output is placed in target/screenshot_errors

        // Compare screen with reference image with id "oneplustwo" from the
        // reference image directory. Reference image filenames also contain
        // browser, version and platform.
        assertTrue(
                "Screenshot comparison for 'initialView' failed, see "
                        + Parameters.getScreenshotErrorDirectory()
                        + " for error images",
                testBench().compareScreen("initialView"));
    }

    @Test
    public void basicExecution() throws Exception {
        //TODO take screenshot and add to proper folder
        InputBarElement inputBar = $(InputBarElement.class).first();
        inputBar.setCurrentValue(IDENTITY_TERM);

        assertEquals(IDENTITY_TERM, inputBar.getCurrentValue());

        inputBar.typeInfer();
        TestBenchCommandExecutor executer = getCommandExecutor();
        executer.waitForVaadin();

        assertTrue("Screenshot comparison for 'identityView' failed, see "
                        + Parameters.getScreenshotErrorDirectory()
                        + " for error images",
                testBench().compareScreen("identityView"));
    }

    @Test
    public void chooseExample() throws IOException {
        InputBarElement inputBar = $(InputBarElement.class).first();
        inputBar.openExampleDialog();

        ExampleDialogElement exampleDialog = $(ExampleDialogElement.class).waitForFirst();
        String term = "λx.x";
        exampleDialog.insertExample(term);

        Assert.assertEquals(term, inputBar.getCurrentValue());

        TestBenchCommandExecutor executor = getCommandExecutor();
        executor.waitForVaadin();

        // check that the example is copied to the input bar
        // TODO: the blinking cursor could cause issues here
        assertTrue("Screenshot comparison for 'chooseExample' (stage 1) failed",
                testBench().compareScreen("chooseExample1"));

        inputBar.typeInfer();
        executor.waitForVaadin();
        ControlPanelElement control = $(ControlPanelElement.class).waitForFirst();
        control.lastStep();
        executor.waitForVaadin();

        // check that the example is inferred correctly
        assertTrue("Screenshot comparison for 'chooseExample' (stage 2) failed",
                testBench().compareScreen("chooseExample2"));

        control.lastStep();
        executor.waitForVaadin();
        // check that the example is unified correctly
        assertTrue("Screenshot comparison for 'chooseExample' (stage 3) failed",
                testBench().compareScreen("chooseExample3"));
    }
}
