package com.effortless.effortlessmarket.domain.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryRequest {

    private String name;
    private Long parentId;
}
