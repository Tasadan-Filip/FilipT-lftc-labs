package Grammar;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws Exception {
        PrintStream out = new PrintStream(
                new FileOutputStream("result.txt", false), true);
        System.setOut(out);
//        {
//            var grammar = new Grammar("src\\g1.txt");
//            var parser = new ParserLR0(grammar);
//            parser.printGotoTable();
//            parser.printActionTable();
//            parser.checkIfStringIsAcceptedByGrammar("abbc");
//        }
        {
            var grammar = new Grammar("src\\g5.txt");
            var parser = new ParserLR0(grammar);
            parser.printGotoTable();
            parser.printActionTable();
            parser.checkIfStringIsAcceptedByGrammar("i-a");
        }
    }
}
