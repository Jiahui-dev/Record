package com.yjh.record.activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.yjh.base.core.event.hub.EventHub;
import com.yjh.base.core.event.model.RefreshEvent;
import com.yjh.base.uikit.widget.dialog.bottom.MultiCategoryGridDialog;
import com.yjh.base.uikit.widget.dialog.center.ListSelectDialog;
import com.yjh.record.R;
import com.yjh.record.contract.AddProductContract;
import com.yjh.record.databinding.AcAddProductBinding;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jiahui on 2026/07/18
 */
public class AddProductActivity extends BaseActivity<AcAddProductBinding> implements AddProductContract.View {

    @IntentParam
    String data;

    private String dateStr;

    private String selectedIconCode = ProductIconDict.GOODS.getCode();
    private String selectedStateCode = ProductStateDict.IN_USE.getCode();

    @InjectPresenter
    AddProductPresenter addProductPresenter;

    @Override
    protected AcAddProductBinding initBinding(LayoutInflater inflater) {
        return AcAddProductBinding.inflate(inflater);
    }

    @Override
    public void initView() {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.setTitle("添加资产", TitleBar.TitleGravity.LEFT);
        binding.ivProductIcon.setImageResource(R.drawable.pic_goods);
        binding.etProductState.setText(ProductStateDict.getTitleByCode(selectedStateCode));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initListener() {
        setClick(v -> {
            DateSpinner dateSpinner = new DateSpinner(this, 3);
            dateSpinner.setOnSelectedListener((year, month, day) -> {
                binding.etPurchaseDate.setText(year + "年" + month + "月" + day + "日");
                dateStr = year + "." + month + "." + day;
            });
            dateSpinner.show();
        }, binding.etPurchaseDate);

        setClick(v -> {
            List<String> options = ProductStateDict.getTitleList();
            ListSelectDialog.<String>newInstance()
                    .setData(options, item -> item)
                    .setOnItemClickListener((item, position) -> {
                        binding.etProductState.setText(item);
                        selectedStateCode = ProductStateDict.getCodeByTitle(item);
                    }).show(getSupportFragmentManager(), "");
        }, binding.etProductState);

        setClick(v -> {

            String productName = binding.etProductName.getText().toString().trim();
            String productPrice = binding.etProductPrice.getText().toString().trim();
            double price = ConvertUtils.toDouble(productPrice);

            if (productName.isEmpty()) {
                ToastUtils.showShort("商品名称不能为空");
                return;
            }
            if (productPrice.isEmpty()) {
                ToastUtils.showShort("商品价格不能为空");
                return;
            }
            if (price < 0) {
                ToastUtils.showShort("请输入正确的价格数字");
                return;
            }
            if (dateStr == null || dateStr.isEmpty()) {
                ToastUtils.showShort("购买日期不能为空");
                return;
            }

            showLoading("保存中");

            addProductPresenter.saveProduct(selectedIconCode, productName, price, dateStr, 1, selectedStateCode);

        }, binding.btnSubmitProduct);

        setClick(v -> {
            List<MultiCategoryGridDialog.TabCategory<ProductIconDict>> categories = new ArrayList<>();
            categories.add(new MultiCategoryGridDialog.TabCategory<>(
                    "数码产品",
                    Arrays.asList(ProductIconDict.CPU, ProductIconDict.CHASSIS, ProductIconDict.GRAPHICS_CARD,
                            ProductIconDict.MOUSE, ProductIconDict.HEADPHONES_OE, ProductIconDict.DISPLAY,
                            ProductIconDict.POWER, ProductIconDict.CONTROLLER_01, ProductIconDict.CONTROLLER_02,
                            ProductIconDict.HEADPHONES_01, ProductIconDict.HEADPHONES_02, ProductIconDict.LAPTOP_01,
                            ProductIconDict.LAPTOP_02, ProductIconDict.DESKTOP_01, ProductIconDict.DESKTOP_02,
                            ProductIconDict.FAN, ProductIconDict.HARD_DISK, ProductIconDict.KEYBOARD, ProductIconDict.USB,
                            ProductIconDict.MOTHERBOARD, ProductIconDict.ROUTER, ProductIconDict.VR_01, ProductIconDict.VR_02)
            ));

            categories.add(new MultiCategoryGridDialog.TabCategory<>(
                    "生活用品",
                    Arrays.asList(ProductIconDict.TOILETRIES, ProductIconDict.BROADBAND, ProductIconDict.ELECTRIC_KETTLE)
            ));

            categories.add(new MultiCategoryGridDialog.TabCategory<>(
                    "家用电器",
                    Arrays.asList(ProductIconDict.TELEVISION, ProductIconDict.FRIDGE, ProductIconDict.WASHING_MACHINE,
                            ProductIconDict.AIR_CONDITIONER, ProductIconDict.HEATING)
            ));

            categories.add(new MultiCategoryGridDialog.TabCategory<>(
                    "车品出行",
                    Arrays.asList(ProductIconDict.BICYCLE_01, ProductIconDict.BICYCLE_02, ProductIconDict.BICYCLE_03,
                            ProductIconDict.ELECTRIC_VEHICLE, ProductIconDict.MOTORCYCLE)
            ));

            categories.add(new MultiCategoryGridDialog.TabCategory<>(
                    "其他",
                    Arrays.asList(ProductIconDict.GOODS)
            ));

            MultiCategoryGridDialog.newInstance(
                    6,
                    categories,
                    (binding, data, position) -> {
                        binding.tvItemName.setText(data.getTitle());
                        binding.ivItemIcon.setImageResource(data.getIconRes());
                    },
                    (data, globalPosition) -> {
                        binding.ivProductIcon.setImageResource(data.getIconRes());
                        selectedIconCode = data.getCode();
                    }
            ).showTitle(true).show(getSupportFragmentManager(), "dialog_select_icon");
        }, binding.ivProductIcon);
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
        ToastUtils.showShort("添加商品成功");
        EventHub.post(new RefreshEvent());
        finish();
    }

    @Override
    public void onSaveProductFailed(String errorMsg) {
        hideLoading();
        ToastUtils.showShort("添加商品失败");
    }
}
