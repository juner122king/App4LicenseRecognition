package com.eb.geaiche.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.GoodsBrand;
import com.juner.mvp.bean.GoodsCategory;

import java.util.List;

public class MallTypeBrandListAdapter extends BaseQuickAdapter<GoodsBrand, BaseViewHolder> {

    Context context;

    public MallTypeBrandListAdapter(@Nullable List<GoodsBrand> data, Context c) {
        super(R.layout.activity_mall_brand_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBrand item) {
        helper.setText(R.id.tv_name, item.getName());


//        Glide.with(context)
//                .load(item.getSrc())
//                .into((ImageView) helper.getView(R.id.src));
    }
}