package edu.kit.typicalc.view;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.commands.TestBenchCommandExecutor;
import edu.kit.typicalc.view.pageobjects.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This example contains usage examples of screenshot comparison feature.
 * <p>
 */
public class ScreenshotIT extends AbstractIT {

    private static final String IDENTITY_TERM = "λx.x";
    private static final String LET_TERM = "let f = λx. g y y in f 3";

    private List<Boolean> matches = new ArrayList<>();

    /**
     * We'll want to perform some additional setup functions, so we override the
     * setUp() method.
     */
    @Override
    public void setUp() {
        super.setUp();

        matches = new ArrayList<>();

        // Set a fixed viewport size so the screenshot is always the same
        // resolution
        testBench().resizeViewPortTo(1600, 800);

        // Define the directory for reference screenshots and for error files
        Parameters.setScreenshotReferenceDirectory("src/test/resources/screenshots");
        Parameters.setScreenshotErrorDirectory("target/screenshot_errors");
    }

    @After
    public void checkMatches() {
        for (int i = 0; i < matches.size(); i++) {
            assertTrue(String.format("comparison %d failed", i), matches.get(i));
        }
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

        matches.add(testBench().compareScreen("letView1"));

        ControlPanelElement control = $(ControlPanelElement.class).waitForFirst();
        control.openShareDialog();
        executor.waitForVaadin();

        matches.add(testBench().compareScreen("letShareDialog"));

        ShareDialogElement shareDialogElement = $(ShareDialogElement.class).waitForFirst();
        String permalink = shareDialogElement.getPermalink();
        getDriver().get(permalink);

        matches.add(testBench().compareScreen("letView2"));
        // TODO: jeden Schritt durchgehen?
    }

    @Test
    public void chooseExample() throws IOException {
        InputBarElement inputBar = $(InputBarElement.class).first();
        inputBar.openExampleDialog();

        ExampleDialogElement exampleDialog = $(ExampleDialogElement.class).waitForFirst();
        String term = "λx.x";
        exampleDialog.insertExample(term);

        matches.add(term.equals(inputBar.getCurrentValue()));

        TestBenchCommandExecutor executor = getCommandExecutor();
        executor.waitForVaadin();

        // check that the example is copied to the input bar
        matches.add(testBench().compareScreen("chooseExample1"));

        inputBar.typeInfer();
        executor.waitForVaadin();
        ControlPanelElement control = $(ControlPanelElement.class).waitForFirst();
        control.lastStep();
        executor.waitForVaadin();

        // check that the example is inferred correctly
        matches.add(testBench().compareScreen("chooseExample2"));

        control.lastStep();
        executor.waitForVaadin();
        // check that the example is unified correctly
        matches.add(testBench().compareScreen("chooseExample3"));
    }

    @Test
    public void exportLatexWithAssumptions() throws IOException {
        TestBenchCommandExecutor executor = getCommandExecutor();

        InputBarElement inputBar = $(InputBarElement.class).first();
        String term = "λx. f x";
        inputBar.setCurrentValue(term);

        // check if the correct term is entered
        matches.add(term.equals(inputBar.getCurrentValue()));

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
        matches.add(testBench().compareScreen("exportLatexWithAssumptions1"));
        assumptionsArea.closeDialog();

        inputBar.typeInfer();
        executor.waitForVaadin();
        ControlPanelElement controlPanel = $(ControlPanelElement.class).waitForFirst();
        controlPanel.lastStep();
        executor.waitForVaadin();

        // check if the algorithm is processed correctly
        matches.add(testBench().compareScreen("exportLatexWithAssumptions2"));

        controlPanel.openShareDialog();
        executor.waitForVaadin();
        // check if the share dialog content is correct
        matches.add(testBench().compareScreen("exportLatexWithAssumptions3"));
    }

    @Test
    public void testScenario1() throws IOException {
        TestBenchCommandExecutor executor = getCommandExecutor();

        InputBarElement inputBar = $(InputBarElement.class).first();
        String term = "λx. f x";
        inputBar.setCurrentValue(term);

        // check if the correct term is entered
        Assert.assertEquals(term, inputBar.getCurrentValue());
        inputBar.typeInfer();

        ControlPanelElement controlPanelElement = $(ControlPanelElement.class).first();
        executor.waitForVaadin();

        matches.add(testBench().compareScreen("testScenario1_step0"));
        controlPanelElement.nextStep();
        executor.waitForVaadin();
        matches.add(testBench().compareScreen("testScenario1_step1"));
        controlPanelElement.nextStep();
        executor.waitForVaadin();
        matches.add(testBench().compareScreen("testScenario1_step2"));
        controlPanelElement.nextStep();
        executor.waitForVaadin();
        matches.add(testBench().compareScreen("testScenario1_step3"));
        controlPanelElement.nextStep();
        executor.waitForVaadin();
        matches.add(testBench().compareScreen("testScenario1_step4"));
        controlPanelElement.previousStep();
        executor.waitForVaadin();
        matches.add(testBench().compareScreen("testScenario1_step3"));
        controlPanelElement.previousStep();
        executor.waitForVaadin();
        matches.add(testBench().compareScreen("testScenario1_step2"));
        controlPanelElement.firstStep();
        executor.waitForVaadin();
        matches.add(testBench().compareScreen("testScenario1_step0"));
        controlPanelElement.lastStep();
        executor.waitForVaadin();
        matches.add(testBench().compareScreen("testScenario1_step4gi"));
    }
    
    @Test
    public void infoDialogTest() throws IOException {
        InputBarElement inputBar = $(InputBarElement.class).first();
        inputBar.openInfoDialog();
        
        matches.add(testBench().compareScreen("infoDialog"));
    }
    
    @Test
    public void helpDialogTest() throws IOException {
        UpperBarElement upperBar = $(UpperBarElement.class).first();
        upperBar.openHelpDialog();
        
        matches.add(testBench().compareScreen("helpDialog"));
    }
}
