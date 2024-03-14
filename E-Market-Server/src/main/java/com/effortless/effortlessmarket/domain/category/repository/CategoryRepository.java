package com.effortless.effortlessmarket.domain.category.repository;

import com.effortless.effortlessmarket.domain.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParent(Category parent);

    List<Category> findByParentIsNull();

    Optional<Category> findByName(String name);

    Optional<Category> findByNameAndParentId(String name, Long id);
}
