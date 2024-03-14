package com.effortless.effortlessmarket.category;

import com.effortless.effortlessmarket.domain.category.dto.CategoryRequest;
import com.effortless.effortlessmarket.domain.category.entity.Category;
import com.effortless.effortlessmarket.domain.category.repository.CategoryRepository;
import com.effortless.effortlessmarket.domain.category.service.CategoryService;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CategoryTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private CategoryRequest newCategoryRequest;

    @BeforeEach
    void setUp() {
        // 초기화 및 목 설정
        newCategoryRequest = CategoryRequest.builder()
                .name("New Category")
                .build();


        // 카테고리 중복이 없을 경우
        when(categoryRepository.findByNameAndParentId(anyString(), any()))
                .thenReturn(Optional.empty());

        // 특정 이름을 가진 카테고리가 이미 있을 경우의 목 설정
        Category existingCategory = new Category("Test Category", null);
        when(categoryRepository.findByNameAndParentId("Test Category", null))
                .thenReturn(Optional.of(existingCategory));
    }

    @Test
    void createCategory_WithNoDuplicates_ShouldCreateCategory() {
        assertDoesNotThrow(() -> categoryService.createCategory(newCategoryRequest));
    }

    @Test
    void createCategory_WithDuplicates_ShouldThrowException() {
        CategoryRequest duplicateRequest = CategoryRequest.builder()
                .name("Test Category")
                .build();

        assertThrows(CustomExceptionType.class, () -> categoryService.createCategory(duplicateRequest));
    }

    @Test
    void getCategory() {
        // 예시로 '의류' 카테고리 생성
        Category createdCategory = new Category("의류", null);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(createdCategory));

        Category foundCategory = categoryService.getCategory(1L);
        assertNotNull(foundCategory);
        assertEquals("의류", foundCategory.getName());
    }
}
