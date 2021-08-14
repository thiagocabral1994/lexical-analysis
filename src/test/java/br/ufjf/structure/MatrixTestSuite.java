package br.ufjf.structure;

import br.ufjf.model.*;
import br.ufjf.service.LexicalAnalyser;
import br.ufjf.service.XmlReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixTestSuite {

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @ValueSource(classes = {
            State.class,
            Symbol.class,
            TransitionN.class,
            TransitionD.class,
            DFA.class,
            NFA.class,
    })
    @DisplayName("Should be instance of Base")
    public <T extends Base<T>> void shouldBeAbleToInsert(Class<T> clazz) {
        assertDoesNotThrow(() -> {
            Constructor<?> constructor = clazz.getConstructor();
            Object object = constructor.newInstance();
            var group = new Group<T>();
            var matrix = new Matrix<T>();

            group.insert((T) object);
            matrix.insert(group);
        });
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @ValueSource(classes = {
            Integer.class,
            String.class,
            Group.class,
            Matrix.class,
            LexicalAnalyser.class,
            XmlReader.class
    })
    @DisplayName("Should not insert objects other than Base")
    public <T extends Base<T>> void shouldNotBeAbleToInsert(Class<T> clazz) {
        assertThrows(Exception.class,() -> {
            Constructor<?> constructor = clazz.getConstructor();
            Object object = constructor.newInstance();
            var group = new Group<T>();
            var matrix = new Matrix<T>();

            group.insert((T) object);
            matrix.insert(group);
        });
    }

    @Test
    @DisplayName("Should contain group")
    public void shouldContainGroup() {
        var state = new State("q1");
        var group = new Group<State>();
        var matrix = new Matrix<State>();
        group.insert(state);
        matrix.insert(group);

        assertTrue(matrix.contains(group));
    }

    @Test
    @DisplayName("Should find group")
    public void shouldFindGroup() {
        var state = new State("q1");
        var group = new Group<State>();
        var matrix = new Matrix<State>();
        group.insert(state);
        matrix.insert(group);

        assertEquals(group, matrix.findElement(group));
    }

    @Test
    @DisplayName("Should union matrix")
    public void shouldUnionMatrix() {
        var state1 = new State("q1");
        var state2 = new State("q2");

        var group1 = new Group<State>();
        var group2 = new Group<State>();

        var matrix1 = new Matrix<State>();
        var matrix2 = new Matrix<State>();

        group1.insert(state1);
        group2.insert(state2);

        matrix1.insert(group1);
        matrix2.insert(group2);

        var resultMatrix = matrix1.union(matrix2);

        assertTrue(resultMatrix.contains(group1));
        assertTrue(resultMatrix.contains(group2));
    }

    @Test
    @DisplayName("Should find intersection of groups")
    public void shouldIntersectGroups() {
        var state11 = new State("q1");
        var state12 = new State("q2");
        var state21 = new State("q1");
        var state22 = new State("q3");

        var group1 = new Group<State>();
        var group2 = new Group<State>();

        var matrix1 = new Matrix<State>();
        var matrix2 = new Matrix<State>();

        group1.insert(state11).insert(state12);
        group2.insert(state21).insert(state22);

        matrix1.insert(group1).insert(group2);
        matrix2.insert(group2);

        var resultMatrix = matrix1.intersection(matrix2);

        assertTrue(resultMatrix.contains(group2));
        assertFalse(resultMatrix.contains(group1));
    }

    @Test
    @DisplayName("Should print group")
    public void shouldToString() {
        var state1 = new State("q1");
        var state2 = new State("q2");

        var group = new Group<State>();
        var matrix = new Matrix<State>();

        group.insert(state1).insert(state2);
        matrix.insert(group);

        assertEquals("{ ( q1, q2 ) }", matrix.toString());
    }
}
