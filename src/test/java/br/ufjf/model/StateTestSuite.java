package br.ufjf.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model: State")
public class StateTestSuite {

    private State state;

    @BeforeEach
    public void setDefaultValue() {
        this.state = new State("value");
    }

    @ParameterizedTest
    @ValueSource(strings = {"value", "two words", ""})
    @DisplayName("Should print input value")
    public void shouldToStringInputValue(String input) {
        var state = new State(input);

        assertEquals(input, state.toString());
    }

    @Test
    @DisplayName("Should return true on equal states comparison")
    public void shouldReturnTrueOnEqualsTo() {
        var equalState = this.state.clone();

        assertTrue(this.state.equals(equalState));
    }

    @Test
    @DisplayName("Should return false on different states comparison")
    public void shouldReturnFalseOnEqualsTo() {
        var differentState = new State("another value");

        assertFalse(this.state.equals(differentState));
    }
}
