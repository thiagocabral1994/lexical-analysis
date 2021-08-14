package br.ufjf.model;

import br.ufjf.structure.Group;
import br.ufjf.structure.Matrix;

import java.util.Collection;
import java.util.Iterator;

public class NFA extends Base<NFA> {
    private Group<Symbol> symbols;
    private Group<State> states;
    private Group<State> endStates;
    private Group<TransitionN> programFunction;
    private State initialState;

    public NFA() { }
    public NFA(
            Group<Symbol> symbols,
            Group<State> states,
            Group<TransitionN> programFunction,
            State initialState,
            Group<State> endStates
    ) {
        this.symbols = symbols;
        this.states = states;
        this.programFunction = programFunction;
        this.initialState = initialState;
        this.endStates = endStates;
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

    public Group<TransitionN> getProgramFunction() {
        return programFunction;
    }

    public boolean setProgramFunction(Group<TransitionN> programFunction) {
        var temp = this.programFunction;
        try {
            this.programFunction.clear();
            for (var el: programFunction.getElements()) {
                this.addTransitionN(el);
            }
            return true;
        } catch (Exception err) {
            this.programFunction = temp;
            return false;
        }
    }

    public boolean addTransitionN(TransitionN transitionN) {
        if(
                !this.states.contains(transitionN.getStart()) ||
                        !this.symbols.contains(transitionN.getSymbol())
        ) {

            return false;
        }

        for (var transition: this.programFunction.getElements()) {
            if (transition.getStart().equals(transitionN.getStart()) &&
                    transition.getSymbol().equals(transitionN.getSymbol())
            ) {
                transition.setFinish(transition.getFinish().union(transitionN.getFinish()));
                return true;
            }
        }
        this.programFunction.insert(transitionN);
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

    public NFA clone() {
        return new NFA(
                this.symbols,
                this.states,
                this.programFunction,
                this.initialState,
                this.endStates
        );
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NFA &&
                this.symbols.equals(((NFA) obj).getSymbols()) &&
                this.states.equals(((NFA) obj).getStates()) &&
                this.endStates.equals(((NFA) obj).getEndStates()) &&
                this.programFunction.equals(((NFA) obj).getProgramFunction()) &&
                this.initialState.equals(((NFA) obj).getInitialState());
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

    public Group<State> functionP(State start, Symbol symbol) {
        if (start == null) {
            return null;
        }

        Group<TransitionN> transitionSet = this.getProgramFunction();
        for(TransitionN transition : transitionSet.getElements()) {
            if(
                    transition.getStart().equals(start) &&
                            transition.getSymbol().equals(symbol)
            ) {
                return transition.getFinish();
            }
        }
        return null;
    }


    public Group<State> functionPe(Group<State> startGroup, String word) {
        if (word.isEmpty()) {
            return startGroup;
        }

        Group<State> newStartGroup = new Group<>();
        Symbol symbol = new Symbol(word.charAt(0));
        for (State start : startGroup.getElements()) {
            newStartGroup = newStartGroup.union(this.functionP(start, symbol));
        }
        return this.functionPe(newStartGroup, word.substring(1));
    }

    public boolean accepts(String word) {
        Group<State> initialStateGroup = new Group<State>();
        initialStateGroup.insert(this.getInitialState());
        Group<State> endStateGroup = this.getEndStates();

        Collection<State> states = this.functionPe(initialStateGroup, word).getElements();
        for (State state : states) {
            if (endStateGroup.contains(state)) {
                return true;
            }
        }
        return false;
    }

    public DFA toDFA() throws Exception {
        Group<Symbol> newSymbolGroup = this.getSymbols().duplicate();
        Group<State> newStateGroup = new Group<>();
        Group<TransitionD> newProgramFunctionGroup = new Group<>();

        State newStartState = new State("< " + this.getInitialState().toString() + " >");
        Group<State> newEndStateGroup = new Group<>();
        State newState;
        TransitionD newTransition;

        Group<State> currentEndStates = this.getEndStates();

        Matrix<State> currentMatrix = new Matrix<>();
        Group<State> currentGroup = new Group<>();
        State currentState = this.getInitialState().clone();
        Symbol currentSymbol;

        Group<State> tempGroupState;

        currentGroup.insert(currentState);
        currentMatrix.insert(currentGroup);

        if (currentEndStates.contains(currentState))
            newEndStateGroup.insert(newStartState);

        newStateGroup.insert(newStartState);

        Iterator<Group<State>> matrixIterator = currentMatrix.getIterator();
        while (matrixIterator.hasNext()) {
            currentGroup = matrixIterator.next();
            currentMatrix.removeElement(currentGroup);

            boolean isAtEndState = false;

            for (Iterator<Symbol> symbolIterator = newSymbolGroup.getIterator(); symbolIterator.hasNext();) {
                currentSymbol = symbolIterator.next();

                tempGroupState = new Group<>();
                for (Iterator<State> stateIterator = currentGroup.getIterator(); stateIterator.hasNext();) {
                    currentState = stateIterator.next();
                    tempGroupState = tempGroupState.union(functionP(currentState, currentSymbol));

                }

                if (!tempGroupState.isEmpty()) {
                    String newName = tempGroupState.toString();
                    newName = newName.substring(1, newName.length() - 1);
                    newState = new State("<" + newName + ">");

                    for (Iterator<State> endStateIterator = tempGroupState.getIterator(); endStateIterator
                            .hasNext();) {
                        if (currentEndStates.contains(endStateIterator.next()))
                            isAtEndState = true;
                    }

                    if (!newStateGroup.contains(newStateGroup.findElement(newState))) {

                        newStateGroup.insert(newState);

                        if (isAtEndState) {
                            newEndStateGroup.insert(newState);
                            isAtEndState = false;
                        }

                        newTransition = new TransitionD();
                        String nameOrigin = currentGroup.toString();
                        nameOrigin = "<"
                                + nameOrigin.substring(1,
                                nameOrigin.length() - 1) + ">";
                        State newOrigin = newStateGroup.findElement(new State(
                                nameOrigin));
                        newTransition.setStart(newOrigin);
                        newTransition.setSymbol(currentSymbol);
                        newTransition.setFinish(newState);

                        newProgramFunctionGroup.insert(newTransition.clone());

                        currentMatrix.insert(tempGroupState);
                    } else {
                        newTransition = new TransitionD();
                        String nameOrigin = currentGroup.toString();
                        nameOrigin = "<"
                                + nameOrigin.substring(1,
                                nameOrigin.length() - 1) + ">";
                        State newOrigin = newStateGroup.findElement(new State(
                                nameOrigin));
                        newTransition.setStart(newOrigin);
                        newTransition.setSymbol(currentSymbol);
                        newTransition.setFinish(newState);

                        newProgramFunctionGroup.insert(newTransition.clone());
                    }
                }
            }
            matrixIterator = currentMatrix.getIterator();
        }

        DFA newDFA = new DFA(newSymbolGroup, newStateGroup, newProgramFunctionGroup, newStartState, newEndStateGroup);
        return newDFA;
    }
}
