package com.yjh.record.contract;

import androidx.lifecycle.LifecycleOwner;
import com.yjh.base.core.contract.IBaseView;
import com.yjh.record.model.ProductBean;
import java.util.List;

public interface LoadProductsContract {
    interface View extends IBaseView {
        // 让 View 拿到 LiveData 去观察
        LifecycleOwner getLifecycleOwner();
        void onLoadProductsSuccess(List<ProductBean> productList);
        void onLoadProductsFailed(String errorMsg);
    }
    interface Presenter{
        void loadProducts();
        void loadProductsByCategory(int categoryId);
    }
}
