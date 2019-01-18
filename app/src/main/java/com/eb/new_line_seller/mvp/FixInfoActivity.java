package com.eb.new_line_seller.mvp;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.mvp.contacts.FixInfoContacts;
import com.eb.new_line_seller.mvp.presenter.FixInfoPtr;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class FixInfoActivity extends BaseActivity<FixInfoContacts.FixInfoPtr> implements FixInfoContacts.FixInfoUI {


    @BindView(R.id.tv_car_no)
    TextView tv_car_no;

    @BindView(R.id.tv_fix_sn)
    TextView tv_fix_sn;

    @BindView(R.id.tv_new_order)
    TextView tv_new_order;

    @BindView(R.id.tv_text)
    TextView tv_text;//总价


    @BindView(R.id.tv_dec)
    TextView tv_dec;//车况描述


    @BindView(R.id.tv_price1)
    TextView tv_price1;//工时小计


    @BindView(R.id.tv_price2)
    TextView tv_price2;//配件


    @BindView(R.id.iv_add1)
    ImageView iv_add1;//按钮1


    @BindView(R.id.iv_add2)
    ImageView iv_add2;//


    @BindView(R.id.rv)
    RecyclerView rv;//工时

    @BindView(R.id.rv2)
    RecyclerView rv2;//服务


    @OnClick({R.id.iv_add1, R.id.iv_add2, R.id.tv_new_order})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add1:
                //添加工时
                toActivity(FixPickServiceActivity.class);
                break;

            case R.id.iv_add2:
                //添加配件
                toActivity(FixPickPartsActivity.class);
                break;


            case R.id.tv_new_order:
                //生成估价单

                getPresenter().onInform();
                break;
        }
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info;
    }

    @Override
    protected void init() {
        tv_title.setText("汽车检修单");
        getPresenter().initRecyclerView(rv, rv2);
        getPresenter().getInfo();
    }


    @Override
    public FixInfoContacts.FixInfoPtr onBindPresenter() {
        return new FixInfoPtr(this);
    }

    @Override
    public void setInfo(FixInfoEntity fixInfo) {

        tv_car_no.setText(fixInfo.getCarNo());
        tv_fix_sn.setText("单号：" + fixInfo.getQuotationSn());
        tv_dec.setText(fixInfo.getDescribe());


    }

    @Override
    public void createOrderSuccess() {
        ToastUtils.showToast("生成成功！");
        finish();
    }

    @Override
    public void setServicePrice(String price) {
        tv_price1.setText("金额小计：￥" + MathUtil.twoDecimal(price));

    }

    @Override
    public void setPartsPrice(String price) {
        tv_price2.setText("金额小计：￥" + MathUtil.twoDecimal(price));


    }

    @Override
    public void setAllPrice(String price) {
        tv_text.setText("总价：￥" + MathUtil.twoDecimal(price));
    }

    @Override
    public void showAddButton() {
        iv_add1.setVisibility(View.VISIBLE);
        iv_add2.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddButton() {
        iv_add1.setVisibility(View.INVISIBLE);
        iv_add2.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setButtonText(String text) {
        tv_new_order.setText(text);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPresenter().handleCallback(intent);
    }
}
