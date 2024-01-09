package com.grocery.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class CollectionRequest extends CollectionBase {
    List<FolderRequest> item;

    public CollectionRequest(Info info, List<FolderRequest> item) {
        super(info);
        this.item = item;
    }
}
