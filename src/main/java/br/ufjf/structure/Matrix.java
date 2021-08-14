package br.ufjf.structure;

import br.ufjf.model.Base;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Matrix<T extends Base<T>> {
    private final Set<Group<T>> elements;

    public Matrix() {
        this.elements = new LinkedHashSet<Group<T>>();
    }
    public Matrix(Collection<Group<T>> elements) {
        this.elements = new LinkedHashSet<Group<T>>(elements);
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    public Matrix<T> clear() {
        this.elements.clear();
        return this;
    }

    public Matrix<T> insert(Group<T> element) {
        this.elements.add(element);
        return this;
    }

    public boolean contains(Group<T> element) {
        return this.elements.contains(element);
    }

    public Group<T> findElement(Group<T> element) {
        if(element == null) {
            return null;
        }

        for (Group<T> currentElement : this.elements) {
            if (currentElement.equals(element)) {
                return currentElement;
            }
        }

        return null;
    }

    public Matrix<T> union(Matrix<T> matrix) {
        Set<Group<T>> resultSet = new LinkedHashSet<Group<T>>(this.elements);
        resultSet.addAll(matrix.getElements());
        return new Matrix<T>(resultSet);
    }

    public Matrix<T> intersection(Matrix<T> matrix) {
        Set<Group<T>> resultSet = new LinkedHashSet<Group<T>>();
        for (var thisItem : this.elements) {
            for (var item : matrix.getElements()) {
                if (thisItem.equals(item)) {
                    resultSet.add(item);
                }
            }
        }
        return new Matrix<T>(resultSet);
    }

    public Matrix<T> duplicate() {
        return new Matrix<T>(this.elements);
    }

    public boolean equals(Matrix<T> matrix) {
        return this.getElements().equals(matrix.getElements());
    }

    public Collection<Group<T>> getElements() {
        return this.elements;
    }

    public Iterator<Group<T>> getIterator() {
        return this.elements.iterator();
    }

    public boolean removeElement(Group<T> element) {
        return this.elements.remove(element);
    }

    public String toString() {
        String groupString = "{ ";
        for (Group<T> currentGroup : this.elements) {
            groupString += "( ";
            for (T currentElement : currentGroup.getElements()) {
                groupString += currentElement.toString()+", ";
            }
            groupString = groupString.substring(0, groupString.length() - 2);
            groupString += " ),";
        }
        groupString = groupString.substring(0, groupString.length() - 1);
        groupString += " }";
        return groupString;
    }
}
