package com.yjh.record.presenter;

import com.yjh.base.core.presenter.BasePresenter;
import com.yjh.record.contract.LoadProductsContract;
import com.yjh.record.db.AppDatabase;
import com.yjh.record.model.bean.ProductBean;
import android.os.Handler;
import android.os.Looper;
import java.util.List;

public class LoadProductsPresenter extends BasePresenter<LoadProductsContract.View> implements LoadProductsContract.Presenter {

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    @Override
    public void loadProducts() {
        new Thread(() -> {
            try {
                List<ProductBean> productList = AppDatabase.getInstance()
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
                List<ProductBean> productList = AppDatabase.getInstance()
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
