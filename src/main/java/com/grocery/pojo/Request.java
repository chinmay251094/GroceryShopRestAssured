package com.grocery.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    Body body;
    private String url;
    private String method;
    private List<Header> header;
    private String description;
}
