package com.leben.record.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leben.base.annotation.InjectPresenter;
import com.leben.base.annotation.IntentParam;
import com.leben.base.ui.activity.BaseActivity;
import com.leben.base.util.ConvertUtils;
import com.leben.base.util.ToastUtils;
import com.leben.base.widget.spinner.DateSpinner;
import com.leben.base.widget.titleBar.TitleBar;
import com.leben.record.R;
import com.leben.record.contract.AddProductContract;
import com.leben.record.presenter.AddProductPresenter;

public class AddProductActivity extends BaseActivity implements AddProductContract.View {

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
    protected int getLayoutId() {
        return R.layout.ac_add_product;
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
        return findViewById(R.id.title_bar);
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
