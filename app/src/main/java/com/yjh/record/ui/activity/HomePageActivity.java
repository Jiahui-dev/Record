package com.yjh.record.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.core.router.BaseRouter;
import com.yjh.base.uikit.activity.BaseRecyclerActivity;
import com.yjh.base.uikit.adapter.SimpleAdapter;
import com.yjh.base.uikit.controller.IRefreshListener;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.record.R;
import com.yjh.record.constant.Constant;
import com.yjh.record.contract.LoadProductsContract;
import com.yjh.record.databinding.AcHomePageBinding;
import com.yjh.record.databinding.ItemHomeHeaderBinding;
import com.yjh.record.databinding.ItemProductBinding;
import com.yjh.record.model.ProductBean;
import com.yjh.record.presenter.LoadProductsPresenter;
import java.util.Collections;
import java.util.List;

/**
 * Created by youjiahui on 2026/7/18
 */
public class HomePageActivity extends BaseRecyclerActivity<ProductBean, AcHomePageBinding>
        implements IRefreshListener, LoadProductsContract.View {

    private TitleBar titleBar;
    private ImageView ivAddProduct;

    // 1. 定义两个 SimpleAdapter，分别处理 Header 和 列表
    private SimpleAdapter<HeaderEntity, ItemHomeHeaderBinding> headerAdapter;
    private SimpleAdapter<ProductBean, ItemProductBinding> productAdapter;

    // 2. 存放 Header 状态的数据实体
    private final HeaderEntity headerEntity = new HeaderEntity();

    @InjectPresenter
    LoadProductsPresenter loadProductsPresenter;

    @Override
    protected SimpleAdapter<ProductBean, ItemProductBinding> createAdapter() {
        // 创建主列表的 SimpleAdapter
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

        // --- 头部 Icon 逻辑保持不动 ---
        ivAddProduct = new ImageView(this);
        ivAddProduct.setImageResource(R.drawable.ic_add_circle);

        // --- 3. 初始化 Header 部分的 SimpleAdapter ---
        headerAdapter = new SimpleAdapter<>(
                this,
                ItemHomeHeaderBinding::inflate,
                (binding, data, position) -> {
                    binding.tvSlogan.setText("阳光正好，复盘也正好");
                    binding.tvTotalAmount.setText(data.totalAmount);
                    binding.tvTotalNumber.setText(data.totalNumber);
                }
        );
        // 让 Header 显示出一行 Item
        headerAdapter.setList(Collections.singletonList(headerEntity));

        // --- 4. 用 ConcatAdapter 拼接两个 SimpleAdapter ---
        ConcatAdapter concatAdapter = new ConcatAdapter(headerAdapter, productAdapter);

        // --- 5. 设置给 RecyclerView ---
        RecyclerView recyclerView = attachRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(concatAdapter);
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
    public void onRefresh() {
        loadProductsPresenter.loadProducts();
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.grey_backGround;
    }


    @Override
    public void onLoadProductsSuccess(List<ProductBean> productList) {
        // 刷新列表数据
        refreshListSuccess(productList);

        // 计算总金额与总资产数，并更新 Header
        double totalAmount = 0;
        if (productList != null) {
            for (ProductBean item : productList) {
                totalAmount += item.getPrice();
            }
            headerEntity.totalAmount = String.format("%.2f", totalAmount);
            headerEntity.totalNumber = String.valueOf(productList.size());
        } else {
            headerEntity.totalAmount = "0.00";
            headerEntity.totalNumber = "0";
        }

        // 刷新 Header 布局
        headerAdapter.notifyItemChanged(0);
    }

    @Override
    public void onLoadProductsFailed(String errorMsg) {
        refreshListFailed(errorMsg);
    }

    @Override
    protected boolean isSupportLoadMore() {
        return false;
    }

    /**
     * 简单的实体类，用于驱动 Header 数据的更新
     */
    private static class HeaderEntity {
        public String totalAmount = "0.00";
        public String totalNumber = "0";
    }
}