package de.alexanderwodarz.code.model.varible;

import java.util.ArrayList;

public class VaribleMap {

    private ArrayList<Varible> varibles = new ArrayList<>();

    public VaribleMap put(Varible varible) {
        this.varibles.add(varible);
        return this;
    }

    public VaribleMap put(String key, Object value){
        return new VaribleBuilder(this).setKey(key).setValue(value).build();
    }

    public VaribleBuilder put() {
        return new VaribleBuilder(this);
    }

    public Varible search(String key) {
        return this.varibles.stream().filter(k -> k.getKey().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

    public void remove(String key) {
        Varible varible = search(key);
        if (varible == null)
            return;
        this.varibles.remove(varible);
    }

    public ArrayList<Varible> getVaribles() {
        return varibles;
    }
}
