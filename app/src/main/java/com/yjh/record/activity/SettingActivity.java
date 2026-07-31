package com.yjh.record.activity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.base.utils.util.ToastUtils;
import com.yjh.record.R;
import com.yjh.record.databinding.AcSettingBinding;

/**
 * Created by jiahui on 2026/7/24
 */
public class SettingActivity extends BaseActivity<AcSettingBinding> {

    private ConstraintLayout cl_01;

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
        cl_01=binding.cl01;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        setClick(v->{
            ToastUtils.show(this,"点击了cl_01");
        },cl_01);
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.grey_backGround;
    }
}
