package com.grocery.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Header {
    private String key;
    private String value;

    public Header setKey(String key) {
        this.key = key;
        return this;
    }

    public Header setValue(String value) {
        this.value = value;
        return this;
    }
}

