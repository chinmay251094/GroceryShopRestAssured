package com.grocery.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public final class TokenGenerator {
    private String clientName;
    private String clientEmail;
}
