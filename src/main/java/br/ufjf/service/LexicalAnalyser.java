package br.ufjf.service;

import br.ufjf.model.DFA;
import br.ufjf.model.NFA;
import br.ufjf.model.State;
import br.ufjf.model.Symbol;

import java.io.BufferedReader;
import java.io.Reader;

public class LexicalAnalyser {
    private final BufferedReader bufferedReader;
    private final DFA dfa;
    private State currentState;

    public LexicalAnalyser(DFA dfa, Reader reader) {
        this.dfa = dfa;
        this.currentState = dfa.getInitialState();
        this.bufferedReader = new BufferedReader(reader);
    }

    public LexicalAnalyser(NFA nfa, Reader reader) throws Exception {
        this.dfa = nfa.toDFA();
        this.currentState = dfa.getInitialState();
        this.bufferedReader = new BufferedReader(reader);
    }

    private Character readCharacter() throws Exception {
        var characterAsInt = this.bufferedReader.read();
        return characterAsInt != -1 ? (char) characterAsInt : null;
    }

    public String readToken() throws Exception {
        StringBuilder token = new StringBuilder();
        var readCharacter = this.readCharacter();
        while(readCharacter != null) {
            this.currentState = this.dfa.functionP(this.currentState, new Symbol(readCharacter));

            if (this.currentState == null) {
                throw new Exception("'" +token+"' is not a valid token.");
            } else if (this.dfa.getEndStates().contains(this.currentState)) {
                this.currentState = this.dfa.getInitialState();
                return token.toString();
            }
            token.append(readCharacter);

            readCharacter = this.readCharacter();
        }

        if (token.toString().length() > 1) {
            throw new Exception("'"+token+"' is not a valid token.");
        }
        return null;
    }

    public void readFile() throws Exception {
        String token = this.readToken();
        while (token != null) {
            System.out.println("Token: "+ token);
            token = this.readToken();
        }
        this.bufferedReader.close();
        System.out.println("--END--");
    }
}
