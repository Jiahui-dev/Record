package com.yjh.record.model.dict;

import androidx.annotation.DrawableRes;
import com.yjh.record.R;
import com.yjh.record.model.bean.ProductIconBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 资产图标系统字典
 * Created by jiahui on 2026/7/24
 */
public enum ProductIconDict {

    KEYBOARD("KEYBOARD", "键盘", R.drawable.pic_keyboard),
    CAMERA("CAMERA", "相机", R.drawable.pic_camera),
    CONTROLLER("CONTROLLER", "手柄", R.drawable.pic_controller),
    USB("USB", "U盘", R.drawable.pic_usb),
    MOUSE("MOUSE", "鼠标", R.drawable.pic_mouse),
    GOODS("GOODS","物品",R.drawable.pic_goods);

    private final String code;
    private final String title;
    private final int iconRes;

    ProductIconDict(String code, String title, @DrawableRes int iconRes) {
        this.code = code;
        this.title = title;
        this.iconRes = iconRes;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getIconRes() { return iconRes; }

    /**
     * 转成 UI 用的 List<ProductIconBean>
     */
    public static List<ProductIconBean> getIconList() {
        List<ProductIconBean> list = new ArrayList<>();
        for (ProductIconDict item : values()) {
            list.add(new ProductIconBean(item.getTitle(), item.getIconRes()));
        }
        return list;
    }

    /**
     * 根据 Code 查询对应的图片资源（UI 显示时用）
     */
    public static int getIconResByCode(String code) {
        if (code == null) return R.drawable.pic_keyboard; // 兜底默认图
        for (ProductIconDict item : values()) {
            if (item.getCode().equalsIgnoreCase(code)) {
                return item.getIconRes();
            }
        }
        return R.drawable.pic_keyboard; // 未找到时的兜底图
    }
}