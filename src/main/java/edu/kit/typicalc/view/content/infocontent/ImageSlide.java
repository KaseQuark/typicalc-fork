package edu.kit.typicalc.view.content.infocontent;

import org.apache.commons.lang3.StringUtils;

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
    
    private static final String SLIDE_LAYOUT_ID = "slideLayout";
    private static final String EXPLANATION_ID = "explanation";
    
    private final VerticalLayout slideLayout;
    private final Span explanation;
    private final String explanationKey;
    private Image image;
    private final String imagePathKey;
    
    /**
     * Create a new ImageSlide with a key for the path of the image and a key for the text.
     * 
     * @param imgPathKey key for the path of the image
     * @param textKey key for the text
     */
    public ImageSlide(String imgPathKey, String textKey) {
        slideLayout = new VerticalLayout();
        this.imagePathKey = imgPathKey;
        image = new Image(getTranslation(imgPathKey), StringUtils.EMPTY);
        explanationKey = textKey;
        explanation = new Span(getTranslation(explanationKey));
        explanation.setId(EXPLANATION_ID);
        slideLayout.add(image, explanation);
        slideLayout.setId(SLIDE_LAYOUT_ID);
        add(slideLayout);
    }
    
    @Override
    public void localeChange(LocaleChangeEvent event) {
        explanation.setText(getTranslation(explanationKey));
        slideLayout.remove(image, explanation);
        image = new Image(getTranslation(imagePathKey), StringUtils.EMPTY);
        slideLayout.add(image, explanation);
    }

}
