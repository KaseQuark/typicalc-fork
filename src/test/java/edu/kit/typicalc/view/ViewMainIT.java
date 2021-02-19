package edu.kit.typicalc.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import edu.kit.typicalc.view.pageobjects.ErrorNotificationElement;
import edu.kit.typicalc.view.pageobjects.ExampleDialogElement;
import edu.kit.typicalc.view.pageobjects.InputBarElement;

public class ViewMainIT extends AbstractIT {
    
    private static final String EMPTY_INPUT_ERROR = "Falsche Eingabe! Der Term endet abrupt.";
    private static final String INPUT_EXAMPLE = "let f = λx. g y y in f 3";

    @Test
    public void emptyInput() {
        InputBarElement inputBar = $(InputBarElement.class).first();
        inputBar.setCurrentValue(StringUtils.EMPTY);
        
        assertEquals(StringUtils.EMPTY, inputBar.getCurrentValue());
        inputBar.typeInfer();
        
        ErrorNotificationElement errorNotification = $(ErrorNotificationElement.class).waitForFirst();
        assertTrue(errorNotification.isOpen());
        String errorMessage = errorNotification.getErrorParagraph().getText();
        assertEquals(EMPTY_INPUT_ERROR, errorMessage);
        
        errorNotification.close();
        assertFalse(errorNotification.isOpen());
    }
    
    @Test
    public void chooseExample() {
        InputBarElement inputBar = $(InputBarElement.class).first();
        inputBar.openExampleDialog();
        
        ExampleDialogElement exampleDialog = $(ExampleDialogElement.class).waitForFirst();
        exampleDialog.insertExample(INPUT_EXAMPLE);
        
        assertEquals(INPUT_EXAMPLE, inputBar.getCurrentValue());
    }
}
