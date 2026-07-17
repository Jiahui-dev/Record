package com.yjh.base.ui.activity;

import com.yjh.base.listener.IRefreshListener;
import com.yjh.base.controller.SwipeRefreshController;

public abstract class BaseRefreshActivity extends BaseActivity implements IRefreshListener {

    protected SwipeRefreshController mRefreshController;

    @Override
    public void initView() {
        int refreshId = getResources().getIdentifier("swipeRefresh", "id", getPackageName());
        if (refreshId != 0) {
            mRefreshController = new SwipeRefreshController(this, refreshId);
            mRefreshController.setOnRefreshListener(this);
        }
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        if (mRefreshController != null) {
            mRefreshController.autoRefresh();
        }
        onRefresh();
    }

    /**
     * 结束刷新
     */
    public void refreshComplete() {
        if (mRefreshController != null) {
            mRefreshController.finishRefresh();
        }
    }

}
