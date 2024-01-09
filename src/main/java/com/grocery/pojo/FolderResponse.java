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
public class FolderResponse extends FolderBase {
    private List<RequestRootResponse> item;

    FolderResponse(String name, List<RequestRootResponse> item) {
        super(name);
        this.item = item;
    }
}
