package br.ufjf.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model: TransitionD")
public class TransitionDTestSuite {

    private TransitionD transitionD;

    @BeforeEach
    public void setDefaultValue() {
        var start = new State("q0");
        var finish = new State("qf");
        var symbol = new Symbol('a');

        this.transitionD = new TransitionD(start, finish, symbol);
    }

    @Test
    @DisplayName("Should print transition transaction")
    public void shouldToStringValue() {
        assertEquals("( q0, a, qf )", this.transitionD.toString());
    }

    @Test
    @DisplayName("Should return true on equal transitions comparison")
    public void shouldReturnTrueOnEqualsTo() {
        var equalTransition = this.transitionD.clone();

        assertTrue(this.transitionD.equals(equalTransition));
    }

    @Test
    @DisplayName("Should return false on different transitions comparison")
    public void shouldReturnFalseOnEqualsTo() {
        var differentTransaction = this.transitionD.clone();
        differentTransaction.setFinish(new State("different value"));

        assertFalse(this.transitionD.equals(differentTransaction));
    }
}
