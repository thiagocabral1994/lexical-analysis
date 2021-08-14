package br.ufjf;

import br.ufjf.model.DFA;
import br.ufjf.service.LexicalAnalyser;
import br.ufjf.service.XmlReader;

import java.io.FileReader;

public class App {

    public static void main( String[] args ) throws Exception {
        DFA dfa = XmlReader.xmlToDFA("afd.xml");
        LexicalAnalyser la = new LexicalAnalyser(dfa, new FileReader("teste.txt"));
        la.readFile();
    }
}
