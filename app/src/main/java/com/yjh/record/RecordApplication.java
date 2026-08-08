package com.yjh.record;

import com.yjh.base.core.BaseApplication;
import com.yjh.base.uikit.controller.NetworkStateController;
import com.yjh.base.uikit.leak.GlobalLeakMonitor;
import com.yjh.base.utils.util.ToastUtils;

public class RecordApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        GlobalLeakMonitor.init(this);
        NetworkStateController.init(this);
    }
}