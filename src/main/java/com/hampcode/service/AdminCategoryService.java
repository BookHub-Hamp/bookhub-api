package com.hampcode.service;

import com.hampcode.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminCategoryService {
    List<Category> getAll();
    Page<Category> paginate(Pageable pageable);
    Category findById(Integer id);
    Category create(Category category);
    Category update(Integer id, Category updateCategory);
    void delete(Integer id);
}
