package br.ufjf.structure;

import br.ufjf.model.Base;

import java.util.*;

public class Group<T extends Base<T>> {
    private final Set<T> elements;

    public Group() {
        this.elements = new LinkedHashSet<T>();
    }
    public Group(Collection<T> elements) {
        this.elements = new LinkedHashSet<T>(elements);
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    public Group<T> clear() {
        this.elements.clear();
        return this;
    }

    public Group<T> insert(T element) {
        this.elements.add(element);
        return this;
    }

    public boolean contains(T element) {
        if (element == null) {
            return false;
        }
        for(var el : this.elements) {
            if(el.equals(element)) {
                return true;
            }
        }
        return false;
    }

    public T findElement(T element) {
        if(element == null) {
            return null;
        }

        for (T currentElement : this.elements) {
            if (currentElement.equals(element)) {
                return currentElement;
            }
        }

        return null;
    }

    public Group<T> union(Group<T> group) {
        if(group == null) {
            return this;
        }
        Set<T> resultSet = new LinkedHashSet<T>(this.elements);
        resultSet.addAll(group.getElements());
        return new Group<T>(resultSet);
    }

    public Group<T> intersection(Group<T> group) {
        Set<T> resultSet = new LinkedHashSet<T>();
        for (var thisItem : this.elements) {
            for (var item : group.getElements()) {
                if (thisItem.equals(item)) {
                    resultSet.add(item);
                }
            }
        }
        return new Group<T>(resultSet);
    }

    public Group<T> duplicate() {
        return new Group<T>(this.elements);
    }

    public boolean equals(Group<T> group) {
        return this.getElements().equals(group.getElements());
    }

    public Collection<T> getElements() {
        return this.elements;
    }

    public Iterator<T> getIterator() {
        return this.elements.iterator();
    }

    public boolean removeElement(T element) {
        return this.elements.remove(element);
    }

    public String toString() {
        var set = new TreeSet<String>();
        for (var el: this.elements) {
            set.add(el.toString());
        }
        StringBuilder groupString = new StringBuilder("{ ");
        for (var currentElement : set) {
            groupString.append(currentElement).append(", ");
        }
        groupString = new StringBuilder(groupString.substring(0, groupString.length() - 2));
        groupString.append(" }");
        return groupString.toString();
    }
}
