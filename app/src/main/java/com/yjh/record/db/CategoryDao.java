package com.yjh.record.db;

import androidx.room.Dao;
import androidx.room.Query;
import com.yjh.base.core.db.BaseDao;
import com.yjh.record.model.CategoryBean;
import java.util.List;

@Dao
public interface CategoryDao extends BaseDao<CategoryBean> {

    //查询所有分类
    @Query("SELECT * FROM categories")
    List<CategoryBean> getAllCategories();

}
