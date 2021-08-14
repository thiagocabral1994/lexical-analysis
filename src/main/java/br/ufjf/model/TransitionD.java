package br.ufjf.model;

public class TransitionD extends Base<TransitionD>{
    private State start;
    private State finish;
    private Symbol symbol;

    public TransitionD() {}

    public TransitionD(State start, State finish, Symbol symbol) {
        this.start = start;
        this.finish = finish;
        this.symbol = symbol;
    }

    public TransitionD(String start, String finish, char symbol) {
        this.start = new State(start);
        this.finish = new State(finish);
        this.symbol = new Symbol(symbol);
    }

    public State getStart() {
        return start;
    }

    public void setStart(State start) {
        this.start = start;
    }

    public State getFinish() {
        return finish;
    }

    public void setFinish(State finish) {
        this.finish = finish;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public TransitionD clone() {
        TransitionD newTransition = new TransitionD(
                this.getStart(),
                this.getFinish(),
                this.getSymbol()
        );
        return newTransition;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TransitionD &&
                this.start.equals(((TransitionD) obj).getStart()) &&
                this.finish.equals(((TransitionD) obj).getFinish()) &&
                this.symbol.equals(((TransitionD) obj).getSymbol());
    }

    @Override
    public String toString() {
        return "( " +
                this.getStart().toString() + ", " +
                this.getSymbol().toString() + ", " +
                this.getFinish().toString() +
                " )";
    }
}
