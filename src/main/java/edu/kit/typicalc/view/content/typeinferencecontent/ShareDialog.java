package edu.kit.typicalc.view.content.typeinferencecontent;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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

    private static final String HEADING_LAYOUT_ID = "share-heading-layout";
    private static final String SHARE_DIALOG_ID = "share-dialog";
    private static final String LAYOUT_ID = "share-dialog-layout";
    private static final String FIELD_CLASS = "share-dialog-field";
    private static final String CLOSE_ICON_CLASS = "close-icon";
    private static final String COPY_BUTTON_LAYOUT_CLASS = "copy-button-layout";

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
     * @param latexCodeUnification     LaTeX code for the unification for users to copy into their own LaTeX document(s)
     */
    public ShareDialog(String url, String latexCodeTree, String latexCodeUnification) {
        HorizontalLayout headingLayout = new HorizontalLayout();
        headingLayout.setId(HEADING_LAYOUT_ID);

        heading = new H3();
        Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
        closeIcon.addClickListener(event -> this.close());
        closeIcon.setClassName(CLOSE_ICON_CLASS);

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
        // remove <br> html elements added by the "latex" creator
        initializeFields(url, latexCodeTree,
                latexCodeUnification.replace("<br>", LatexCreatorConstants.LATEX_NEW_LINE));

        UI.getCurrent().getPage().executeJs("window.autoSelect($0)", FIELD_CLASS);

        setReadOnly();

        Button copyButtonUrl = makeCopyButton(urlField.getValue());
        Button copyButtonPackageAreaTree = makeCopyButton(packageAreaTree.getValue());
        Button copyButtonPackageAreaUnification = makeCopyButton(packageAreaUnification.getValue());
        Button copyButtonLatexAreaTree = makeCopyButton(latexAreaTree.getValue());
        Button copyButtonLatexAreaUnification = makeCopyButton(latexAreaUnification.getValue());

        HorizontalLayout urlLayout = new HorizontalLayout();
        urlLayout.setClassName(COPY_BUTTON_LAYOUT_CLASS);
        HorizontalLayout packageAreaTreeLayout = new HorizontalLayout();
        packageAreaTreeLayout.setClassName(COPY_BUTTON_LAYOUT_CLASS);
        HorizontalLayout latexAreaTreeLayout = new HorizontalLayout();
        latexAreaTreeLayout.setClassName(COPY_BUTTON_LAYOUT_CLASS);
        HorizontalLayout packageAreaUnificationLayout = new HorizontalLayout();
        packageAreaUnificationLayout.setClassName(COPY_BUTTON_LAYOUT_CLASS);
        HorizontalLayout latexAreaUnificationLayout = new HorizontalLayout();
        latexAreaUnificationLayout.setClassName(COPY_BUTTON_LAYOUT_CLASS);

        urlLayout.add(urlField, copyButtonUrl);
        packageAreaTreeLayout.add(packageAreaTree, copyButtonPackageAreaTree);
        latexAreaTreeLayout.add(latexAreaTree, copyButtonLatexAreaTree);
        packageAreaUnificationLayout.add(packageAreaUnification, copyButtonPackageAreaUnification);
        latexAreaUnificationLayout.add(latexAreaUnification, copyButtonLatexAreaUnification);

        layout.add(urlLayout, packageAreaTreeLayout, latexAreaTreeLayout, packageAreaUnificationLayout,
                latexAreaUnificationLayout);

        add(headingLayout, layout);
    }

    private void initializeFields(String url, String latexCodeTree, String latexCodeUnification) {
        urlField.setValue(url);
        urlField.setClassName(FIELD_CLASS);
        packageAreaTree.setValue(getTranslation("share.neededPackagesTree"));
        packageAreaTree.setClassName(FIELD_CLASS);
        packageAreaUnification.setValue(getTranslation("share.neededPackagesUnification"));
        packageAreaUnification.setClassName(FIELD_CLASS);
        latexAreaTree.setValue(latexCodeTree);
        latexAreaTree.setClassName(FIELD_CLASS);
        latexAreaUnification.setValue(latexCodeUnification
                .replaceAll(Pattern.quote(LatexCreatorConstants.SUBSTITUTION_SIGN), RIGHT_ARROW_WHITE));
        latexAreaUnification.setClassName(FIELD_CLASS);
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

    private Button makeCopyButton(String value) {
       Button newButton = new Button(new Icon(VaadinIcon.CLIPBOARD));
        newButton.addClickListener(event -> {
            UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", value);
            Notification.show(getTranslation("root.copied")).addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        });
        return newButton;
    }
}
