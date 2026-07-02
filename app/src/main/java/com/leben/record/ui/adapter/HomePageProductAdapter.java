package com.leben.record.ui.adapter;

import android.content.Context;

import com.leben.base.ui.adapter.BaseRecyclerAdapter;
import com.leben.base.ui.adapter.holder.BaseViewHolder;
import com.leben.record.model.bean.ProductEntity;

public class HomePageProductAdapter extends BaseRecyclerAdapter<ProductEntity> {

    public HomePageProductAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return 0;
    }

    @Override
    protected void bindData(BaseViewHolder holder, ProductEntity data, int position) {

    }
}
