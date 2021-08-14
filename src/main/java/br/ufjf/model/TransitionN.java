package br.ufjf.model;

import br.ufjf.structure.Group;

import java.util.HashSet;

public class TransitionN extends Base<TransitionN> {
    private State start;
    private Group<State> finish;
    private Symbol symbol;

    public TransitionN() { }

    public TransitionN(State start, Group<State> finish, Symbol symbol) {
        this.start = start;
        this.finish = finish;
        this.symbol = symbol;
    }
    public TransitionN(String start, String[] finish, char symbol) {
        this.start = new State(start);
        var finishElements = new HashSet<State>();
        for (var el : finish) {
            finishElements.add(new State(el));
        }
        this.finish = new Group<>(finishElements);
        this.symbol = new Symbol(symbol);
    }

    public State getStart() {
        return start;
    }

    public void setStart(State start) {
        this.start = start;
    }

    public Group<State> getFinish() {
        return this.finish;
    }

    public void setFinish(Group<State> finish) {
        this.finish = finish;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public TransitionN clone() {
        TransitionN newTransition = new TransitionN(
                this.getStart(),
                this.getFinish(),
                this.getSymbol()
        );
        return newTransition;
    }

    @Override
    public boolean equals(Object obj) {
        return this.start.equals(((TransitionN) obj).getStart()) &&
                this.finish.equals(((TransitionN) obj).getFinish()) &&
                this.symbol.equals(((TransitionN) obj).getSymbol());
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
