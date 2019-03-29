package com.eb.geaiche.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.UserEntity;

import java.util.List;

public class UserlistListAdapter extends BaseQuickAdapter<UserEntity, BaseViewHolder> {

    Context context;

    public UserlistListAdapter(@Nullable List<UserEntity> data, Context c) {
        super(R.layout.dialog_user_list_item_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserEntity item) {
        helper.setText(R.id.tv_name, item.getUsername());
        helper.setText(R.id.tv_mobile, item.getMobile());

    }
}