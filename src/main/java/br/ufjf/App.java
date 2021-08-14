package br.ufjf;

import br.ufjf.model.DFA;
import br.ufjf.service.LexicalAnalyser;
import br.ufjf.service.XmlReader;

import java.io.FileReader;

public class App {

    public static void main( String[] args ) throws Exception {
        System.out.println(args[0]);
        DFA dfa = XmlReader.xmlToDFA(args[0]);
        LexicalAnalyser la = new LexicalAnalyser(dfa, new FileReader(args[1]));
        la.readFile();
    }
}
