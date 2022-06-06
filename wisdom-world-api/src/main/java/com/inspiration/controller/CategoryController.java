package com.inspiration.controller;

import com.inspiration.service.CategoryService;
import com.inspiration.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SunDocker
 */
@RestController
@RequestMapping("categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping
    public Result categories() {
        return categoryService.allCategories();
    }

    @GetMapping("detail")
    public Result categoriesDetail() {
        return categoryService.allCategoriesDetail();
    }

    @GetMapping("detail/{id}")
    public Result categoryDetail(@PathVariable("id") Long categoryId) {
        return categoryService.categoryDetailById(categoryId);
    }
}
