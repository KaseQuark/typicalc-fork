package edu.kit.typicalc.view;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.commands.TestBenchCommandExecutor;
import edu.kit.typicalc.view.pageobjects.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This example contains usage examples of screenshot comparison feature.
 * <p>
 */
public class ScreenshotIT extends AbstractIT {

    private static final String IDENTITY_TERM = "λx.x";
    private static final String LET_TERM = "let f = λx. g y y in f 3";

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
    public void createPermalink() throws IOException {
        InputBarElement inputBar = $(InputBarElement.class).first();
        inputBar.setCurrentValue(LET_TERM);

        assertEquals(LET_TERM, inputBar.getCurrentValue());

        inputBar.typeInfer();
        TestBenchCommandExecutor executor = getCommandExecutor();
        executor.waitForVaadin();

        assertTrue("Screenshot comparison for 'letView' failed, see "
                        + Parameters.getScreenshotErrorDirectory()
                        + " for error images",
                testBench().compareScreen("letView"));

        ControlPanelElement control = $(ControlPanelElement.class).waitForFirst();
        control.openShareDialog();
        executor.waitForVaadin();

        assertTrue("Screenshot comparison for 'letShareDialog' failed, see "
                        + Parameters.getScreenshotErrorDirectory()
                        + " for error images",
                testBench().compareScreen("letShareDialog"));

        ShareDialogElement shareDialogElement = $(ShareDialogElement.class).waitForFirst();
        String permalink = shareDialogElement.getPermalink();
        getDriver().get(permalink);

        assertTrue("Screenshot comparison for 'letView' from permalink failed, see "
                        + Parameters.getScreenshotErrorDirectory()
                        + " for error images",
                testBench().compareScreen("letView"));
        // TODO: jeden Schritt durchgehen?
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
    
    @Test
    public void exportLatexWithAssumptions() throws IOException {
        TestBenchCommandExecutor executor = getCommandExecutor();
        
        InputBarElement inputBar = $(InputBarElement.class).first();
        String term = "λx. f x";
        inputBar.setCurrentValue(term);
        
        // check if the correct term is entered
        Assert.assertEquals(term, inputBar.getCurrentValue());
        
        inputBar.openTypeAssumptionsArea();
        TypeAssumptionsAreaElement assumptionsArea = $(TypeAssumptionsAreaElement.class).waitForFirst();
        assumptionsArea.addTypeAssumption();
        executor.waitForVaadin();
        TypeAssumptionFieldElement assumptionField =  assumptionsArea.getLastTypeAssumption();
        
        String variable = "f";
        String type = "int -> y";
        assumptionField.setVariable(variable);
        assumptionField.setType(type);
        assumptionsArea.$(HorizontalLayoutElement.class).first().$(ButtonElement.class).last().focus();
        
        executor.waitForVaadin();
        // check if type assumption is added correctly
        assertTrue("Screenshot comparison for 'exportLatexWithAssumptions' (stage 1) failed",
                testBench().compareScreen("exportLatexWithAssumptions1"));
        assumptionsArea.closeDialog();
        
        inputBar.typeInfer();
        executor.waitForVaadin();
        ControlPanelElement controlPanel = $(ControlPanelElement.class).waitForFirst();
        controlPanel.lastStep();
        executor.waitForVaadin();
        
        // check if the algorithm is processed correctly
        assertTrue("Screenshot comparison for 'exportLatexWithAssumptions' (stage 2) failed",
                testBench().compareScreen("exportLatexWithAssumptions2"));
        
        controlPanel.openShareDialog();
        executor.waitForVaadin();
        // check if the share dialog content is correct
        assertTrue("Screenshot comparison for 'exportLatexWithAssumptions' (stage 3) failed",
                testBench().compareScreen("exportLatexWithAssumptions3"));
    }

    @Test
    public void testScenario1() throws IOException {
        InputBarElement inputBar = $(InputBarElement.class).first();
        String term = "λx. f x";
        inputBar.setCurrentValue(term);

        // check if the correct term is entered
        Assert.assertEquals(term, inputBar.getCurrentValue());
        inputBar.typeInfer();

        ControlPanelElement controlPanelElement = $(ControlPanelElement.class).first();

        assertTrue(testBench().compareScreen("testScenario1_step0"));
        controlPanelElement.nextStep();
        assertTrue(testBench().compareScreen("testScenario1_step1"));
        controlPanelElement.nextStep();
        assertTrue(testBench().compareScreen("testScenario1_step2"));
        controlPanelElement.nextStep();
        assertTrue(testBench().compareScreen("testScenario1_step3"));
        controlPanelElement.nextStep();
        assertTrue(testBench().compareScreen("testScenario1_step4"));
        controlPanelElement.previousStep();
        assertTrue(testBench().compareScreen("testScenario1_step3"));
        controlPanelElement.previousStep();
        assertTrue(testBench().compareScreen("testScenario1_step2"));
        controlPanelElement.firstStep();
        assertTrue(testBench().compareScreen("testScenario1_step0"));
        controlPanelElement.lastStep();
        assertTrue(testBench().compareScreen("testScenario1_step4gi"));
    }
}
