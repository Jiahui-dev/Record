package com.yjh.record.activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.yjh.base.core.model.event.RefreshEvent;
import com.yjh.base.uikit.widget.dialog.bottom.GridPanelBottomDialog;
import com.yjh.base.uikit.widget.dialog.center.ListSelectDialog;
import com.yjh.record.R;
import com.yjh.record.contract.AddProductContract;
import com.yjh.record.databinding.AcAddProductBinding;
import com.yjh.record.model.bean.ProductIconBean;
import com.yjh.record.model.dict.ProductIconDict;
import com.yjh.record.model.dict.ProductStateDict;
import com.yjh.record.presenter.AddProductPresenter;
import com.yjh.base.core.annotation.InjectPresenter;
import com.yjh.base.core.annotation.IntentParam;
import com.yjh.base.uikit.activity.BaseActivity;
import com.yjh.base.uikit.widget.spinner.DateSpinner;
import com.yjh.base.uikit.widget.titleBar.TitleBar;
import com.yjh.base.utils.util.ConvertUtils;
import com.yjh.base.utils.util.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jiahui on 2026/7/18
 */
public class AddProductActivity extends BaseActivity<AcAddProductBinding> implements AddProductContract.View {

    @IntentParam
    String data;

    private EditText etProductName;
    private EditText etProductPrice;
    private EditText etPurchaseDate;
    private EditText etProductState;
    private Button btnSubmitProduct;
    private ImageView ivProductIcon;
    private String dateStr;

    private String selectedIconCode = ProductIconDict.GOODS.getCode();
    private String selectedStateCode= ProductStateDict.IN_USE.getCode();

    @InjectPresenter
    AddProductPresenter addProductPresenter;

    @Override
    protected AcAddProductBinding initBinding(LayoutInflater inflater) {
        return AcAddProductBinding.inflate(inflater);
    }

    @Override
    public void initView() {
        TitleBar titleBar=findViewById(R.id.title_bar);
        titleBar.setTitle("添加资产");
        etPurchaseDate=findViewById(R.id.et_purchase_date);
        btnSubmitProduct=findViewById(R.id.btn_submit_product);
        etProductName=findViewById(R.id.et_product_name);
        etProductPrice=findViewById(R.id.et_product_price);
        ivProductIcon=binding.ivProductIcon;
        ivProductIcon.setImageResource(R.drawable.pic_goods);
        etProductState=binding.etProductState;
        etProductState.setText(ProductStateDict.getTitleByCode(selectedStateCode));
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
            List<String> options=ProductStateDict.getTitleList();
            ListSelectDialog.<String>newInstance()
                    .setData(options,item->item)
                    .setOnItemClickListener((item,position)->{
                        binding.etProductState.setText(item);
                        selectedStateCode=ProductStateDict.getCodeByTitle(item);
                    }).show(getSupportFragmentManager(),"");
        },etProductState);

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

            addProductPresenter.saveProduct(selectedIconCode,productName, price,dateStr,1,selectedStateCode);

        },btnSubmitProduct);

        setClick(v->{
            // 从 Enum 字典拿数据列表
            List<ProductIconDict> menus = Arrays.asList(ProductIconDict.values());

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
                        selectedIconCode = data.getCode();
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
    protected View getTopView() {
        return binding.titleBar;
    }

    @Override
    public void onSaveProductSuccess() {
        hideLoading();
        ToastUtils.show(this,"添加商品成功");
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void onSaveProductFailed(String errorMsg) {
        hideLoading();
        ToastUtils.show(this,"添加商品失败");
    }
}
