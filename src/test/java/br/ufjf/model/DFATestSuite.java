package br.ufjf.model;

import br.ufjf.structure.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model: DFA")
public class DFATestSuite {

    private DFA dfa;

    @BeforeEach
    public void setDefaultValue() {
        var symbols = new Group<Symbol>();
        var states = new Group<State>();
        var programFunction = new Group<TransitionD>();
        var initialState = new State("q0");
        var endStates = new Group<State>();

        symbols
                .insert(new Symbol('a'))
                .insert(new Symbol('b'))
                .insert(new Symbol('c'));
        states
                .insert(new State("q0"))
                .insert(new State("q1"))
                .insert(new State("q2"))
                .insert(new State("qf"));

        programFunction
                .insert(new TransitionD("q0","q1", 'a'))
                .insert(new TransitionD("q1","q2", 'b'))
                .insert(new TransitionD("q2","q2", 'a'))
                .insert(new TransitionD("q2","qf", 'c'));

        endStates.insert(new State("qf"));

        this.dfa = new DFA(symbols, states, programFunction, initialState, endStates);
    }

    @Test
    @DisplayName("Should print DFA")
    public void shouldToStringValue() {
        assertEquals(
                "( { a, b, c }, { q0, q1, q2, qf }, { ( q0, a, q1 ), ( q1, b, q2 ), ( q2, a, q2 ), ( q2, c, qf ) }, q0, { qf } )",
                this.dfa.toString());
    }

    @Test
    @DisplayName("Should return true on equal DFA comparison")
    public void shouldReturnTrueOnEqualsTo() {
        var equalDfa = this.dfa.clone();

        assertTrue(this.dfa.equals(equalDfa));
    }

    @Test
    @DisplayName("Should return false on different DFA comparison")
    public void shouldReturnFalseOnEqualsTo() {
        var differentDFA = this.dfa.clone();
        differentDFA.setInitialState(new State("qf"));

        assertFalse(this.dfa.equals(differentDFA));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q0", "q1", "q2", "qf"})
    @DisplayName("Should successfully insert valid initial state")
    public void shouldInsertValidInitialState(String value) {
        assertTrue(this.dfa.setInitialState(new State(value)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q3", "", "Q1", "q"})
    @DisplayName("Should not insert invalid initial state")
    public void shouldNotInsertInvalidInitialState(String value) {
        assertFalse(this.dfa.setInitialState(new State(value)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q0", "q1", "q2", "qf"})
    @DisplayName("Should successfully insert valid end state")
    public void shouldInsertValidEndState(String value) {
        var endStates = this.dfa.getEndStates();
        endStates.insert(new State(value));
        assertTrue(this.dfa.setEndStates(endStates));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q3", "", "Q1", "q"})
    @DisplayName("Should not insert invalid end state")
    public void shouldNotInsertInvalidEndState(String value) {
        var endStates = this.dfa.getEndStates();
        endStates.insert(new State(value));
        assertFalse(this.dfa.setEndStates(endStates));
    }

    @ParameterizedTest
    @CsvSource({"qf,a,q0", "qf,b,q0", "qf,c,q2"})
    @DisplayName("Should successfully insert valid transition")
    public void shouldInsertValidTransition(String start, char symbol, String finish) {
        var transitionDGroup = this.dfa.getProgramFunction();
        transitionDGroup.insert(new TransitionD(start, finish, symbol));
        assertTrue(this.dfa.setProgramFunction(transitionDGroup));
    }

    @ParameterizedTest
    @CsvSource({"q3,a,q0", "q1,b,q4", "q2,T,q1"})
    @DisplayName("Should not insert invalid transition")
    public void shouldNotInsertInvalidTransition(String start, char symbol, String finish) {
        var transitionDGroup = this.dfa.getProgramFunction();
        transitionDGroup.insert(new TransitionD(start, finish, symbol));
        assertFalse(this.dfa.setProgramFunction(transitionDGroup));
    }

    @ParameterizedTest
    @CsvSource({"q0,a,q1", "q1,b,q2", "q2,a,q2"})
    @DisplayName("should return valid state in P function")
    public void shouldReturnValidStateInFunctionP(String start, char symbol, String expectedReturnState) {
        assertEquals(
                expectedReturnState,
                this.dfa.functionP(new State(start), new Symbol(symbol)).toString()
        );
    }

    @ParameterizedTest
    @CsvSource({"q0,b", "q0,c", "q2,b"})
    @DisplayName("should return null in P function")
    public void shouldReturnNullInFunctionP(String start, char symbol) {
        assertNull(
                this.dfa.functionP(new State(start), new Symbol(symbol))
        );
    }

    @ParameterizedTest
    @CsvSource({"q0,abaaa,q2", "q1,baaa,q2", "q2,aaaa,q2"})
    @DisplayName("should return valid state in Pe function")
    public void shouldReturnValidStateInFunctionPe(String start, String word, String expectedReturnState) {
        assertEquals(
                expectedReturnState,
                this.dfa.functionPe(new State(start), word).toString()
        );
    }

    @ParameterizedTest
    @CsvSource({"q0,baaaa", "q0,caaaa", "q2,baaa"})
    @DisplayName("should return null in Pe function")
    public void shouldReturnNullInFunctionPe(String start, String word) {
        assertNull(
                this.dfa.functionPe(new State(start), word)
        );
    }


    @ParameterizedTest
    @ValueSource(strings = {"abac", "abaac", "abaaaaac"})
    @DisplayName("should accept word")
    public void shouldAcceptWord(String word) {
        assertTrue(this.dfa.accepts(word));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aba", "baac", "abaaaaad"})
    @DisplayName("should reject word")
    public void shouldRejectWord(String word) {
        assertFalse(this.dfa.accepts(word));
    }
}
