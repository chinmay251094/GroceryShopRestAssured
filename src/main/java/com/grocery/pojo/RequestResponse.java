package com.grocery.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RequestResponse extends RequestBase {
    private URL url;

    public RequestResponse(Body body, String method, List<Header> header, String description, URL url) {
        super(body, method, header, description);
        this.url = url;
    }
}
