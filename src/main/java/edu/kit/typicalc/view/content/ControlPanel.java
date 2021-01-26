package edu.kit.typicalc.view.content;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Provides a GUI in form of buttons for the user to navigate through steps.
 */
public class ControlPanel  {

    private Button firstStep;
    private Button lastStep;
    private Button nextStep;
    private Button previousStep;
    private Button share;

    /**
     * Sets up buttons with click-listeners that call the corresponding method in the view.
     * @param view the view that reacts to the button clicks
     */
    public ControlPanel(ControlPanelView view) {
        firstStep = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT), evt -> view.firstStepButton());
        lastStep = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT), evt -> view.lastStepButton());
        nextStep = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT), evt -> view.nextStepButton());
        previousStep = new Button(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT), evt -> view.previousStepButton());
        share = new Button(new Icon(VaadinIcon.CONNECT), evt -> view.shareButton());

        // todo change shortcut scope
        firstStep.addClickShortcut(Key.ARROW_LEFT, KeyModifier.CONTROL);
        lastStep.addClickShortcut(Key.ARROW_RIGHT, KeyModifier.CONTROL);
        nextStep.addClickShortcut(Key.ARROW_LEFT);
        previousStep.addClickShortcut(Key.ARROW_LEFT);
    }

    /**
     * Enables the firstStep-button if the parameter is true, disables it if hte parameter is false.
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledFirstStep(boolean setEnabled) {
        firstStep.setEnabled(setEnabled);
    }

    /**
     * Enables the lastStep-button if the parameter is true, disables it if hte parameter is false.
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledLastStep(boolean setEnabled) {
        lastStep.setEnabled(setEnabled);
    }

    /**
     * Enables the nextStep-button if the parameter is true, disables it if hte parameter is false.
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledNextStep(boolean setEnabled) {
        nextStep.setEnabled(setEnabled);
    }

    /**
     * Enables the previousStep-button if the parameter is true, disables it if hte parameter is false.
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledPreviousStep(boolean setEnabled) {
        previousStep.setEnabled(setEnabled);
    }

    /**
     * Enables the share-button if the parameter is true, disables it if hte parameter is false.
     * @param setEnabled true to enable the button,false to disable it
     */
    public void setEnabledShareButton(boolean setEnabled) {
        share.setEnabled(setEnabled);
    }
}
