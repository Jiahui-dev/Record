package com.yjh.record.contract;

import com.yjh.base.core.contract.IBaseView;
import com.yjh.record.model.ProductBean;

public interface DeleteProductContract {
    interface View extends IBaseView{
        void onDeleteProductSuccess();
        void onDeleteProductFailed(String errorMsg);
    }
    interface Presenter{
        void deleteProduct(ProductBean product);
    }
}
