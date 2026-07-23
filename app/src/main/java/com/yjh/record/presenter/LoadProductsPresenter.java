package com.yjh.record.presenter;

import com.yjh.base.core.presenter.BasePresenter;
import com.yjh.record.contract.LoadProductsContract;
import com.yjh.record.db.AppDatabase;
import com.yjh.record.model.ProductBean;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LoadProductsPresenter extends BasePresenter<LoadProductsContract.View> implements LoadProductsContract.Presenter {

    private LiveData<List<ProductBean>> productLiveData;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void loadProducts() {
        if (getView() == null) return;

        // 确保只初始化一次观察者
        if (productLiveData == null) {
            // Room 内部处理了异步查询，返回的 LiveData 会在主线程触发回调
            productLiveData = AppDatabase.getInstance().productDao().getAll();

            productLiveData.observe(getView().getLifecycleOwner(), productList -> {
                if (getView() != null) {
                    if (productList != null) {
                        getView().onLoadProductsSuccess(productList);
                    } else {
                        getView().onLoadProductsFailed("数据加载失败");
                    }
                }
            });
        }
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
