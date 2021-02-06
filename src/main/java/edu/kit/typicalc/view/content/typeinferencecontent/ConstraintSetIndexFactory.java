package edu.kit.typicalc.view.content.typeinferencecontent;

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
    public int nextConstraintSetIndex() {
        return nextConstraintSetIndex++;
    }
}
