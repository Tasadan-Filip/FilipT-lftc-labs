package Tests;

import FA.FA;
import Grammar.Grammar;

public class TestGrammar {
    public static void main(String[] args) throws Exception {
        Grammar testGrammar = new Grammar("src\\g1.txt");
        testGrammar.DisplayElementMain();
    }
}
