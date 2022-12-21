package Grammar;

public class Main {
    public static void main(String[] args) throws Exception {
        var grammar = new Grammar("src\\g2.txt");
        var parser = new ParserLR0(grammar);
        parser.printGotoTable();
        parser.printActionTable();
        parser.checkIfStringIsAcceptedByGrammar("i+c");
    }
}
