package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.*;
import static edu.kit.typicalc.view.content.typeinferencecontent.latexcreator.LatexCreatorConstants.CURLY_RIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstraintSetIndexFactoryTest {

    @Test
    void nextConstraintSetIndexTest() throws NoSuchFieldException, IllegalAccessException {
        Field field = ConstraintSetIndexFactory.class.getDeclaredField("FIRST_CONSTRAINT_SET_INDEX");
        field.setAccessible(true);
        int firstIndex = field.getInt(null);

        ConstraintSetIndexFactory factory = new ConstraintSetIndexFactory();

        assertEquals("", factory.nextConstraintSetIndex());
        assertEquals("" + UNDERSCORE + CURLY_LEFT + LET + CURLY_RIGHT, factory.nextConstraintSetIndex());
        assertEquals("" + UNDERSCORE + CURLY_LEFT + LET + UNDERSCORE
                + CURLY_LEFT + (firstIndex + 2) + CURLY_RIGHT + CURLY_RIGHT, factory.nextConstraintSetIndex());
        assertEquals("" + UNDERSCORE + CURLY_LEFT + LET + UNDERSCORE
                + CURLY_LEFT + (firstIndex + 3) + CURLY_RIGHT + CURLY_RIGHT, factory.nextConstraintSetIndex());
        assertEquals("" + UNDERSCORE + CURLY_LEFT + LET + UNDERSCORE
                + CURLY_LEFT + (firstIndex + 4) + CURLY_RIGHT + CURLY_RIGHT, factory.nextConstraintSetIndex());
    }
}
