package com.yjh.record.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.yjh.base.core.db.BaseDao;
import com.yjh.record.model.ProductBean;
import java.util.List;

@Dao
public interface ProductDao extends BaseDao<ProductBean> {

    //根据分类 ID 查询该分类下的所有电子产品
    @Query("SELECT * FROM products WHERE category_id = :catId")
    List<ProductBean> getProductsByCategoryId(int catId);

    //根据分类名称查出所有物品
    @Query("SELECT products.* FROM products " +
            "INNER JOIN categories ON products.category_id=categories.id " +
            "WHERE categories.category_name= :categoryName")
    List<ProductBean> getProductsByCategoryName(String categoryName);

    @Query("SELECT * FROM products ORDER BY id DESC")
    LiveData<List<ProductBean>> getAll();

}
