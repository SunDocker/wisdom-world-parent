package com.inspiration.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspiration.dao.mapper.IdeaCollectionMapper;
import com.inspiration.dao.pojo.IdeaCollection;
import com.inspiration.service.IdeaCollectionService;
import org.springframework.stereotype.Service;

/**
 * @author SunDocker
 */
@Service
public class IdeaCollectionServiceImpl extends ServiceImpl<IdeaCollectionMapper, IdeaCollection> implements IdeaCollectionService{
}
