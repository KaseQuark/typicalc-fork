package edu.kit.typicalc.view.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

import java.util.List;
import java.util.function.Consumer;

public class ExampleDialog extends Dialog implements LocaleChangeObserver {
    private static final List<String> EXAMPLES =
            List.of("λx.x", "λx.λy.y x", "λx.λy.y (x x)", "let f = λx. g y y in f 3", "(λx.x x) (λx.x x)");
    private Paragraph instruction;

    public ExampleDialog(Consumer<String> callback) {
        VerticalLayout layout = new VerticalLayout();
        instruction = new Paragraph(getTranslation("root.selectExample"));
        layout.add(instruction);
        for (String term : EXAMPLES) {
            Button button = new Button(term);
            button.addClickListener(click -> callback.accept(term));
            layout.add(button);
        }
        add(layout);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        instruction.setText(getTranslation("root.selectExample"));
    }
}
