package com.leben.record.contract;

import com.leben.base.contract.IBaseView;

public interface AddProductContract {

    interface View extends IBaseView {
        void onSaveProductSuccess();
        void onSaveProductFailed(String errorMsg);
    }

    interface Presenter {
        void saveProduct(String name, double price, String dateStr, int categoryId);
    }
}
