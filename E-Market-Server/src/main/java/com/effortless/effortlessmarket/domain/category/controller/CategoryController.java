package com.effortless.effortlessmarket.domain.category.controller;

import com.effortless.effortlessmarket.domain.category.dto.CategoryRequest;
import com.effortless.effortlessmarket.domain.category.dto.CategoryResponse;
import com.effortless.effortlessmarket.domain.category.entity.Category;
import com.effortless.effortlessmarket.domain.category.service.CategoryService;
import com.effortless.effortlessmarket.global.annotaion.ExcludeLogging;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /* 카테고리 생성 */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    /* 카테고리 단일 조회 */
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.getCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    /**
     * 단일 계층의 모든 카테고리 불러오기
     * data.layer : required,
     * data.parentId : Nullable
     */
    @ExcludeLogging
    @GetMapping("/depth")
    public ResponseEntity<List<CategoryResponse.categoryOfLayer>> getCategoryOfLayer(
            @RequestParam("depth") Integer depth,
            @Nullable @RequestParam("parentId") Long parentId) {
        List<CategoryResponse.categoryOfLayer> categoryList = categoryService.getCategories(depth, parentId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryList);
    }

    @GetMapping("/child/{parentId}")
    public ResponseEntity<List<CategoryResponse.categoryOfLayer>> getChildCategories(@PathVariable("parentId") Long parentId){
        List<CategoryResponse.categoryOfLayer> categories = categoryService.getChildCategories(parentId);

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    /* 카테고리 검증 */
    @GetMapping("/check")
    public ResponseEntity<Category> checkCategory(
            @RequestBody CategoryRequest.check request
    ) {
        Optional<Category> category = categoryService.findCategory(request.getName(), request.getDepth(), request.getParentId());
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /* 카테고리 계층적 조회 */
    @GetMapping
    public List<Category> getAllCategory() {
        return categoryService.getAllCategoriesWithHierarchy();
    }
}
