package com.yjh.record.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.yjh.base.core.router.BaseRouter;
import com.yjh.base.uikit.adapter.SimpleAdapter;
import com.yjh.base.uikit.controller.IRefreshListener;
import com.yjh.record.R;
import com.yjh.record.constant.Constant;
import com.yjh.record.contract.LoadProductsContract;
import com.yjh.record.databinding.AcHomePageBinding;
import com.yjh.record.databinding.ItemProductBinding;
import com.yjh.record.model.bean.ProductBean;
import com.yjh.record.presenter.LoadProductsPresenter;
import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.uikit.activity.BaseRecyclerActivity;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import java.util.List;

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
                }
        );

        // 4. 如果需要点击事件，直接在这里顺手挂上
        adapter.setOnItemClickListener((view, viewId, position, data) -> {
            // 处理 Item 的点击逻辑
        });

        return adapter;
    }

    @Override
    protected RecyclerView attachRecyclerView() {
        return null;
    }

    @Override
    protected View attachRefreshLayout() {
        return super.attachRefreshLayout();
    }

    @Override
    protected AcHomePageBinding initBinding(LayoutInflater inflater) {
        return AcHomePageBinding.inflate(inflater);
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
        autoRefresh();
    }

    @Override
    protected boolean isSupportLoadMore() {
        return false;
    }
}
