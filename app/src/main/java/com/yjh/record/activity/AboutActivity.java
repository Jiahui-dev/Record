package com.yjh.record.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.base.utils.util.ToastUtils;
import com.yjh.record.R;
import com.yjh.record.constant.Constant;
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
        binding.titleBar.setTitle("关于", TitleBar.TitleGravity.LEFT);
    }

    @Override
    protected void initListener() {
        setClick(v -> {
            String url = Constant.URL.GitHub;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent); // 启动系统浏览器
            } else {
                ToastUtils.showShort("未找到可用的浏览器应用");
            }
        }, binding.cl01, binding.cl02);
    }

    @Override
    protected View getTopView() {
        return binding.titleBar;
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.background_grey_theme;
    }
}
