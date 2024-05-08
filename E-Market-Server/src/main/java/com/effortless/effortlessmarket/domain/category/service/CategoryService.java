package com.effortless.effortlessmarket.domain.category.service;

import com.effortless.effortlessmarket.domain.category.dto.CategoryRequest;
import com.effortless.effortlessmarket.domain.category.dto.CategoryResponse;
import com.effortless.effortlessmarket.domain.category.entity.Category;
import com.effortless.effortlessmarket.domain.category.repository.CategoryRepository;
import com.effortless.effortlessmarket.global.exception.CustomException;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /* 카테고리 조회 */
    @Transactional
    public Category createCategory(CategoryRequest request) {
        /* 카테고리 중복 체크 */
        Optional<Category> existingCategory = categoryRepository
                .findByNameAndParentId(request.getName(), request.getParentId());
        if (existingCategory.isPresent()) {
            throw new CustomExceptionType(CustomException.CATEGORY_IS_ALREADY);
        }

        Category parent = null;

        if (request.getParentId() != null) {
            parent = getCategory(request.getParentId());
        }

        Category newCategory = new Category(request.getName(), parent);
        return categoryRepository.save(newCategory); // 카테고리 저장
    }

    /* 카테고리 단일 조회 */
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CustomExceptionType(CustomException.CATEGORY_NOT_FOUND)
        );
    }

    /* 카테고리 계층 구조 적 조회*/
    public List<Category> getAllCategoriesWithHierarchy() {
        List<Category> allCategories = categoryRepository.findAll();
        Map<Long, Category> categoryMap = allCategories.stream()
                .collect(Collectors.toMap(Category::getId, category -> category));

        allCategories.forEach(category -> {
            Category parent = category.getParent() != null ? categoryMap.get(category.getParent().getId()) : null;
            if (parent != null) {
                parent.getChildren().add(category);
            }
        });

        return allCategories.stream()
                .filter(category -> category.getParent() == null)
                .collect(Collectors.toList());
    }


    public Optional<Category> findCategory(String name, Integer depth, Long parentId) {
        return categoryRepository.findByNameAndDepthAndParentId(name, depth, parentId);
    }

    public List<CategoryResponse.categoryOfLayer> getCategories(Integer depth, Long parentId) {
        List<Category> categories;

        if (parentId == null) {
            categories = categoryRepository.findByDepth(depth);
        } else {
            categories = categoryRepository.findByDepthAndParentId(depth, parentId);
        }

        return categories.stream()
                .map(CategoryResponse.categoryOfLayer::new)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse.categoryOfLayer> getChildCategories(Long parentId) {
        List<Category> byParent = categoryRepository.findByParentId(parentId);
        return byParent.stream()
                .map(CategoryResponse.categoryOfLayer::new)
                .collect(Collectors.toList());
    }

}
