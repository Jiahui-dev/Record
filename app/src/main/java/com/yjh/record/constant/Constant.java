package com.yjh.record.constant;

import com.yjh.record.activity.AddProductActivity;
import com.yjh.record.activity.DetailPageProductActivity;
import com.yjh.record.activity.HomePageActivity;
import com.yjh.base.core.router.IRoutePath;
import com.yjh.record.activity.SettingActivity;

public interface Constant {
    interface Router{
        IRoutePath<HomePageActivity> HomePage = () -> HomePageActivity.class;
        IRoutePath<AddProductActivity> AddProduct = () -> AddProductActivity.class;
        IRoutePath<DetailPageProductActivity> DetailPageProduct = () -> DetailPageProductActivity.class;
        IRoutePath<SettingActivity> Setting = () -> SettingActivity.class;
    }
}
