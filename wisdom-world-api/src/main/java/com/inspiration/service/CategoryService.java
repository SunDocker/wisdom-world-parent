package com.inspiration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspiration.dao.pojo.Category;
import com.inspiration.vo.Result;

/**
 * @author SunDocker
 */
public interface CategoryService extends IService<Category> {
    /**
     * 获取所有种类
     * @return
     */
    Result allCategories();

    /**
     * 获取所有种类的详细信息
     * @return
     */
    Result allCategoriesDetail();

    /**
     * 通过id获取分类详细信息
     * @param categoryId
     * @return
     */
    Result categoryDetailById(Long categoryId);
}
