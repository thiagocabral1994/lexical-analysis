package br.ufjf.model;

import br.ufjf.structure.Group;

public class DFA extends Base<DFA> {
    private Group<Symbol> symbols;
    private Group<State> states;
    private Group<State> endStates;
    private Group<TransitionD> programFunction;
    private State initialState;

    public DFA() { }
    public DFA(
            Group<Symbol> symbols,
            Group<State> states,
            Group<TransitionD> programFunction,
            State initialState,
            Group<State> endStates
            ) {
        this.symbols = symbols;
        this.states = states;
        this.setEndStates(endStates);
        this.setProgramFunction(programFunction);
        this.setInitialState(initialState);
    }

    public Group<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(Group<Symbol> symbols) {
        this.symbols = symbols;
    }

    public Group<State> getStates() {
        return states;
    }

    public void setStates(Group<State> states) {
        this.states = states;
    }

    public Group<State> getEndStates() {
        return endStates;
    }

    public boolean setEndStates(Group<State> endStates) {
        for (var endState: endStates.getElements()) {
            if(!this.states.contains(endState)) {
                return false;
            }
        }
        this.endStates = endStates;
        return true;
    }

    public Group<TransitionD> getProgramFunction() {
        return programFunction;
    }

    public boolean setProgramFunction(Group<TransitionD> programFunction) {
        for (var transition: programFunction.getElements()) {
            if(
                    !this.states.contains(transition.getStart()) ||
                    !this.states.contains(transition.getFinish()) ||
                    !this.symbols.contains(transition.getSymbol())
            ) {
                return false;
            }

            for (var transition2: programFunction.getElements()) {
                if (    !transition.equals(transition2) &&
                        transition.getStart().equals(transition2.getStart()) &&
                        transition.getSymbol().equals(transition2.getSymbol())) {
                    return false;
                }
            }
        }
        this.programFunction = programFunction;
        return true;
    }

    public State getInitialState() {
        return initialState;
    }

    public boolean setInitialState(State initialState) {
        if(!this.states.contains(initialState)) {
            return false;
        }
        this.initialState = initialState;
        return true;
    }

    public DFA clone() {
        return new DFA(
                this.symbols,
                this.states,
                this.programFunction,
                this.initialState,
                this.endStates
        );
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DFA &&
                this.symbols.equals(((DFA) obj).getSymbols()) &&
                this.states.equals(((DFA) obj).getStates()) &&
                this.endStates.equals(((DFA) obj).getEndStates()) &&
                this.programFunction.equals(((DFA) obj).getProgramFunction()) &&
                this.initialState.equals(((DFA) obj).getInitialState());
    }

    @Override
    public String toString() {
        return "( " +
                this.symbols.toString() + ", " +
                this.states.toString() + ", " +
                this.programFunction.toString() + ", " +
                this.initialState.toString() + ", " +
                this.endStates.toString() +
                " )";
    }

    public State functionP(State start, Symbol symbol) {
        if (start == null) {
            return null;
        }

        Group<TransitionD> transitionSet = this.getProgramFunction();
        for(TransitionD transition : transitionSet.getElements()) {
            if(
                    transition.getStart().equals(start) &&
                            transition.getSymbol().equals(symbol)
            ) {
                return transition.getFinish();
            }
        }
        return null;
    }

    public State functionPe(State start, String word) {
        State currentState = start;
        Symbol symbol;
        int i = 0;
        while (i < word.length()) {
            symbol = new Symbol(word.charAt(i));
            currentState = this.functionP(currentState, symbol);
            if (currentState == null) {
                return null;
            }
            i++;
        }
        return currentState;
    }

    public boolean accepts(String word) {
        State resultState = this.functionPe(this.getInitialState(), word);
        return getEndStates().contains(resultState);
    }

    public void clear() {
        this.symbols.clear();
        this.states.clear();
        this.programFunction.clear();
        this.endStates.clear();
    }
}
