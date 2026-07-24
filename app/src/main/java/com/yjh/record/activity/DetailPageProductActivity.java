package com.yjh.record.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.core.annotation.IntentParam;
import com.yjh.base.core.model.event.RefreshEvent;
import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.dialog.center.CommonDialog;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.base.utils.util.ToastUtils;
import com.yjh.record.R;
import com.yjh.record.contract.DeleteProductContract;
import com.yjh.record.databinding.AcDetailPageProductBinding;
import com.yjh.record.model.ProductBean;
import com.yjh.record.presenter.DeleteProductPresenter;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by jiahui on 2026/7/23
 */
public class DetailPageProductActivity extends BaseActivity<AcDetailPageProductBinding> implements DeleteProductContract.View {

    @IntentParam(name = "product")
    private ProductBean mProduct;

    private TitleBar titleBar;
    private ImageView ivDeleteProduct;

    @InjectPresenter
    DeleteProductPresenter deleteProductPresenter;

    @Override
    protected AcDetailPageProductBinding initBinding(LayoutInflater inflater) {
        return AcDetailPageProductBinding.inflate(inflater);
    }

    @Override
    protected void initView() {
        titleBar=binding.titleBar;
        titleBar.setTitle("资产详情");
        ivDeleteProduct=new ImageView(this);
        ivDeleteProduct.setImageResource(R.drawable.pic_delect);
        titleBar.addRightView(ivDeleteProduct,20,20);
        binding.tvProductName.setText(mProduct.getName());
        binding.tvProductPrice.setText(String.valueOf(mProduct.getPrice()));
        binding.tvPurchaseDate.setText(mProduct.getPurchaseDate());
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        setClick(v->{
            CommonDialog dialog=new CommonDialog();
            dialog.setTitle("确认删除");
            dialog.setContent("确认要删除此物品吗？");
            dialog.setOnConfirmListener(confirm->{
                deleteProductPresenter.deleteProduct(mProduct);
            });
            dialog.setOnCancelListener(cancel->{
                dialog.dismiss();
            });
            dialog.show(getSupportFragmentManager(),"DetailPageDeleteProductDialog");
        },ivDeleteProduct);
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
        ToastUtils.show(this,"已删除");
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void onDeleteProductFailed(String errorMsg) {
        ToastUtils.show(this,errorMsg);
    }
}
