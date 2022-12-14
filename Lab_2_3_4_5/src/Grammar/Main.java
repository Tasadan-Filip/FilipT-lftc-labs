package Grammar;

public class Main {
    public static void main(String[] args) throws Exception {
        var grammar = new Grammar("D:\\FilipT-lftc-labs\\Lab_2_3_4_5\\src\\g1.txt");
        var parser = new ParserLR0(grammar);
        parser.printGotoTable();
        //  parser.printActionTable();
    }
}
