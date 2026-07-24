package com.yjh.record.contract;

import com.yjh.base.core.contract.IBaseView;

public interface AddProductContract {

    interface View extends IBaseView {
        void onSaveProductSuccess();
        void onSaveProductFailed(String errorMsg);
    }

    interface Presenter {
        void saveProduct(String icon,String name, double price, String dateStr, int categoryId,String state);
    }
}
