package com.yjh.record.activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.core.annotation.IntentParam;
import com.yjh.base.core.model.event.EventHub;
import com.yjh.base.core.model.event.RefreshEvent;
import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.dialog.center.CommonDialog;
import com.yjh.base.utils.util.ToastUtils;
import com.yjh.record.R;
import com.yjh.record.contract.DeleteProductContract;
import com.yjh.record.databinding.AcDetailPageProductBinding;
import com.yjh.record.model.bean.ProductBean;
import com.yjh.record.model.dict.ProductIconDict;
import com.yjh.record.model.dict.ProductStateDict;
import com.yjh.record.presenter.DeleteProductPresenter;
import com.yjh.record.utils.DataUtils;

/**
 * Created by jiahui on 2026/07/23
 */
public class DetailPageProductActivity extends BaseActivity<AcDetailPageProductBinding> implements DeleteProductContract.View {

    @IntentParam(name = "product")
    private ProductBean mProduct;

    private ImageView ivDeleteProduct;

    @InjectPresenter
    DeleteProductPresenter deleteProductPresenter;

    @Override
    protected AcDetailPageProductBinding initBinding(LayoutInflater inflater) {
        return AcDetailPageProductBinding.inflate(inflater);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void initView() {
        binding.titleBar.setTitle("资产详情");
        ivDeleteProduct = new ImageView(this);
        ivDeleteProduct.setImageResource(com.yjh.base.uikit.R.drawable.uikit_icon_delect);
        binding.titleBar.addRightView(ivDeleteProduct, 20, 20);
        binding.tvProductName.setText(mProduct.getName());
        binding.tvProductPrice.setText("¥"+ DataUtils.formatNumber(mProduct.getPrice()));
        binding.ivProductPicture.setImageResource(ProductIconDict.getIconResByCode(mProduct.getIconCode()));
        binding.tvProductState.setText(ProductStateDict.getTitleByCode(mProduct.getStateCode()));
        binding.tvPurchaseDate.setText(mProduct.getPurchaseDate());
        binding.tvProductPriceSecond.setText("¥"+ DataUtils.formatNumber(mProduct.getPrice()));
        binding.tvDailyCost.setText("¥"+ DataUtils.calculateDailyCostFormatted(mProduct.getPurchaseDate(),mProduct.getPrice()));
        int textColor = R.color.product_state_green;
        switch (ProductStateDict.getTitleByCode(mProduct.getStateCode())) {
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

    @Override
    protected void initListener() {
        setClick(v -> {
            CommonDialog dialog = new CommonDialog();
            dialog.setTitle("确认删除");
            dialog.setContent("确认要删除此物品吗？");
            dialog.setOnConfirmListener(confirm -> {
                deleteProductPresenter.deleteProduct(mProduct);
            });
            dialog.setOnCancelListener(cancel -> {
                dialog.dismiss();
            });
            dialog.show(getSupportFragmentManager(), "DetailPageDeleteProductDialog");
        }, ivDeleteProduct);
    }

    @Override
    protected View getTopView() {
        return binding.titleBar;
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.white;
    }

    @Override
    public void onDeleteProductSuccess() {
        ToastUtils.showShort("已删除");
        EventHub.post(new RefreshEvent());
        finish();
    }

    @Override
    public void onDeleteProductFailed(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }
}
