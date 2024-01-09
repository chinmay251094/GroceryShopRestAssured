package com.grocery.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RequestRequest extends RequestBase {
    private String url;

    public RequestRequest(Body body, String method, List<Header> header, String description, String url) {
        super(body, method, header, description);
        this.url = url;
    }
}
