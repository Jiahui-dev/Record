package com.leben.record.presenter;

import android.os.Handler;
import android.os.Looper;
import com.leben.base.presenter.BasePresenter;
import com.leben.record.contract.AddProductContract;
import com.leben.record.db.AppDatabase;
import com.leben.record.model.bean.ProductEntity;

public class AddProductPresenter extends BasePresenter<AddProductContract.View> implements AddProductContract.Presenter {

    // 用于异步操作完成后，安全地切回主线程刷新 UI
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void saveProduct(String name, double price, String dateStr, int categoryId) {

        ProductEntity product = new ProductEntity();
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