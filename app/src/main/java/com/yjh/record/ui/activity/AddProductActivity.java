package com.yjh.record.ui.activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.yjh.base.uikit.widget.dialog.bottom.GridPanelBottomDialog;
import com.yjh.record.R;
import com.yjh.record.contract.AddProductContract;
import com.yjh.record.databinding.AcAddProductBinding;
import com.yjh.record.model.bean.ProductIconBean;
import com.yjh.record.presenter.AddProductPresenter;
import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.core.annotation.IntentParam;
import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.spinner.DateSpinner;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.base.utils.util.ConvertUtils;
import com.yjh.base.utils.util.ToastUtils;

import java.util.Arrays;
import java.util.List;

public class AddProductActivity extends BaseActivity<AcAddProductBinding> implements AddProductContract.View {

    @IntentParam
    String data;

    private EditText etProductName;
    private EditText etProductPrice;
    private EditText etPurchaseDate;
    private Button btnSubmitProduct;
    private ImageView ivProductIcon;
    private String dateStr;

    // 记录当前选中的图标 ID，0 表示未选择/无图标
    private int selectedIconRes = 0;

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
        ivProductIcon=binding.ivProductIcon;
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

        setClick(v->{
            ProductIconBean item01=new ProductIconBean("键盘",R.drawable.pic_keyboard);
            ProductIconBean item02=new ProductIconBean("相机",R.drawable.pic_camera);
            ProductIconBean item03=new ProductIconBean("手柄",R.drawable.pic_controller);
            ProductIconBean item04=new ProductIconBean("U盘",R.drawable.pic_usb);
            ProductIconBean item05=new ProductIconBean("鼠标",R.drawable.pic_mouse);
            ProductIconBean item06=new ProductIconBean("鼠标1",R.drawable.pic_mouse);
            ProductIconBean item07=new ProductIconBean("鼠标2",R.drawable.pic_mouse);
            ProductIconBean item08=new ProductIconBean("鼠标3",R.drawable.pic_mouse);
            ProductIconBean item09=new ProductIconBean("鼠标4",R.drawable.pic_mouse);
            ProductIconBean item10=new ProductIconBean("鼠标5",R.drawable.pic_mouse);
            ProductIconBean item11=new ProductIconBean("鼠标6",R.drawable.pic_mouse);
            ProductIconBean item12=new ProductIconBean("鼠标7",R.drawable.pic_mouse);
            ProductIconBean item13=new ProductIconBean("鼠标8",R.drawable.pic_mouse);
            ProductIconBean item14=new ProductIconBean("鼠标9",R.drawable.pic_mouse);
            ProductIconBean item15=new ProductIconBean("鼠标10",R.drawable.pic_mouse);
            List<ProductIconBean> menus = Arrays.asList(item01,item02,item03,item04,item05,item06,item07,item08,item09,item10,
                    item11,item12,item13,item14,item15);

            GridPanelBottomDialog.newInstance(
                    3, 4, menus,
                    (binding, data, position) -> {
                        binding.tvItemName.setText(data.getTitle());
                        binding.ivItemIcon.setImageResource(data.getIconRes());
                    },
                    (data, globalPosition) -> {
                        ivProductIcon.setImageResource(data.getIconRes());
                        //清除 pic_set_product_icon
                        binding.ivProductIcon.setBackground(null);
                    }

            ).showTitle(true).show(getSupportFragmentManager(), "dialog_select_product_icon");

        },ivProductIcon);
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
