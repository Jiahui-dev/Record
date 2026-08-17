package com.yjh.record.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.dialog.center.CommonDialog;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.base.utils.util.ToastUtils;
import com.yjh.record.R;
import com.yjh.record.databinding.AcSettingBinding;

/**
 * Created by jiahui on 2026/7/24
 */
public class SettingActivity extends BaseActivity<AcSettingBinding> {

    private ConstraintLayout cl_01;
    private ConstraintLayout cl_04;

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
        TitleBar titleBar = binding.titleBar;
        titleBar.setTitle("设置", TitleBar.TitleGravity.LEFT);
        cl_01 = binding.cl01;
        cl_04 = binding.cl04;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        setClick(v -> {
            ToastUtils.showShort("点击了cl_01");
        }, cl_01);

        setClick(v->{
            CommonDialog dialog=new CommonDialog();
            dialog.setTitle("免责声明");
            dialog.setContent("本软件仅供学习交流、科研等非商业性质的用途，严禁将本软件用于商业目的。如有任何商业行为，均与本软件无关。");
            dialog.setButtons("同意","退出");
            dialog.setOnConfirmListener(result->{
                dialog.dismiss();
            });
            dialog.setOnCancelListener(result->{
                finishAffinity();

                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
            });
            dialog.show(getSupportFragmentManager(),"dialog_disclaimers");
        },cl_04);
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.grey_backGround;
    }
}
