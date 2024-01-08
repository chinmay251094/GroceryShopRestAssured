package com.grocery.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Body {
    private String mode;
    private String raw;

    private Body setMode(String mode) {
        this.mode = mode;
        return this;
    }

    private Body setRaw(String raw) {
        this.raw = raw;
        return this;
    }
}
