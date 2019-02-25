package com.eb.new_line_seller.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.Courses;
import com.juner.mvp.bean.GoodsEntity;

import java.util.List;

public class CollegeListAdapter extends BaseQuickAdapter<Courses, BaseViewHolder> {

    Activity activity;



    public CollegeListAdapter(Activity activity, @Nullable List<Courses> data) {
        super(R.layout.activity_college_list_item, data);
        this.activity = activity;
    }



    @Override
    protected void convert(BaseViewHolder helper, Courses item) {


        helper.setText(R.id.tv_name, item.getCourseName())
                .setText(R.id.tv_number, String.format("已有%d人学习过", item.getPageView()))
                .setText(R.id.tv_product, String.format("￥%s", item.getCoursePrice()))
                .setText(R.id.tv_time, String.format("时长：%s分钟", "100"));

        Glide.with(activity)
                .load(item.getCourseImg())
                .into((ImageView) helper.getView(R.id.iv_pic));


    }


}