package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Contains GUI elements to extract the URL and LaTeX code of the currently shown proof tree.
 */
@CssImport("./styles/view/share-dialog.css")
public class ShareDialog extends Dialog implements LocaleChangeObserver {

    private static final String SHARE_DIALOG_ID = "shareDialog";
    private static final String LAYOUT_ID = "share-dialog-layout";
    private static final String FIELD_CLASS = "share-dialog-field";

    private final TextField urlField;
    private final TextField packageField;
    private final TextArea latexArea;
    private final H3 heading;

    /**
     * Sets up three GUI elements, one for each parameter. The content of each element is equal
     * to the String that is passed as corresponding parameter.
     *
     * @param url           a permalink to share with other users
     * @param latexPackages the needed LaTeX-packages to use the displayed mathematics
     *                      in other LaTeX documents. Should be in the form „\\usepackage&ltpackage&gt“
     * @param latexCode     LaTeX code for users to copy into their own LaTeX document(s)
     */
    public ShareDialog(String url, String latexPackages, String latexCode) {
        VerticalLayout layout = new VerticalLayout();
        layout.setId(LAYOUT_ID);
        add(layout);
        setId(SHARE_DIALOG_ID);

        heading = new H3();
        urlField = new TextField();
        packageField = new TextField();
        latexArea = new TextArea();

        urlField.setValue(url);
        urlField.setClassName(FIELD_CLASS);
        packageField.setValue(latexPackages);
        packageField.setClassName(FIELD_CLASS);
        latexArea.setValue(latexCode);
        latexArea.setClassName(FIELD_CLASS);

        layout.add(heading, urlField, packageField, latexArea);
    }


    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        heading.setText(getTranslation("share.heading"));
        urlField.setLabel(getTranslation("share.url.label"));
        packageField.setLabel(getTranslation("share.packages.label"));
        latexArea.setLabel(getTranslation("share.latex.label"));
    }
}
