package edu.kit.typicalc.view.content.typeinferencecontent.latexcreator;

import edu.kit.typicalc.model.Model;
import edu.kit.typicalc.model.ModelImpl;
import edu.kit.typicalc.model.TypeInfererInterface;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LatexCreatorTest {
    private final Model model = new ModelImpl();

    @Test
    void testFailedLet() {
        TypeInfererInterface typeInferer = model.getTypeInferer("let fun = 5 5 in fun 42", new HashMap<>()).unwrap();
        String latex = new LatexCreator(typeInferer, Enum::toString).getTree();
        assertEquals("\\begin{prooftree}\n" +
                "\\AxiomC{$\\texttt{5} \\in Const$}\n" +
                "\\LeftLabel{\\textrm C{\\small ONST}}\n" +
                "\\UnaryInfC{$\\vdash\\texttt{5}:\\alpha_{3}$}\n" +
                "\\AxiomC{$\\texttt{5} \\in Const$}\n" +
                "\\LeftLabel{\\textrm C{\\small ONST}}\n" +
                "\\UnaryInfC{$\\vdash\\texttt{5}:\\alpha_{4}$}\n" +
                "\\LeftLabel{\\textrm A{\\small PP}}\n" +
                "\\BinaryInfC{$\\vdash\\texttt{5}\\ \\texttt{5}:\\alpha_{2}$}\n" +
                "\\AxiomC{}\n" +
                "\\LeftLabel{\\textrm L{\\small ET}}\n" +
                "\\BinaryInfC{$\\vdash\\texttt{\\textbf{let}}\\ \\texttt{fun}=\\texttt{5}\\ \\texttt{5}\\ \\texttt{\\textbf{in}}\\ \\texttt{fun}\\ \\texttt{42}:\\alpha_{1}$}\n" +
                "\\end{prooftree}", latex);
        typeInferer = model.getTypeInferer("(let fun = 5 5 in fun) 42", new HashMap<>()).unwrap();
        latex = new LatexCreator(typeInferer, Enum::toString).getTree();
        assertEquals("\\begin{prooftree}\n" +
                "\\AxiomC{$\\texttt{5} \\in Const$}\n" +
                "\\LeftLabel{\\textrm C{\\small ONST}}\n" +
                "\\UnaryInfC{$\\vdash\\texttt{5}:\\alpha_{5}$}\n" +
                "\\AxiomC{$\\texttt{5} \\in Const$}\n" +
                "\\LeftLabel{\\textrm C{\\small ONST}}\n" +
                "\\UnaryInfC{$\\vdash\\texttt{5}:\\alpha_{6}$}\n" +
                "\\LeftLabel{\\textrm A{\\small PP}}\n" +
                "\\BinaryInfC{$\\vdash\\texttt{5}\\ \\texttt{5}:\\alpha_{4}$}\n" +
                "\\AxiomC{}\n" +
                "\\LeftLabel{\\textrm L{\\small ET}}\n" +
                "\\BinaryInfC{$\\vdash\\texttt{\\textbf{let}}\\ \\texttt{fun}=\\texttt{5}\\ \\texttt{5}\\ \\texttt{\\textbf{in}}\\ \\texttt{fun}:\\alpha_{2}$}\n" +
                "\\AxiomC{$\\vdash\\texttt{42}:\\alpha_{3}$}\n" +
                "\\LeftLabel{\\textrm A{\\small PP}}\n" +
                "\\BinaryInfC{$\\vdash(\\texttt{\\textbf{let}}\\ \\texttt{fun}=\\texttt{5}\\ \\texttt{5}\\ \\texttt{\\textbf{in}}\\ \\texttt{fun})\\ \\texttt{42}:\\alpha_{1}$}\n" +
                "\\end{prooftree}", latex);
    }
}
