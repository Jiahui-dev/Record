package com.leben.record.db;

import androidx.room.Dao;
import androidx.room.Query;
import com.leben.base.db.BaseDao;
import com.leben.record.model.bean.ProductEntity;
import java.util.List;

@Dao
public interface ProductDao extends BaseDao<ProductEntity> {

    //根据分类 ID 查询该分类下的所有电子产品
    @Query("SELECT * FROM products WHERE category_id = :catId")
    List<ProductEntity> getProductsByCategoryId(int catId);

    //根据分类名称查出所有物品
    @Query("SELECT products.* FROM products " +
            "INNER JOIN categories ON products.category_id=categories.id " +
            "WHERE categories.category_name= :categoryName")
    List<ProductEntity> getProductsByCategoryName(String categoryName);

}
