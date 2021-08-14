package br.ufjf.model;

import br.ufjf.structure.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model: NFA")
public class NFATestSuite {

    private NFA nfa;

    @BeforeEach
    public void setDefaultValue() {
        var symbols = new Group<Symbol>();
        var states = new Group<State>();
        var programFunction = new Group<TransitionN>();
        var initialState = new State("q1");
        var endStates = new Group<State>();

        symbols
                .insert(new Symbol('a'))
                .insert(new Symbol('b'));
        states
                .insert(new State("q1"))
                .insert(new State("q2"))
                .insert(new State("q3"));

        String[] array1 = { "q2" };
        programFunction.insert(new TransitionN("q1", array1,'a'));
        String[] array2 = { "q1", "q3" };
        programFunction.insert(new TransitionN("q2", array2,'b'));
        String[] array3 = { "q1" };
        programFunction.insert(new TransitionN("q3", array3,'a'));

        endStates.insert(new State("q1"));

        this.nfa = new NFA(symbols, states, programFunction, initialState, endStates);
    }

    @Test
    @DisplayName("Should print NFA")
    public void shouldToStringValue() {
        assertEquals(
                "( { a, b }, { q1, q2, q3 }, { ( q1, a, { q2 } ), ( q2, b, { q1, q3 } ), ( q3, a, { q1 } ) }, q1, { q1 } )",
                this.nfa.toString());
    }

    @Test
    @DisplayName("Should return true on equal NFA comparison")
    public void shouldReturnTrueOnEqualsTo() {
        var equalNfa = this.nfa.clone();

        assertTrue(this.nfa.equals(equalNfa));
    }

    @Test
    @DisplayName("Should return false on different NFA comparison")
    public void shouldReturnFalseOnEqualsTo() {
        var differentNFA = this.nfa.clone();
        differentNFA.setInitialState(new State("q2"));

        assertFalse(this.nfa.equals(differentNFA));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q1", "q2", "q3"})
    @DisplayName("Should successfully insert valid initial state")
    public void shouldInsertValidInitialState(String value) {
        assertTrue(this.nfa.setInitialState(new State(value)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q4", "", "Q1", "q"})
    @DisplayName("Should not insert invalid initial state")
    public void shouldNotInsertInvalidInitialState(String value) {
        assertFalse(this.nfa.setInitialState(new State(value)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q1", "q2", "q3"})
    @DisplayName("Should successfully insert valid end state")
    public void shouldInsertValidEndState(String value) {
        var endStates = this.nfa.getEndStates();
        endStates.insert(new State(value));
        assertTrue(this.nfa.setEndStates(endStates));
    }

    @ParameterizedTest
    @ValueSource(strings = {"q4", "", "Q1", "q"})
    @DisplayName("Should not insert invalid end state")
    public void shouldNotInsertInvalidEndState(String value) {
        var endStates = this.nfa.getEndStates();
        endStates.insert(new State(value));
        assertFalse(this.nfa.setEndStates(endStates));
    }

    @Test
    @DisplayName("Should successfully insert valid transition")
    public void shouldInsertValidTransition() {
        var start = "q3";
        String[] finish = { "q1", "q2" };
        var symbol = 'a';

        assertTrue(this.nfa.addTransitionN(new TransitionN(start, finish, symbol)));
    }

    @Test
    @DisplayName("Should not insert invalid transition")
    public void shouldNotInsertInvalidTransition() {
        var start = "q3";
        String[] finish = { "q1", "q4" };
        var symbol = 'a';

        assertTrue(this.nfa.addTransitionN(new TransitionN(start, finish, symbol)));
    }

    @ParameterizedTest
    @CsvSource(value = {"q1;a;{ q2 }", "q2;b;{ q1, q3 }"}, delimiter = ';')
    @DisplayName("should return valid state in P function")
    public void shouldReturnValidStateInFunctionP(String start, char symbol, String expectedReturnState) {
        assertEquals(
                expectedReturnState,
                this.nfa.functionP(new State(start), new Symbol(symbol)).toString()
        );
    }

    @ParameterizedTest
    @CsvSource({"q0,b", "q0,c", "q2,d"})
    @DisplayName("should return null in P function")
    public void shouldReturnNullInFunctionP(String start, char symbol) {
        assertNull(
                this.nfa.functionP(new State(start), new Symbol(symbol))
        );
    }

    @Test
    @DisplayName("should return array with 2 states in Pe function")
    public void shouldReturnValidStateInFunctionPe() {
        var word = "ab";
        String[] start = { "q1" };
        var startSet = new HashSet<State>();
        for (var el: start) {
            startSet.add(new State(el));
        }
        assertEquals(
                2,
                this.nfa.functionPe(new Group<>(startSet), word).getElements().size()
        );
    }

    @Test
    @DisplayName("should return array with 0 states in Pe function")
    public void shouldReturnEmptyInFunctionPe() {
        var word = "ba";
        String[] start = { "q3" };
        var startSet = new HashSet<State>();
        for (var el: start) {
            startSet.add(new State(el));
        }
        assertEquals(
                0,
                this.nfa.functionPe(new Group<>(startSet), word).getElements().size()
        );
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "ab", "aba"})
    @DisplayName("should accept word")
    public void shouldAcceptWord(String word) {
        assertTrue(this.nfa.accepts(word));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aa", "baac", "abaaaaad"})
    @DisplayName("should reject word")
    public void shouldRejectWord(String word) {
        assertFalse(this.nfa.accepts(word));
    }
}
