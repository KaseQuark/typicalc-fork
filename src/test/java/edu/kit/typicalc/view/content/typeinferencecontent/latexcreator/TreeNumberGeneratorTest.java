package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TreeNumberGeneratorTest {

    @Test
    void normalFunctionalityTest() {
        List<Integer> expected = List.of(0, 1, 1, 1, 2, 2, 3);
        TreeNumberGenerator generator = new TreeNumberGenerator();
        generator.incrementPush();
        generator.incrementPush();
        generator.push();
        generator.push();
        generator.incrementPush();
        generator.push();
        generator.incrementPush();
        List<Integer> actual = generator.getNumbers();

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    void pushExceptionTest() {
        TreeNumberGenerator generator = new TreeNumberGenerator();
        assertThrows(IllegalStateException.class, generator::push);
    }

}
