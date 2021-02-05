package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Contains GUI elements to extract the URL and LaTeX code of the currently shown proof tree.
 */
public class ShareDialog extends Dialog implements LocaleChangeObserver {

    private final VerticalLayout layout;
    private final TextField urlField;
    private final TextField packageField;
    private final TextArea latexArea;

    /**
     * Sets up three GUI elements, one for each parameter. The content of each element is equal
     * to the String that is passed as corresponding parameter.
     * @param url a permalink to share with other users
     * @param latexPackages the needed LaTeX-packages to use the displayed mathematics
     * in other LaTeX documents. Should be in the form „\\usepackage<package>“
     * @param latexCode LaTeX code for users to copy into their own LaTeX document(s)
     */
    public ShareDialog(String url, String latexPackages, String latexCode) {
        setWidth(80, Unit.PERCENTAGE);
        layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.START);
        layout.setSizeFull();
        add(layout);

        urlField = new TextField(getTranslation("share.url.label"));
        packageField = new TextField(getTranslation("share.packages.label"));
        latexArea = new TextArea(getTranslation("share.latex.label"));

        urlField.setValue(url);
        packageField.setValue(latexPackages);
        latexArea.setValue(latexCode);

        urlField.setWidthFull();
        packageField.setWidthFull();
        latexArea.setWidthFull();

        layout.add(urlField, packageField, latexArea);
    }


    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        urlField.setLabel(getTranslation("share.url.label"));
        packageField.setLabel(getTranslation("share.packages.label"));
        latexArea.setLabel(getTranslation("share.latex.label"));
    }
}
