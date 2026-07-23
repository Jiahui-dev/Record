package com.yjh.record.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.yjh.base.core.router.BaseRouter;
import com.yjh.base.uikit.adapter.SimpleAdapter;
import com.yjh.base.uikit.controller.IRefreshListener;
import com.yjh.record.R;
import com.yjh.record.constant.Constant;
import com.yjh.record.contract.LoadProductsContract;
import com.yjh.record.databinding.AcHomePageBinding;
import com.yjh.record.databinding.ItemProductBinding;
import com.yjh.record.model.ProductBean;
import com.yjh.record.presenter.LoadProductsPresenter;
import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.uikit.activity.BaseRecyclerActivity;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import java.util.List;

/**
 * Created by youjiahui on 2026/7/18
 */
public class HomePageActivity extends BaseRecyclerActivity<ProductBean, AcHomePageBinding> implements IRefreshListener,LoadProductsContract.View{

    private TitleBar titleBar;
    private ImageView ivAddProduct;

    @InjectPresenter
    LoadProductsPresenter loadProductsPresenter;

    @Override
    protected SimpleAdapter<ProductBean, ItemProductBinding> createAdapter() {
        SimpleAdapter<ProductBean, ItemProductBinding> adapter = new SimpleAdapter<>(
                this,
                ItemProductBinding::inflate,
                (binding, data, position) -> {
                    binding.tvProductName.setText(data.getName());
                    binding.tvProductPrice.setText(String.valueOf(data.getPrice()));
                    binding.tvPurchaseDate.setText(data.getPurchaseDate());
                }
        );

        adapter.setOnItemClickListener((view, viewId, position, data) -> {
            setClick(v->{
                BaseRouter.getInstance()
                        .build(Constant.Router.DetailPageProduct)
                        .withSerializable("product",data)
                        .navigation(this);
            },view);
        });

        return adapter;
    }

    @Override
    protected RecyclerView attachRecyclerView() {
        return binding.contentView;
    }

    @Override
    protected View attachRefreshLayout() {
        return binding.swipeRefresh;
    }

    @Override
    protected AcHomePageBinding initBinding(LayoutInflater inflater) {
        return AcHomePageBinding.inflate(inflater);
    }

    @Override
    public void initView() {
        super.initView();
        titleBar=binding.titleBar;
        ivAddProduct=new ImageView(this);
        ivAddProduct.setImageResource(R.drawable.ic_add_circle);
        titleBar.addRightView(ivAddProduct,30,30);
        titleBar.setBackVisible(false);
    }

    @Override
    public void initData() {
        loadProductsPresenter.loadProducts();
    }

    @Override
    public void initListener() {
        setClick(v->{
            BaseRouter.getInstance()
                    .build(Constant.Router.AddProduct)
                    .navigation(this);
        },ivAddProduct);

    }

    @Override
    public void onRefresh() {
        loadProductsPresenter.loadProducts();
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        // 返回当前 Activity 作为生命周期所有者
        return this;
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.white;
    }

    @Override
    protected View getTitleBar() {
        return binding.titleBar;
    }


    @Override
    public void onLoadProductsSuccess(List<ProductBean> productList) {
        refreshListSuccess(productList);
    }

    @Override
    public void onLoadProductsFailed(String errorMsg) {
        refreshListFailed(errorMsg);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected boolean isSupportLoadMore() {
        return false;
    }
}
