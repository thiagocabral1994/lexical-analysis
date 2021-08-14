package br.ufjf.model;

public class State extends Base<State> {
    private String name = "";

    public State () { }
    public State (String name) { this.name = name; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State clone() {
        return new State(this.getName());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof State && this.getName().equals(((State) obj).getName());
    }

    @Override
    public String toString(){
        var stateString = "";
        stateString += this.getName();
        return stateString;
    }
}
