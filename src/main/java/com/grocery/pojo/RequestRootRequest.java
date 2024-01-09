package com.grocery.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRootRequest extends RequestRootBase {
    private RequestRequest request;

    public RequestRootRequest(String name, RequestRequest request) {
        super(name);
        this.request = request;
    }
}
