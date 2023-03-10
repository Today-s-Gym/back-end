package com.gym.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT c.name FROM Category c")
    List<String> findCategoryNames();

    Optional<Category> getByCategoryId(Integer categoryId);
}
