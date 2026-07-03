package com.leben.record.constant;

import com.leben.base.router.IRoutePath;
import com.leben.record.ui.activity.AddProductActivity;
import com.leben.record.ui.activity.HomePageActivity;

public interface Constant {
    interface Router{
        IRoutePath<HomePageActivity> HomePage = () -> HomePageActivity.class;
        IRoutePath<AddProductActivity> AddProduct = () -> AddProductActivity.class;
    }
}
