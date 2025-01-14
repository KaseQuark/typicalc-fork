package edu.kit.typicalc.view.content.infocontent;

import com.flowingcode.vaadin.addons.carousel.Slide;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

/**
 * A Slide for the vaadin carousel-addon. This Slide consists of an image and a text.
 */
@CssImport("./styles/view/image-slide.css")
public class ImageSlide extends Slide implements LocaleChangeObserver {

    private static final long serialVersionUID = 232255503611054445L;

    private static final String SLIDE_LAYOUT_ID = "slide-layout";
    private static final String EXPLANATION_ID = "slide-explanation";

    private final VerticalLayout slideLayout;
    private final Span explanation;
    private final String explanationKey;
    private final String imageKey;

    /**
     * Create a new ImageSlide with the key of the image and a key for the text.
     *
     * @param imgKey the key of the image
     * @param textKey key for the text
     */
    public ImageSlide(String imgKey, String textKey) {
        slideLayout = new VerticalLayout();
        imageKey = imgKey;
        explanationKey = textKey;
        explanation = new Span();
        explanation.setId(EXPLANATION_ID);
        slideLayout.setId(SLIDE_LAYOUT_ID);
        add(slideLayout);
        // actual content added in localeChange
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        slideLayout.removeAll();
        explanation.setText(getTranslation(explanationKey));
        slideLayout.add(explanation, new Image(getTranslation(imageKey), ""));
    }

}
