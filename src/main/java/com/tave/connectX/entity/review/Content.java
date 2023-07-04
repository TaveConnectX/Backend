package com.tave.connectX.entity.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

@Getter
public class Content {
    Map<String, String> content;

    public Content(Map<String, String> content) {
        this.content = content;
    }
}
