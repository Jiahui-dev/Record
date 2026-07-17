package com.yjh.record.contract;

import com.yjh.base.core.contract.IBaseView;
import com.yjh.record.model.bean.ProductBean;
import java.util.List;

public interface LoadProductsContract {
    interface View extends IBaseView {
        void onLoadProductsSuccess(List<ProductBean> productList);
        void onLoadProductsFailed(String errorMsg);
    }
    interface Presenter{
        void loadProducts();
        void loadProductsByCategory(int categoryId);
    }
}
