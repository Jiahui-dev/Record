package com.leben.record.ui.activity;

import android.view.View;
import android.widget.ImageView;
import com.leben.base.annotation.InjectPresenter;
import com.leben.base.router.BaseRouter;
import com.leben.base.ui.activity.BaseRecyclerActivity;
import com.leben.base.ui.adapter.BaseRecyclerAdapter;
import com.leben.base.widget.titleBar.TitleBar;
import com.leben.record.R;
import com.leben.record.constant.Constant;
import com.leben.record.contract.LoadProductsContract;
import com.leben.record.model.bean.ProductEntity;
import com.leben.record.presenter.LoadProductsPresenter;
import com.leben.record.ui.adapter.HomePageProductAdapter;
import java.util.List;

public class HomePageActivity extends BaseRecyclerActivity<ProductEntity> implements LoadProductsContract.View{

    private TitleBar titleBar;
    private ImageView ivAddProduct;

    @InjectPresenter
    LoadProductsPresenter loadProductsPresenter;

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
        autoRefresh();
    }

    @Override
    public void onRefresh() {
        loadProductsPresenter.loadProducts();
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.white;
    }

    @Override
    protected View getTitleBarView() {
        return findViewById(R.id.title_bar);
    }


    @Override
    public void onLoadProductsSuccess(List<ProductEntity> productList) {
        refreshListSuccess(productList);
    }

    @Override
    public void onLoadProductsFailed(String errorMsg) {
        refreshListFailed(errorMsg);
    }

    @Override
    public void onResume() {
        super.onResume();
        autoRefresh();
    }

    @Override
    protected boolean isSupportLoadMore() {
        return false;
    }
}
