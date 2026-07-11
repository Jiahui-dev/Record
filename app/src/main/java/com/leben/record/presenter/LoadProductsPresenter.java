package com.leben.record.presenter;

import com.leben.base.presenter.BasePresenter;
import com.leben.record.contract.LoadProductsContract;
import com.leben.record.db.AppDatabase;
import com.leben.record.model.bean.ProductEntity;
import android.os.Handler;
import android.os.Looper;
import java.util.List;

public class LoadProductsPresenter extends BasePresenter<LoadProductsContract.View> implements LoadProductsContract.Presenter {

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    @Override
    public void loadProducts() {
        new Thread(() -> {
            try {
                List<ProductEntity> productList = AppDatabase.getInstance()
                        .productDao()
                        .getAll();
                mainHandler.post(() -> {
                    if (getView() != null) {
                        getView().onLoadProductsSuccess(productList);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    if (getView() != null) {
                        getView().onLoadProductsFailed(e.getMessage());
                    }
                });
            }
        }).start();
    }

    @Override
    public void loadProductsByCategory(int categoryId) {
        new Thread(() -> {
            try {
                List<ProductEntity> productList = AppDatabase.getInstance()
                        .productDao()
                        .getProductsByCategoryId(categoryId);
                mainHandler.post(() -> {
                    if (getView() != null) {
                        getView().onLoadProductsSuccess(productList);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    if (getView() != null) {
                        getView().onLoadProductsFailed(e.getMessage());
                    }
                });
            }
        }).start();
    }
}
