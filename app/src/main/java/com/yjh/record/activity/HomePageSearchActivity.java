package com.yjh.record.activity;

import androidx.core.content.ContextCompat;

import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.core.router.BaseRouter;
import com.yjh.base.uikit.activity.BaseSearchActivity;
import com.yjh.base.uikit.adapter.SimpleAdapter;
import com.yjh.record.R;
import com.yjh.record.constant.Constant;
import com.yjh.record.contract.LoadProductsContract;
import com.yjh.record.databinding.ItemProductBinding;
import com.yjh.record.model.bean.ProductBean;
import com.yjh.record.model.dict.ProductIconDict;
import com.yjh.record.model.dict.ProductStateDict;
import com.yjh.record.presenter.LoadProductsPresenter;

import java.util.List;

/**
 * Created by youjiahui on 2026/08/14
 */
public class HomePageSearchActivity extends BaseSearchActivity<ProductBean> implements LoadProductsContract.View {

    @InjectPresenter
    LoadProductsPresenter loadProductsPresenter;

    private SimpleAdapter<ProductBean, ItemProductBinding> productAdapter;

    @Override
    protected SimpleAdapter<ProductBean, ItemProductBinding> createAdapter() {
        productAdapter = new SimpleAdapter<>(
                this,
                ItemProductBinding::inflate,
                (binding, data, position) -> {
                    binding.tvProductName.setText(data.getName());
                    binding.tvProductPrice.setText(String.valueOf(data.getPrice()));
                    binding.tvPurchaseDate.setText(data.getPurchaseDate());
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
                    binding.tvProductState.setTextColor(
                            ContextCompat.getColor(binding.getRoot().getContext(), textColor)
                    );
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
    protected void doSearch(String keyword) {
        loadProductsPresenter.loadProductsByName(keyword);
    }

    @Override
    public void onLoadProductsSuccess(List<ProductBean> productList) {
        refreshListSuccess(productList,false);
    }

    @Override
    public void onLoadProductsFailed(String errorMsg) {
        refreshListFailed(errorMsg);
    }
}
