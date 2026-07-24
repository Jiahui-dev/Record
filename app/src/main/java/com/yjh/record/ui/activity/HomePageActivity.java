package com.yjh.record.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.core.model.event.RefreshEvent;
import com.yjh.base.core.router.BaseRouter;
import com.yjh.base.uikit.activity.BaseRecyclerActivity;
import com.yjh.base.uikit.adapter.SimpleAdapter;
import com.yjh.base.uikit.controller.IRefreshListener;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.record.R;
import com.yjh.record.constant.Constant;
import com.yjh.record.contract.LoadProductsContract;
import com.yjh.record.databinding.AcHomePageBinding;
import com.yjh.record.databinding.ItemProductBinding;
import com.yjh.record.model.ProductBean;
import com.yjh.record.presenter.LoadProductsPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by youjiahui on 2026/7/18
 */
public class HomePageActivity extends BaseRecyclerActivity<ProductBean, AcHomePageBinding>
        implements IRefreshListener, LoadProductsContract.View {

    private TitleBar titleBar;
    private ImageView ivAddProduct;

    private SimpleAdapter<ProductBean, ItemProductBinding> productAdapter;

    @InjectPresenter
    LoadProductsPresenter loadProductsPresenter;

    @Override
    protected SimpleAdapter<ProductBean, ItemProductBinding> createAdapter() {
        productAdapter = new SimpleAdapter<>(
                this,
                ItemProductBinding::inflate,
                (binding, data, position) -> {
                    binding.tvProductName.setText(data.getName());
                    binding.tvProductPrice.setText(String.valueOf(data.getPrice()));
                    binding.tvPurchaseDate.setText(data.getPurchaseDate());
                }
        );

        productAdapter.setOnItemClickListener((view, viewId, position, data) -> {
            setClick(v -> {
                BaseRouter.getInstance()
                        .build(Constant.Router.DetailPageProduct)
                        .withSerializable("product", data)
                        .navigation(this);
            }, view);
        });

        return productAdapter;
    }

    @Override
    public void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        ivAddProduct = binding.fabAddProduct;

        RecyclerView recyclerView = attachRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public void initData() {
        loadProductsPresenter.loadProducts();
    }

    @Override
    public void initListener() {
        setClick(v -> {
            BaseRouter.getInstance()
                    .build(Constant.Router.AddProduct)
                    .navigation(this);
        }, ivAddProduct);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        loadProductsPresenter.loadProducts();
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.grey_backGround;
    }

    @Override
    public void onLoadProductsSuccess(List<ProductBean> productList) {
        // 1. 刷新基类自带的 Adapter 列表数据
        refreshListSuccess(productList);

        // 2. 直接操作局部 View 来改变头部数据
        double totalAmount = 0;
        int totalNumber = 0;
        if (productList != null) {
            for (ProductBean item : productList) {
                totalAmount += item.getPrice();
            }
            totalNumber = productList.size();
        }

        binding.tvTotalAmount.setText(String.format("%.2f", totalAmount));
        binding.tvTotalNumber.setText(String.valueOf(totalNumber));
    }

    @Override
    public void onLoadProductsFailed(String errorMsg) {
        refreshListFailed(errorMsg);
    }

    @Override
    protected boolean isSupportLoadMore() {
        return false;
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
    protected View getTopView() {
        return binding.tvSlogan;
    }

    @Override
    protected AcHomePageBinding initBinding(LayoutInflater inflater) {
        return AcHomePageBinding.inflate(inflater);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshEvent event){
        onRefresh();
    }

    @Override
    protected int setFooterBackgroundColorRes() {
        return R.color.white;
    }
}