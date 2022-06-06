package com.inspiration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspiration.dao.mapper.CategoryMapper;
import com.inspiration.dao.pojo.Category;
import com.inspiration.service.CategoryService;
import com.inspiration.vo.CategoryVo;
import com.inspiration.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SunDocker
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public Result allCategories() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId, Category::getCategoryName);
        List<Category> categories = list(queryWrapper);
        return Result.success(copyList(categories));
    }

    @Override
    public Result allCategoriesDetail() {
        List<Category> categories = list();
        return Result.success(copyList(categories));
    }

    @Override
    public Result categoryDetailById(Long categoryId) {
        Category category = getById(categoryId);
        return Result.success(copy(category));
    }

    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }
}
