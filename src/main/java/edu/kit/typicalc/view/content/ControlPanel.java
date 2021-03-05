package edu.kit.typicalc.view.content;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Provides a GUI in form of buttons for the user to navigate through steps.
 */
public class ControlPanel extends HorizontalLayout {
    public static final String ID = "control-panel";

    private final Button firstStep;
    public static final String FIRST_STEP_ID = "first-step";
    private final Button lastStep;
    public static final String LAST_STEP_ID = "last-step";
    private final Button nextStep;
    public static final String NEXT_STEP_ID = "next-step";
    private final Button previousStep;
    public static final String PREVIOUS_STEP_ID = "previous-step";
    private final Button share;

    /**
     * Sets up buttons with click-listeners that call the corresponding method in the view.
     *
     * @param view the view that reacts to the button clicks
     */
    public ControlPanel(ControlPanelView view) {
        setId(ID);
        firstStep = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT), evt -> view.firstStepButton());
        firstStep.setId(FIRST_STEP_ID);
        lastStep = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT), evt -> view.lastStepButton());
        lastStep.setId(LAST_STEP_ID);
        nextStep = new Button(new Icon(VaadinIcon.ANGLE_RIGHT), evt -> view.nextStepButton());
        nextStep.setId(NEXT_STEP_ID);
        previousStep = new Button(new Icon(VaadinIcon.ANGLE_LEFT), evt -> view.previousStepButton());
        previousStep.setId(PREVIOUS_STEP_ID);
        share = new Button(new Icon(VaadinIcon.CONNECT), evt -> view.shareButton());

        add(share, firstStep, previousStep, nextStep, lastStep);
    }

    /**
     * Enables the firstStep-button if the parameter is true, disables it if hte parameter is false.
     *
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledFirstStep(boolean setEnabled) {
        firstStep.setEnabled(setEnabled);
    }

    /**
     * Enables the lastStep-button if the parameter is true, disables it if hte parameter is false.
     *
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledLastStep(boolean setEnabled) {
        lastStep.setEnabled(setEnabled);
    }

    /**
     * Enables the nextStep-button if the parameter is true, disables it if hte parameter is false.
     *
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledNextStep(boolean setEnabled) {
        nextStep.setEnabled(setEnabled);
    }

    /**
     * Enables the previousStep-button if the parameter is true, disables it if hte parameter is false.
     *
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledPreviousStep(boolean setEnabled) {
        previousStep.setEnabled(setEnabled);
    }

    /**
     * Enables the share-button if the parameter is true, disables it if hte parameter is false.
     *
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledShareButton(boolean setEnabled) {
        share.setEnabled(setEnabled);
    }
}
