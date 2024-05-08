package com.effortless.effortlessmarket.domain.category.dto;

import com.effortless.effortlessmarket.domain.category.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class CategoryResponse {

    @Data
    public static class categoryOfLayer{
        private Long id;
        private String name;
        private Integer depth = 0;

        public categoryOfLayer(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.depth = category.getDepth();
        }
    }

    public class category {
    }
}
