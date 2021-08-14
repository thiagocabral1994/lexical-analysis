package br.ufjf.model;

public abstract class Base<T extends Base> {
    public abstract T clone();
    public abstract boolean equals(Object obj);
}
