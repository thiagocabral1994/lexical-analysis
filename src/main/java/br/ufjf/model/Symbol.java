package br.ufjf.model;

import javax.swing.*;

public class Symbol extends Base<Symbol> {
    final static public Symbol EMPTY = new Symbol(' ');

    private char symbol;

    public Symbol() {}

    public Symbol (char symbol) {
        this.symbol = symbol;
    }
    public Symbol (String symbol) {
        if (!symbol.isEmpty()) {
            if (symbol.compareTo("\\n") == 0) {
                var newChar = symbol.replace("\\n","\n");
                this.symbol = newChar.charAt(0);
            } else if (symbol.compareTo("\\t") == 0){
                var newChar = symbol.replace("\\t","\t");
                this.symbol = newChar.charAt(0);
            }else {
                this.symbol = symbol.charAt(0);
            }
        }
    }

    public char getSymbol() {
        return this.symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public Symbol clone() {
        return new Symbol(this.getSymbol());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Symbol && this.getSymbol() == ((Symbol) obj).getSymbol();
    }

    @Override
    public String toString() {
        String symbolString = "";
        symbolString += this.getSymbol();
        return symbolString;
    }
}
