package com.effortless.effortlessmarket.domain.category.service;

import com.effortless.effortlessmarket.domain.category.dto.CategoryRequest;
import com.effortless.effortlessmarket.domain.category.entity.Category;
import com.effortless.effortlessmarket.domain.category.repository.CategoryRepository;
import com.effortless.effortlessmarket.global.exception.CustomException;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /* 카테고리 조회 */
    public Category createCategory(CategoryRequest request) {
        /* 카테고리 중복 체크 */
        Optional<Category> alreadyCategory = categoryRepository.findByNameAndParentId(request.getName(), request.getParentId());
        if (alreadyCategory.isPresent()){
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
    public Category getCategory(Long id){
        return categoryRepository.findById(id).orElseThrow(
                () -> new CustomExceptionType(CustomException.CATEGORY_NOT_FOUND)
        );
    }

    /* 카테고리 계층 구조 적 조회*/
    public List<Category> getAllCategoriesWithHierarchy() {
        List<Category> allCategories = categoryRepository.findAll();
        Map<Long, Category> categoryMap = new HashMap<>();
        List<Category> rootCategories = new ArrayList<>();

        for (Category category : allCategories) {
            categoryMap.put(category.getId(), category);
        }

        for (Category category : allCategories) {
            if (category.getParent() == null) {
                rootCategories.add(category);
            } else {
                Category parent = categoryMap.get(category.getParent().getId());
                if (parent != null) {
                    parent.getChildren().add(category);
                }
            }
        }

        return rootCategories;
    }



}
