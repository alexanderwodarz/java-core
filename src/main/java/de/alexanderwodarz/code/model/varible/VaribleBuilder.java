package de.alexanderwodarz.code.model.varible;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VaribleBuilder {

    private String key;
    private Object value;

    private final VaribleMap map;

    public VaribleBuilder setKey(String key) {
        this.key = key;
        return this;
    }

    public VaribleBuilder setValue(Object value) {
        this.value = value;
        return this;
    }

    public VaribleMap build() {
        Varible varible = new Varible();
        varible.setKey(key);
        varible.setValue(value);
        map.put(varible);
        return map;
    }

}
