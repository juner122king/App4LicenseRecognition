package com.eb.new_line_seller.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.mvp.contacts.FixPickServiceContacts;
import com.eb.new_line_seller.mvp.presenter.FixPickServicePtr;
import com.juner.mvp.bean.FixServie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.new_line_seller.mvp.presenter.FixInfoPtr.TYPE;
import static com.eb.new_line_seller.mvp.presenter.FixInfoPtr.TYPE_Service;


//选择服务工时
public class FixPickServiceActivity extends BaseActivity<FixPickServiceContacts.FixPickServicePtr> implements FixPickServiceContacts.FixPickServiceUI {


    @BindView(R.id.rg_type)
    RadioGroup rg_type;//一级

    @BindView(R.id.rv0)
    RecyclerView rv0;//二级

    @BindView(R.id.rv1)
    RecyclerView rv1;//三级

    @BindView(R.id.price)
    TextView price;//总价格

    @BindView(R.id.et_key)
    EditText et_key;//


    @OnClick({R.id.tv_confirm, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                //确认选择
                getPresenter().confirm();
                break;

            case R.id.iv_search:
                //搜索
                getPresenter().seekServerforKey(et_key.getText().toString());
                break;


        }


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_pick_service;
    }

    @Override
    protected void init() {
        tv_title.setText("请选择服务工时");
        tv_title_r.setText("自定义工时");
        getPresenter().initRecyclerView(rv0, rv1);
        getPresenter().onGetData(rg_type);
    }

    @Override
    public FixPickServiceContacts.FixPickServicePtr onBindPresenter() {
        return new FixPickServicePtr(this);
    }


    @Override
    public void showServiceList() {

        rv0.setVisibility(View.GONE);
        rv1.setVisibility(View.VISIBLE);


    }

    @Override
    public void showService2List() {
        rv0.setVisibility(View.VISIBLE);
        rv1.setVisibility(View.GONE);
    }

    @Override
    public void setPickAllPrice(String prices) {
        price.setText(prices);
    }

    @Override
    public void onConfirm(List<FixServie> fixServies) {


        Intent intent = new Intent(this, FixInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TYPE_Service, (ArrayList) fixServies);
        bundle.putString(TYPE, TYPE_Service);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
