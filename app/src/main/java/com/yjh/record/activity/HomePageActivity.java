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
import com.yjh.record.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiahui on 2026/7/18
 */
public class HomePageActivity extends BaseRecyclerActivity<ProductBean, AcHomePageBinding> implements IRefreshListener, LoadProductsContract.View {

    private TitleBar titleBar;
    private ImageView ivAddProduct;
    private ImageView ivSetting;

    private SimpleAdapter<ProductBean, ItemProductBinding> productAdapter;

    @InjectPresenter
    LoadProductsPresenter loadProductsPresenter;

    @SuppressLint("SetTextI18n")
    @Override
    protected SimpleAdapter<ProductBean, ItemProductBinding> createAdapter() {
        productAdapter = new SimpleAdapter<>(this, ItemProductBinding::inflate, (binding, data, position) -> {
            binding.tvProductName.setText(data.getName());
            binding.tvProductPrice.setText("¥"+data.getPrice());
            binding.tvPurchaseDate.setText(DateUtils.getDaysFromPurchaseDate(data.getPurchaseDate())+"天");
            binding.ivProductPicture.setImageResource(ProductIconDict.getIconResByCode(data.getIconCode()));
            binding.tvProductState.setText(ProductStateDict.getTitleByCode(data.getStateCode()));
            int textColor = R.color.green_product_state;
            switch (ProductStateDict.getTitleByCode(data.getStateCode())) {
                case "使用中":
                    textColor = R.color.green_product_state;
                    binding.tvProductState.setBackgroundResource(R.drawable.bg_green_radius_25);
                    break;
                case "闲置中":
                    textColor = R.color.brown_product_state;
                    binding.tvProductState.setBackgroundResource(R.drawable.bg_brown_radius_25);
                    break;
                case "已损坏":
                    textColor = R.color.red_product_state;
                    binding.tvProductState.setBackgroundResource(R.drawable.bg_red_radius_25);
                    break;
                case "已变卖":
                case "已丢失":
                    textColor = R.color.gray_product_state;
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
        ivAddProduct = binding.fabAddProduct;
        ivSetting = binding.ivSetting;
        titleBar = binding.titleBar;
        titleBar.setBackVisible(false);
        List<String> hints = new ArrayList<>();
        hints.add("搜索");
        titleBar.setSearchHints(hints);
        titleBar.startRoll();

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
        }, ivAddProduct);

        setClick(v -> {
            BaseRouter.getInstance().build(Constant.Router.Setting).navigation(this);
        }, ivSetting);

        setClick(v -> {
            BaseRouter.getInstance().build(Constant.Router.HomePageSearch).navigation(this);
        }, titleBar);
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

    @Override
    public void onLoadProductsSuccess(List<ProductBean> productList) {
        refreshListSuccess(productList, false);

        double totalAmount = 0;
        int totalNumber = 0;
        if (productList != null) {
            for (ProductBean item : productList) {
                totalAmount += item.getPrice();
            }
            totalNumber = productList.size();
        }

        double dailyAverageCost = 0;
        if (productList != null) {
            double inUseTotalPrice = 0;
            long totalDays = 0;
            int inUseCount = 0;

            for (ProductBean item : productList) {
                if ("IN_USE".equals(item.getStateCode())) {
                    inUseTotalPrice += item.getPrice();
                    long days = DateUtils.getDaysFromPurchaseDate(item.getPurchaseDate());
                    if (days > 0) {
                        totalDays += days;
                        inUseCount++;
                    }
                }
            }

            // 日均成本 = 使用中物品总价 ÷ 平均使用天数
            if (inUseCount > 0) {
                long averageDays = totalDays / inUseCount;
                if (averageDays > 0) {
                    dailyAverageCost = inUseTotalPrice / averageDays;
                }
            }
        }

        binding.tvTotalAmount.setText(String.format("%.2f", totalAmount));
        // 显示日均成本（保留两位小数）
        binding.tvDailyAverageCost.setText(String.format("%.2f", dailyAverageCost));
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