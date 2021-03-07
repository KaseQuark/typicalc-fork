package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.CURLY_LEFT;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.CURLY_RIGHT;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.LET;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.UNDERSCORE;

/**
 * A constraint set index factory is used to create consecutive indices for multiple (let-) constraint sets
 * that might be created during the inference of one lambda term.
 */
public class ConstraintSetIndexFactory {

    private static final int FIRST_CONSTRAINT_SET_INDEX = 0;
    private int nextConstraintSetIndex;

    /**
     * Creates a new factory.
     */
    protected ConstraintSetIndexFactory() {
        nextConstraintSetIndex = FIRST_CONSTRAINT_SET_INDEX;
    }

    /**
     * Returns the next constraint set index.
     *
     * @return the next constraint set index
     */
    protected String nextConstraintSetIndex() {
        String realIndex = nextConstraintSetIndex == FIRST_CONSTRAINT_SET_INDEX + 1
                ? "" + UNDERSCORE + CURLY_LEFT + LET + CURLY_RIGHT
                : "" + UNDERSCORE + CURLY_LEFT + LET + UNDERSCORE
                + CURLY_LEFT + nextConstraintSetIndex + CURLY_RIGHT + CURLY_RIGHT;

        String index = nextConstraintSetIndex == FIRST_CONSTRAINT_SET_INDEX
                ? ""
                : realIndex;
        nextConstraintSetIndex++;
        return index;
    }
}
