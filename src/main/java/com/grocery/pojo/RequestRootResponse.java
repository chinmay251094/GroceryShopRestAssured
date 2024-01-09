package com.grocery.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestRootResponse extends RequestRootBase {
    private RequestResponse request;

    public RequestRootResponse(String name, RequestResponse request) {
        super(name);
        this.request = request;
    }
}
