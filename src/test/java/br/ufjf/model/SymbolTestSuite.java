package br.ufjf.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model: Symbol")
public class SymbolTestSuite {

    private Symbol symbol;

    @BeforeEach
    public void setDefaultValue() {
        this.symbol = new Symbol('a');
    }

    @ParameterizedTest
    @ValueSource(chars = {'a', 'b', ' '})
    @DisplayName("Should print input value")
    public void shouldToStringCharValue(char input) {
        var symbol = new Symbol(input);

        assertEquals(String.valueOf(input), symbol.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"value", "double-value"})
    @DisplayName("Should print input value")
    public void shouldToStringFirstCharValue(String input) {
        var symbol = new Symbol(input);

        assertEquals(input.substring(0,1), symbol.toString());
    }

    @Test
    @DisplayName("Should return true on equal symbols comparison")
    public void shouldReturnTrueOnEqualsTo() {
        var equalSymbol = this.symbol.clone();

        assertTrue(this.symbol.equals(equalSymbol));
    }

    @Test
    @DisplayName("Should return false on different symbols comparison")
    public void shouldReturnFalseOnEqualsTo() {
        var differentSymbol = new Symbol('t');

        assertFalse(this.symbol.equals(differentSymbol));
    }
}
