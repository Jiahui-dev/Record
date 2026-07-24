package com.yjh.record.presenter;

import android.os.Handler;
import android.os.Looper;
import com.yjh.base.core.presenter.BasePresenter;
import com.yjh.record.contract.DeleteProductContract;
import com.yjh.record.db.AppDatabase;
import com.yjh.record.model.bean.ProductBean;

public class DeleteProductPresenter extends BasePresenter<DeleteProductContract.View> implements DeleteProductContract.Presenter{

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void deleteProduct(ProductBean product) {
        if (product == null) {
            if (getView() != null) {
                getView().onDeleteProductFailed("删除失败：商品数据为空");
            }
            return;
        }

        new Thread(() -> {
            try {
                // 调用继承自 BaseDao 的 delete 方法
                // Room 会自动根据 product 的主键 id 去数据库执行删除
                int rows = AppDatabase.getInstance()
                        .productDao()
                        .delete(product);

                mainHandler.post(() -> {
                    if (getView() != null) {
                        if (rows > 0) {
                            getView().onDeleteProductSuccess();
                        } else {
                            // rows == 0 说明数据库里没有这条记录（可能已被删或主键不对）
                            getView().onDeleteProductFailed("删除失败：未找到该商品");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    if (getView() != null) {
                        getView().onDeleteProductFailed(e.getMessage());
                    }
                });
            }
        }).start();
    }
}
