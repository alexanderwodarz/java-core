package de.alexanderwodarz.code.model.varible;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Varible {
    private String key;
    private Object value;

    public Varible(String key, Object value) {
        this.key = key;
        this.value = value;
    }
    public Varible() {

    }
}
