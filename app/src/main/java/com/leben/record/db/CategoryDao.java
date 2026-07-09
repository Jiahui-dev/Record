package com.leben.record.db;

import androidx.room.Dao;
import androidx.room.Query;
import com.leben.base.db.BaseDao;
import com.leben.record.model.bean.CategoryEntity;
import java.util.List;

@Dao
public interface CategoryDao extends BaseDao<CategoryEntity> {

    //查询所有分类
    @Query("SELECT * FROM categories")
    List<CategoryEntity> getAllCategories();

}
