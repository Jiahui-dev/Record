package com.yjh.record.ui.activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.yjh.record.R;
import com.yjh.record.contract.AddProductContract;
import com.yjh.record.databinding.AcAddProductBinding;
import com.yjh.record.presenter.AddProductPresenter;
import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.core.annotation.IntentParam;
import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.spinner.DateSpinner;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.base.utils.util.ConvertUtils;
import com.yjh.base.utils.util.ToastUtils;

public class AddProductActivity extends BaseActivity<AcAddProductBinding> implements AddProductContract.View {

    @IntentParam
    String data;

    private EditText etProductName;
    private EditText etProductPrice;
    private EditText etPurchaseDate;
    private Button btnSubmitProduct;
    private String dateStr;

    @InjectPresenter
    AddProductPresenter addProductPresenter;

    @Override
    protected AcAddProductBinding initBinding(LayoutInflater inflater) {
        return AcAddProductBinding.inflate(inflater);
    }

    @Override
    public void initView() {
        TitleBar titleBar=findViewById(R.id.title_bar);
        titleBar.setTitle("添加");
        etPurchaseDate=findViewById(R.id.et_purchase_date);
        btnSubmitProduct=findViewById(R.id.btn_submit_product);
        etProductName=findViewById(R.id.et_product_name);
        etProductPrice=findViewById(R.id.et_product_price);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initListener() {
        setClick(v->{
            DateSpinner dateSpinner=new DateSpinner(this,3);
            dateSpinner.setOnSelectedListener((year,month,day)->{
                etPurchaseDate.setText(year+"年"+month+"月"+day+"日");
                dateStr=year+"."+month+"."+day;
            });
            dateSpinner.show();
        },etPurchaseDate);

        setClick(v->{

            String productName = etProductName.getText().toString().trim();
            String productPrice=etProductPrice.getText().toString().trim();
            double price = ConvertUtils.toDouble(productPrice);

            if (productName.isEmpty()) {
                ToastUtils.show(this,"商品名称不能为空");
                return;
            }
            if (productPrice.isEmpty()) {
                ToastUtils.show(this,"商品价格不能为空");
                return;
            }
            if (price < 0) {
                ToastUtils.show(this, "请输入正确的价格数字");
                return;
            }
            if (dateStr == null || dateStr.isEmpty()) {
                ToastUtils.show(this,"购买日期不能为空");
                return;
            }

            showLoading("保存中");

            addProductPresenter.saveProduct(productName, price,dateStr,1);

        },btnSubmitProduct);
    }

    @Override
    public void initData() {

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
    public void onSaveProductSuccess() {
        hideLoading();
        ToastUtils.show(this,"添加商品成功");
        finish();
    }

    @Override
    public void onSaveProductFailed(String errorMsg) {
        hideLoading();
        ToastUtils.show(this,"添加商品失败");
    }
}
