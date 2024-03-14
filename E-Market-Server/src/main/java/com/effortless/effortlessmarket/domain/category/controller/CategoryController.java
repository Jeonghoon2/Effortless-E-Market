package com.effortless.effortlessmarket.domain.category.controller;

import com.effortless.effortlessmarket.domain.category.dto.CategoryRequest;
import com.effortless.effortlessmarket.domain.category.entity.Category;
import com.effortless.effortlessmarket.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /* 카테고리 생성 */
    @PostMapping
    public Category createCategory(@RequestBody CategoryRequest request){
        return categoryService.createCategory(request);
    }

    /* 카테고리 단일 조회 */
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id){
        return categoryService.getCategory(id);
    }

    /* 카테고리 계층적 조회 */
    @GetMapping
    public List<Category> getAllCategory(){
        return categoryService.getAllCategoriesWithHierarchy();
    }
}
