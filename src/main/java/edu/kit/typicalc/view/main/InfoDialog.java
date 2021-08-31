package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Dialog which contains information on the correct syntax for the users input.
 */
@CssImport("./styles/view/main/info-dialog.css")
public class InfoDialog extends Dialog {
    private static final long serialVersionUID = 2914411566361539614L;

    /*
     * IDs for the imported .css-file
     */
    private static final String INFO_HEADER_ID = "info-header";
    private static final String INFO_CONTENT_ID = "info-content";
    private static final String CLOSE_ICON_CLASS = "close-icon";

    /**
     * Creates new InfoDialog.
     */
    protected InfoDialog() {
        H4 heading = new H4(getTranslation("root.inputSyntax"));
        HorizontalLayout infoHeader = new HorizontalLayout(heading);
        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setClassName(CLOSE_ICON_CLASS);
        infoHeader.setId(INFO_HEADER_ID);
        infoHeader.add(closeIcon);

        VerticalLayout infoContent = new InfoContent();
        infoContent.setId(INFO_CONTENT_ID);

        add(infoHeader, infoContent);
    }
}
