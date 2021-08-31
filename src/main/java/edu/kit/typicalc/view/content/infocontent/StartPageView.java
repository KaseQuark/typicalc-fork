package edu.kit.typicalc.view.content.infocontent;

import com.flowingcode.vaadin.addons.carousel.Carousel;
import com.flowingcode.vaadin.addons.carousel.Slide;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.kit.typicalc.view.content.ControlPanel;
import edu.kit.typicalc.view.content.ControlPanelView;
import edu.kit.typicalc.view.main.MainViewImpl;

/**
 * The starting page of the application. Presents an introduction to the user.
 */
@Route(value = "", layout = MainViewImpl.class)
@PageTitle(MainViewImpl.PAGE_TITLE)
@JsModule("./src/mathjax-setup.js")
@CssImport("./styles/view/start-page.css")
public class StartPageView extends VerticalLayout implements ControlPanelView, LocaleChangeObserver {

    private static final long serialVersionUID = 2502750919087936406L;

    private static final String CONTENT_ID = "start-page-content";
    private static final String HEADING_ID = "start-page-heading";
    private static final String H_LINE_ID = "horizontal-line";
    private static final String TEXT_CONTAINER_ID = "start-text-container";
    private static final String LINK_CONTAINER_ID = "start-link-container";
    private static final String SLIDE_PROGRESS_ID = "slide-progress";
    private static final String CONTROL_PANEL_ID = "control-panel";
    private static final String START_PAGE_ID = "start-page";
    private static final String SLIDE_SHOW_ID = "slide-show";

    private static final String DOT = ".";

    private final Span introduction;
    private final Carousel slideShow;
    private final ProgressBar slideProgress;
    private final Text linkText;
    private final Anchor link;

    /**
     * Fills the view with content.
     */
    public StartPageView() {
        setId(START_PAGE_ID);

        VerticalLayout content = new VerticalLayout();
        content.setId(CONTENT_ID);
        ControlPanel controlPanel = new ControlPanel(this);
        controlPanel.setId(CONTROL_PANEL_ID);
        controlPanel.setEnabledShareButton(false);

        slideShow = createScenarioCarousel();
        slideShow.setId(SLIDE_SHOW_ID);

        H1 heading = new H1(getTranslation("root.typicalc"));
        heading.setId(HEADING_ID);
        Hr line = new Hr();
        line.setId(H_LINE_ID);
        introduction = new Span();

        linkText = new Text(getTranslation("root.linkText"));
        link = new Anchor(getTranslation("root.link"), getTranslation("root.here"));

        Div textContainer = createTextContainer();

        slideProgress = new ProgressBar(slideShow.getStartPosition(), slideShow.getSlides().length - 1);
        slideProgress.setId(SLIDE_PROGRESS_ID);

        content.add(heading, textContainer, slideProgress, slideShow, line);

        Footer footer = new Footer(controlPanel);
        add(content, footer);
    }

    private Div createTextContainer() {
        Div textContainer = new Div();
        textContainer.setId(TEXT_CONTAINER_ID);
        link.setTarget("_blank"); // opens new tab
        Paragraph linkContainer = new Paragraph(linkText, link, new Text(DOT));
        linkContainer.setId(LINK_CONTAINER_ID);
        textContainer.add(introduction, linkContainer);
        return textContainer;
    }

    private Carousel createScenarioCarousel() {
        Slide slide0 = new ImageSlide("root.image0", "root.text0");
        Slide slide1 = new ImageSlide("root.image1", "root.text1");
        Slide slide2 = new ImageSlide("root.image2", "root.text2");
        Slide slide3 = new ImageSlide("root.image3", "root.text3");
        Slide slide4 = new ImageSlide("root.image4", "root.text4");
        Slide slide5 = new ImageSlide("root.image5", "root.text5");
        Slide slide6 = new ImageSlide("root.image6", "root.text6");
        Slide slide7 = new ImageSlide("root.image7", "root.text7");
        Slide slide8 = new ImageSlide("root.image8", "root.text8");

        return new Carousel(slide0, slide1, slide2, slide3, slide4, slide5, slide6, slide7, slide8).withoutNavigation()
                .withoutSwipe();
    }

    @Override
    public void firstStepButton() {
        slideShow.movePos(0);
        slideProgress.setValue(0);
    }

    @Override
    public void lastStepButton() {
        slideShow.movePos(slideShow.getSlides().length - 1);
        slideProgress.setValue(slideShow.getSlides().length - 1);
    }

    @Override
    public void nextStepButton() {
        slideShow.moveNext();
        slideProgress.setValue((slideProgress.getValue() + 1) % slideShow.getSlides().length);
    }

    @Override
    public void previousStepButton() {
        slideShow.movePrev();
        slideProgress.setValue((slideProgress.getValue() - 1 + slideShow.getSlides().length)
                % slideShow.getSlides().length);
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        introduction.setText(getTranslation("root.slideExp"));
        linkText.setText(getTranslation("root.linkText"));
        link.setHref(getTranslation("root.link"));
        link.setText(getTranslation("root.here"));
    }
}
