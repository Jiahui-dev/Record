package com.leben.record.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import com.jakewharton.rxbinding2.view.RxView;
import com.leben.base.router.BaseRouter;
import com.leben.base.ui.activity.BaseRecyclerActivity;
import com.leben.base.ui.adapter.BaseRecyclerAdapter;
import com.leben.base.util.LogUtils;
import com.leben.base.widget.titleBar.TitleBar;
import com.leben.record.R;
import com.leben.record.constant.Constant;
import com.leben.record.model.bean.ProductEntity;
import com.leben.record.ui.adapter.HomePageProductAdapter;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class HomePageActivity extends BaseRecyclerActivity<ProductEntity> {

    private TitleBar titleBar;
    private ImageView ivAddProduct;

    @Override
    protected BaseRecyclerAdapter<ProductEntity> createAdapter() {
        return new HomePageProductAdapter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_home_page;
    }

    @Override
    public void initView() {
        super.initView();
        titleBar=findViewById(R.id.title_bar);
        ivAddProduct=new ImageView(this);
        ivAddProduct.setImageResource(R.drawable.ic_add_circle);
        titleBar.addRightView(ivAddProduct,30,30);
        titleBar.setBackVisible(false);
    }

    @Override
    public void initListener() {
        setClick(v->{
            BaseRouter.getInstance().build(Constant.Router.AddProduct).navigation(this);
        },ivAddProduct);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onRefresh() {

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
