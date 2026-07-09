package com.leben.record.ui.activity;

import android.view.View;

import com.leben.base.annotation.IntentParam;
import com.leben.base.ui.activity.BaseActivity;
import com.leben.base.widget.titleBar.TitleBar;
import com.leben.record.R;

public class AddProductActivity extends BaseActivity {

    @IntentParam
    String data;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_add_product;
    }

    @Override
    public void initView() {
        TitleBar titleBar=findViewById(R.id.title_bar);
        titleBar.setTitle("添加");
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected int getStatusBarColor() {
        return R.color.white;
    }

    @Override
    protected View getTitleBarView() {
        return findViewById(R.id.title_bar);
    }

}
