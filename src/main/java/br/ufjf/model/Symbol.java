package br.ufjf.model;

public class Symbol extends Base<Symbol> {
    final static public Symbol EMPTY = new Symbol(' ');

    private char symbol;

    public Symbol() {}

    public Symbol (char symbol) {
        this.symbol = symbol;
    }
    public Symbol (String symbol) {
        if (!symbol.isEmpty()) {
            this.symbol = symbol.toCharArray()[0];
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
