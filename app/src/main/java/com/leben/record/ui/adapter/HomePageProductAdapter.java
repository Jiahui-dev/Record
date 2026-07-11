package com.leben.record.ui.adapter;

import android.content.Context;

import com.leben.base.ui.adapter.BaseRecyclerAdapter;
import com.leben.base.ui.adapter.holder.BaseViewHolder;
import com.leben.record.R;
import com.leben.record.model.bean.ProductEntity;

public class HomePageProductAdapter extends BaseRecyclerAdapter<ProductEntity> {

    public HomePageProductAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_product;
    }

    @Override
    protected void bindData(BaseViewHolder holder, ProductEntity data, int position) {
        holder.setText(R.id.tv_product_name,data.getName())
                .setText(R.id.tv_product_price,String.valueOf(data.getPrice()))
                .setText(R.id.tv_purchase_date,data.getPurchaseDate());
    }
}
