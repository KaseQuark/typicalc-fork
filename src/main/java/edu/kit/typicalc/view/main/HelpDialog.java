package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

@CssImport("./styles/view/main/help-dialog.css")
public class HelpDialog extends Dialog implements LocaleChangeObserver {

    private static final long serialVersionUID = 4470277770276296164L;
    
    /*
     * Id's for the imported css-file
     */
    private static final String HEADING_LAYOUT_ID = "headingLayout";
    private static final String CONTENT_LAYOUT_ID = "contentLayout";
    
    private final H3 heading;
    private final H5 inputSyntax;
    
    public HelpDialog() {
	final HorizontalLayout headingLayout = new HorizontalLayout();
	heading = new H3(getTranslation("root.operatingHelp"));
	headingLayout.setId(HEADING_LAYOUT_ID);
	headingLayout.add(heading);
	
	final VerticalLayout contentLayout = new VerticalLayout();
	inputSyntax = new H5(getTranslation("root.inputSyntax"));
	contentLayout.setId(CONTENT_LAYOUT_ID);
	contentLayout.add(inputSyntax);
	add(headingLayout, contentLayout);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
	heading.setText(getTranslation("root.operatingHelp"));
	inputSyntax.setText(getTranslation("root.inputSyntax"));
    }
}
