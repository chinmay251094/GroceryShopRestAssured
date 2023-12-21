package com.grocery.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CartIdentifier {
    @JsonProperty("cartId")
    private String cartId;
}
