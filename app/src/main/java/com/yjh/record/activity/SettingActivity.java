package com.yjh.record.activity;

import android.view.LayoutInflater;
import android.view.View;
import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.record.R;
import com.yjh.record.databinding.AcSettingBinding;

/**
 * Created by jiahui on 2026/7/24
 */
public class SettingActivity extends BaseActivity<AcSettingBinding> {

    @Override
    protected AcSettingBinding initBinding(LayoutInflater inflater) {
        return AcSettingBinding.inflate(inflater);
    }

    @Override
    protected View getTopView() {
        return binding.titleBar;
    }

    @Override
    protected void initView() {
        super.initView();
        TitleBar titleBar=binding.titleBar;
        titleBar.setTitle("设置",TitleBar.TitleGravity.LEFT);
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
