package br.ufjf.model;

import br.ufjf.structure.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model: TransitionN")
public class TransitionNTestSuite {

    private TransitionN transitionN;

    @BeforeEach
    public void setDefaultValue() {
        var start = new State("q0");
        var finishGroup = new Group<State>();
        finishGroup.insert(new State("q1")).insert(new State("q2"));
        var symbol = new Symbol('a');

        this.transitionN = new TransitionN(start, finishGroup, symbol);
    }

    @Test
    @DisplayName("Should print transition transaction")
    public void shouldToStringValue() {
        assertEquals("( q0, a, { q1, q2 } )", this.transitionN.toString());
    }

    @Test
    @DisplayName("Should return true on equal transitions comparison")
    public void shouldReturnTrueOnEqualsTo() {
        var equalTransition = this.transitionN.clone();

        assertTrue(this.transitionN.equals(equalTransition));
    }

    @Test
    @DisplayName("Should return false on different transitions comparison")
    public void shouldReturnFalseOnEqualsTo() {
        var differentTransaction = this.transitionN.clone();
        differentTransaction.setStart(new State("different value"));

        assertFalse(this.transitionN.equals(differentTransaction));
    }
}
