package com.grocery.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderRequest extends FolderBase {
    List<RequestRootRequest> item;

    public FolderRequest(String name, List<RequestRootRequest> item) {
        super(name);
        this.item = item;
    }
}
