package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * Contains GUI elements to extract the URL and LaTeX code of the currently shown proof tree.
 */
public class ShareDialog extends Dialog implements LocaleChangeObserver {

    /**
     * Sets up three GUI elements, one for each parameter. The content of each element is equal
     * to the String that is passed as corresponding parameter.
     * @param url a permalink to share with other users
     * @param latexPackages the needed LaTeX-packages to use the displayed mathmatics
     * in other LaTeX documents. Should be in the form „\usepackage<package>“
     * @param latexCode LaTeX code for users to copy into their own LaTeX document(s)
     */
    protected ShareDialog(String url, String latexPackages, String latexCode) {

    }


    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
