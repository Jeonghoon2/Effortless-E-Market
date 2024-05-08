package com.effortless.effortlessmarket.domain.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryRequest {

    private String name;
    private Long parentId;

    @Data
    public static class check {
        private String name;
        private Long parentId;
        private Integer depth;
    }

    @Data
    public static class layer{
        private Long parentId = null;
        private Integer depth;
    }
}
