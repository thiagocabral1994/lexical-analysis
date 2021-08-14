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

@DisplayName("Structure: Group")
public class GroupTestSuite {

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

            group.insert((T) object);
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

            group.insert((T) object);
        });
    }

    @Test
    @DisplayName("Should contain object")
    public void shouldContainObject() {
        var state = new State();
        var group = new Group<State>();
        group.insert(state);

        assertTrue(group.contains(state));
    }

    @Test
    @DisplayName("Should find object")
    public void shouldFindObject() {
        var state = new State("q1");
        var group = new Group<State>();
        group.insert(state);

        assertEquals(state, group.findElement(state));
    }

    @Test
    @DisplayName("Should union groups")
    public void shouldUnionGroups() {
        var state1 = new State("q1");
        var state2 = new State("q2");

        var group1 = new Group<State>();
        var group2 = new Group<State>();

        group1.insert(state1);
        group2.insert(state2);

        var resultGroup = group1.union(group2);

        assertTrue(resultGroup.contains(state1));
        assertTrue(resultGroup.contains(state2));
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

        group1.insert(state11).insert(state12);
        group2.insert(state21).insert(state22);

        var resultGroup = group1.intersection(group2);

        assertTrue(resultGroup.contains(state11));
        assertFalse(resultGroup.contains(state12));
        assertFalse(resultGroup.contains(state22));
    }

    @Test
    @DisplayName("Should print group")
    public void shouldToString() {
        var state1 = new State("q1");
        var state2 = new State("q2");

        var group = new Group<State>();

        group.insert(state1).insert(state2);

        assertEquals("{ q1, q2 }", group.toString());
    }
}
