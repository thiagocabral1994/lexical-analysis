package br.ufjf.model;

import br.ufjf.structure.Group;
import br.ufjf.structure.Matrix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model: Base")
public class BaseTestSuite {

    @ParameterizedTest
    @ValueSource(classes = {
            State.class,
            Symbol.class,
            TransitionN.class,
            TransitionD.class,
            DFA.class,
            NFA.class
    })
    @DisplayName("Should be instance of Base")
    public void shouldBeInstanceOfBase(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getConstructor();
        Object object = constructor.newInstance();

        assertTrue(object instanceof Base<?>);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            Group.class,
            Matrix.class
    })
    @DisplayName("Should not be instance of Base")
    public void shouldNotBeInstanceOfBase(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getConstructor();
        Object object = constructor.newInstance();

        assertFalse(object instanceof Base<?>);
    }
}
