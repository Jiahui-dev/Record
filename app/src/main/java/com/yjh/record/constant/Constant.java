package com.yjh.record.constant;

import com.yjh.record.ui.activity.AddProductActivity;
import com.yjh.record.ui.activity.HomePageActivity;
import com.yjh.base.core.router.IRoutePath;

public interface Constant {
    interface Router{
        IRoutePath<HomePageActivity> HomePage = () -> HomePageActivity.class;
        IRoutePath<AddProductActivity> AddProduct = () -> AddProductActivity.class;
    }
}
