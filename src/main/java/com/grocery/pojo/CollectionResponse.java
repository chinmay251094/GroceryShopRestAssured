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
public class CollectionResponse extends CollectionBase {
    List<FolderResponse> item;

    CollectionResponse(Info info, List<FolderResponse> item) {
        super(info);
        this.item = item;
    }
}
