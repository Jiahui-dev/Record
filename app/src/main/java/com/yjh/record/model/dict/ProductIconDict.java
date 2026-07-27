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

    //数码产品
    GRAPHICS_CARD("GRAPHICS_CARD","显卡",R.drawable.pic_graphics_card),
    MOUSE("MOUSE", "鼠标", R.drawable.pic_mouse),
    CHASSIS("CHASSIS","机箱",R.drawable.pic_chassis),
    HEADPHONES_OE("HEADPHONES_OE","耳机",R.drawable.pic_headphones_oe),
    DISPLAY("DISPLAY","显示器",R.drawable.pic_display),
    CPU("CPU","CPU",R.drawable.pic_cpu),
    POWER("POWER","电源",R.drawable.pic_power),
    CONTROLLER_01("CONTROLLER_01","手柄",R.drawable.pic_controller_01),
    CONTROLLER_02("CONTROLLER_02","手柄",R.drawable.pic_controller_02),
    HEADPHONES_01("HEADPHONES_01","耳机",R.drawable.pic_headphones_01),
    HEADPHONES_02("HEADPHONES_02","耳机",R.drawable.pic_headphones_02),
    LAPTOP_01("LAPTOP_01","笔记本",R.drawable.pic_laptop_01),
    LAPTOP_02("LAPTOP_02","笔记本",R.drawable.pic_laptop_02),
    DESKTOP_01("DESKTOP_01","台式机",R.drawable.pic_desktop_01),
    DESKTOP_02("DESKTOP_02","台式机",R.drawable.pic_desktop_02),
    FAN("FAN","风扇",R.drawable.pic_fan),
    HARD_DISK("HARD_DISK","硬盘",R.drawable.pic_hard_disk),
    KEYBOARD("KEYBOARD","键盘",R.drawable.pic_keyboard),
    USB("USB","USB",R.drawable.pic_usb),
    ROUTER("ROUTER","路由器",R.drawable.pic_router),
    MOTHERBOARD("MOTHERBOARD","主板",R.drawable.pic_motherboard),
    VR_01("VR_01","VR眼镜",R.drawable.pic_vr_01),
    VR_02("VR_02","VR眼镜",R.drawable.pic_vr_02),

    //生活用品
    BROADBAND("BROADBAND","宽带",R.drawable.pic_broadband),
    TOILETRIES("TOILETRIES","洗漱用品",R.drawable.pic_toiletries),
    ELECTRIC_KETTLE("ELECTRIC_KETTLE","电热水壶",R.drawable.pic_electric_kettle),

    //家用电器
    AIR_CONDITIONER("AIR_CONDITIONER","空调",R.drawable.pic_air_conditioner),
    FRIDGE("FRIDGE","冰箱",R.drawable.pic_fridge),
    TELEVISION("TELEVISION","电视机",R.drawable.pic_television),
    WASHING_MACHINE("WASHING_MACHINE","洗衣机",R.drawable.pic_washing_machine),
    HEATING("HEATING","暖气",R.drawable.pic_heating),

    //车品出行
    BICYCLE("BICYCLE","自行车",R.drawable.pic_bicycle),

    //其他
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
        if (code == null) return R.drawable.pic_goods; // 兜底默认图
        for (ProductIconDict item : values()) {
            if (item.getCode().equalsIgnoreCase(code)) {
                return item.getIconRes();
            }
        }
        return R.drawable.pic_goods; // 未找到时的兜底图
    }
}