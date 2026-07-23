package com.yjh.record.presenter;

import android.os.Handler;
import android.os.Looper;
import com.yjh.base.core.presenter.BasePresenter;
import com.yjh.record.contract.AddProductContract;
import com.yjh.record.db.AppDatabase;
import com.yjh.record.model.ProductBean;

public class AddProductPresenter extends BasePresenter<AddProductContract.View> implements AddProductContract.Presenter {

    // 用于异步操作完成后，安全地切回主线程刷新 UI
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void saveProduct(String name, double price, String dateStr, int categoryId) {

        ProductBean product = new ProductBean();
        product.setName(name);
        product.setPrice(price);
        product.setPurchaseDate(dateStr);
        product.setCategoryId(categoryId);

        // 开启子线程读写 SQLite 数据库
        new Thread(() -> {
            try {
                // 调用 Room 的 insert 方法
                AppDatabase.getInstance().productDao().insert(product);

                // 成功后，切回主线程回调给 View 层
                mainHandler.post(() -> {
                    if (getView() != null) {
                        getView().onSaveProductSuccess();
                    }
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (getView() != null) {
                        getView().onSaveProductFailed(e.getMessage());
                    }
                });
                e.printStackTrace();
            }
        }).start();
    }
}