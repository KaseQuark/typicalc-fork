package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants;

import java.util.regex.Pattern;

/**
 * Contains GUI elements to extract the URL and LaTeX code of the currently shown proof tree.
 */
@JsModule("./src/share-dialog-autoselect.ts")
@CssImport("./styles/view/share-dialog.css")
public class ShareDialog extends Dialog implements LocaleChangeObserver {

    private static final String HEADING_LAYOUT_ID = "headingLayout";
    private static final String SHARE_DIALOG_ID = "shareDialog";
    private static final String LAYOUT_ID = "share-dialog-layout";
    private static final String FIELD_CLASS = "share-dialog-field";
    private static final String CLOSE_ICON_ID = "closeIcon";

    private static final String RIGHT_ARROW_WHITE = "\\\\rightwhitearrow";

    private final TextField urlField;
    private final TextArea packageAreaTree;
    private final TextArea packageAreaUnification;
    private final TextArea latexAreaTree;
    private final TextArea latexAreaUnification;
    private final H3 heading;

    /**
     * Sets up three GUI elements, one for each parameter. The content of each element is equal
     * to the String that is passed as corresponding parameter.
     *
     * @param url           a permalink to share with other users
     * @param latexCodeTree     LaTeX code for the tree for users to copy into their own LaTeX document(s)
     */
    public ShareDialog(String url, String latexCodeTree, String latexCodeUnification) {
        HorizontalLayout headingLayout = new HorizontalLayout();
        headingLayout.setId(HEADING_LAYOUT_ID);

        heading = new H3();
        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setId(CLOSE_ICON_ID);

        headingLayout.add(heading);
        headingLayout.add(closeIcon);

        VerticalLayout layout = new VerticalLayout();
        layout.setId(LAYOUT_ID);
        setId(SHARE_DIALOG_ID);

        urlField = new TextField();
        packageAreaTree = new TextArea();
        latexAreaTree = new TextArea();
        packageAreaUnification = new TextArea();
        latexAreaUnification = new TextArea();

        urlField.setValue(url);
        urlField.setClassName(FIELD_CLASS);
        packageAreaTree.setValue(getTranslation("share.neededPackagesTree")); //todo
        packageAreaTree.setClassName(FIELD_CLASS);
        packageAreaUnification.setValue(getTranslation("share.neededPackagesUnification")); //todo
        packageAreaUnification.setClassName(FIELD_CLASS);
        latexAreaTree.setValue(latexCodeTree);
        latexAreaTree.setClassName(FIELD_CLASS);
        latexAreaUnification.setValue(latexCodeUnification
                .replaceAll(Pattern.quote(LatexCreatorConstants.SUBSTITUTION_SIGN), RIGHT_ARROW_WHITE));
        latexAreaUnification.setClassName(FIELD_CLASS);
        UI.getCurrent().getPage().executeJs("window.autoSelect($0)", FIELD_CLASS);

        setReadOnly();

        layout.add(urlField, packageAreaTree, latexAreaTree, packageAreaUnification, latexAreaUnification);

        add(headingLayout, layout);
    }


    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        heading.setText(getTranslation("share.heading"));
        urlField.setLabel(getTranslation("share.url.label"));
        packageAreaTree.setLabel(getTranslation("share.packagesTree.label"));
        latexAreaTree.setLabel(getTranslation("share.latexTree.label"));
        packageAreaUnification.setLabel(getTranslation("share.packagesUnification.label"));
        latexAreaUnification.setLabel(getTranslation("share.latexUnification.label"));
    }

    private void setReadOnly() {
        urlField.setReadOnly(true);
        packageAreaTree.setReadOnly(true);
        latexAreaTree.setReadOnly(true);
        packageAreaUnification.setReadOnly(true);
        latexAreaUnification.setReadOnly(true);
    }
}
