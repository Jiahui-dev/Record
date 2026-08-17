package com.yjh.record.activity;

import android.view.LayoutInflater;

import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.record.databinding.AcAboutBinding;

/**
 * Created by jiahui on 2026/07/24
 */
public class AboutActivity extends BaseActivity<AcAboutBinding> {

    @Override
    protected AcAboutBinding initBinding(LayoutInflater inflater) {
        return AcAboutBinding.inflate(inflater);
    }

    @Override
    protected void initView() {
        super.initView();
        TitleBar titleBar=binding.titleBar;
        titleBar.setTitle("关于");
    }
}
