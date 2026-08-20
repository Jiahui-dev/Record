package com.yjh.record.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.core.model.event.EventHub;
import com.yjh.base.core.model.event.RefreshEvent;
import com.yjh.base.core.router.BaseRouter;
import com.yjh.base.uikit.activity.BaseRecyclerActivity;
import com.yjh.base.uikit.adapter.SimpleAdapter;
import com.yjh.base.uikit.controller.PermissionController;
import com.yjh.base.uikit.listener.IRefreshListener;
import com.yjh.base.uikit.controller.PerformanceTestingController;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.record.R;
import com.yjh.record.constant.Constant;
import com.yjh.record.contract.LoadProductsContract;
import com.yjh.record.databinding.AcHomePageBinding;
import com.yjh.record.databinding.ItemProductBinding;
import com.yjh.record.model.bean.ProductBean;
import com.yjh.record.model.dict.ProductIconDict;
import com.yjh.record.model.dict.ProductStateDict;
import com.yjh.record.presenter.LoadProductsPresenter;
import com.yjh.record.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiahui on 2026/07/18
 */
public class HomePageActivity extends BaseRecyclerActivity<ProductBean, AcHomePageBinding> implements IRefreshListener, LoadProductsContract.View {

    private SimpleAdapter<ProductBean, ItemProductBinding> productAdapter;

    @InjectPresenter
    LoadProductsPresenter loadProductsPresenter;

    @SuppressLint("SetTextI18n")
    @Override
    protected SimpleAdapter<ProductBean, ItemProductBinding> createAdapter() {
        productAdapter = new SimpleAdapter<>(this, ItemProductBinding::inflate, (binding, data, position) -> {
            binding.tvProductName.setText(data.getName());
            binding.tvProductPrice.setText("¥" + DataUtils.formatNumber(data.getPrice()));
            binding.tvPurchaseDate.setText(DataUtils.getDaysFromPurchaseDate(data.getPurchaseDate()) + "天");
            binding.ivProductPicture.setImageResource(ProductIconDict.getIconResByCode(data.getIconCode()));
            binding.tvProductState.setText(ProductStateDict.getTitleByCode(data.getStateCode()));
            int textColor = R.color.product_state_green;
            switch (ProductStateDict.getTitleByCode(data.getStateCode())) {
                case "使用中":
                    textColor = R.color.product_state_green;
                    binding.tvProductState.setBackgroundResource(R.drawable.bg_green_radius_25);
                    break;
                case "闲置中":
                    textColor = R.color.product_state_brown;
                    binding.tvProductState.setBackgroundResource(R.drawable.bg_brown_radius_25);
                    break;
                case "已损坏":
                    textColor = R.color.product_state_red;
                    binding.tvProductState.setBackgroundResource(R.drawable.bg_red_radius_25);
                    break;
                case "已变卖":
                case "已丢失":
                    textColor = R.color.product_state_grey;
                    binding.tvProductState.setBackgroundResource(R.drawable.bg_silvery_radius_25);
                    break;
            }
            binding.tvProductState.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), textColor));
        });

        productAdapter.setOnItemClickListener((view, viewId, position, data) -> {
            setClick(v -> {
                BaseRouter.getInstance().build(Constant.Router.DetailPageProduct).withSerializable("product", data).navigation(this);
            }, view);
        });

        return productAdapter;
    }

    @Override
    protected void onRegisterControllers() {
        registerController(PerformanceTestingController.class, new PerformanceTestingController(getClass().getSimpleName()));
        registerController(PermissionController.class, new PermissionController(this));
        registerController(EventHub.Controller.class, new EventHub.Controller());
    }

    @Override
    protected AcHomePageBinding initBinding(LayoutInflater inflater) {
        return AcHomePageBinding.inflate(inflater);
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
    protected void initView() {
        super.initView();
        binding.titleBar.setBackVisible(false);
        List<String> hints = new ArrayList<>();
        hints.add("搜索");
        binding.titleBar.setSearchHints(hints);
        binding.titleBar.startRoll();

        View rootLayout = findViewById(R.id.rootLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) rootLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        RecyclerView recyclerView = attachRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 禁用 RecyclerView 自身滑动，把它完全交给外层的 NestedScrollView 统一滚动
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(productAdapter);

    }

    @Override
    protected void initListener() {
        setClick(v -> {
            BaseRouter.getInstance().build(Constant.Router.AddProduct).navigation(this);
        }, binding.fabAddProduct);

        setClick(v -> {
            BaseRouter.getInstance().build(Constant.Router.Setting).navigation(this);
        }, binding.ivSetting);

        setClick(v -> {
            BaseRouter.getInstance().build(Constant.Router.HomePageSearch).navigation(this);
        }, binding.titleBar);
    }

    @Override
    protected void initEvent() {
        EventHub.Controller hub = getController(EventHub.Controller.class);
        if (hub != null) {
            hub.observe(RefreshEvent.class, event -> {
                onRefresh();
            });
        }
    }

    @Override
    public void initData() {
        loadProductsPresenter.loadProducts();
    }

    @Override
    public void onRefresh() {
        loadProductsPresenter.loadProducts();
    }

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onLoadProductsSuccess(List<ProductBean> productList) {
        refreshListSuccess(productList, false);

        double totalAmounts = 0;
        if (productList != null) {
            for (ProductBean item : productList) {
                totalAmounts += item.getPrice();
            }
        }

        double totalDailyAverageCost = 0;
        if (productList != null) {
            for (ProductBean item : productList) {
                if ("IN_USE".equals(item.getStateCode())) {
                    double dailyCost = DataUtils.calculateDailyCost(item.getPurchaseDate(), item.getPrice());
                    totalDailyAverageCost += dailyCost;
                }
            }
        }

        binding.tvTotalAmounts.setText(DataUtils.formatNumber(totalAmounts));
        binding.tvDailyAverageCost.setText(DataUtils.formatNumber(totalDailyAverageCost));
    }

    @Override
    public void onLoadProductsFailed(String errorMsg) {
        refreshListFailed(errorMsg);
    }

    @Override
    protected View getTopView() {
        return binding.LinearLayoutSlogan;
    }

    @Override
    protected int setFooterBackgroundColorRes() {
        return R.color.white;
    }
}