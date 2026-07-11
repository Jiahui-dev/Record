package com.leben.record.contract;

import com.leben.base.contract.IBaseView;
import com.leben.record.model.bean.ProductEntity;
import java.util.List;

public interface LoadProductsContract {
    interface View extends IBaseView{
        void onLoadProductsSuccess(List<ProductEntity> productList);
        void onLoadProductsFailed(String errorMsg);
    }
    interface Presenter{
        void loadProducts();
        void loadProductsByCategory(int categoryId);
    }
}
