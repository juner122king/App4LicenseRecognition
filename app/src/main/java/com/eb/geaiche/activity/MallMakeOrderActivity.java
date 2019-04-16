package com.eb.geaiche.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MallOrderGoodsListAdapter;
import com.eb.geaiche.api.RxSubscribe;

import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.util.ToastUtils;

import com.google.gson.Gson;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CartItem;

import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.XgxPurchaseOrderGoodsPojo;

import com.juner.mvp.bean.XgxPurchaseOrderPojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MallMakeOrderActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.all_price)
    TextView all_price;


    MallOrderGoodsListAdapter adapter;


    List<CartItem> cartItems = new ArrayList<>();//购物车页面传来的商品列表

    Shop shop;

    @OnClick({R.id.enter_pay})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.enter_pay://下单1
                makeOrder();
                break;
        }
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_make_order;
    }

    @Override
    protected void init() {

        tv_title.setText("确认订单");

        cartItems = getIntent().getParcelableArrayListExtra("cart_goods");


        all_price.setText("￥" + upDataPrice(cartItems));
    }

    @Override
    protected void setUpView() {
        adapter = new MallOrderGoodsListAdapter(cartItems, this);
        rv.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv.setAdapter(adapter);


    }

    @Override
    protected void setUpData() {
        getAddress();


    }

    //获取地址信息
    private void getAddress() {
        Api().shopInfo().subscribe(new RxSubscribe<Shop>(this, false) {
            @Override
            protected void _onNext(Shop s) {
                shop = s;
                name.setText(shop.getShop().getShopName());
                phone.setText(shop.getShop().getPhone());
                address.setText(shop.getShop().getAddress());


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
                //判断是否是401 token失效
                SystemUtil.isReLogin(message, MallMakeOrderActivity.this);
            }
        });

    }

    //生成订单
    private void makeOrder() {
        Api().mallMakeOrder(getOrderPojo()).subscribe(new RxSubscribe<Integer>(this, true) {
            @Override
            protected void _onNext(Integer t) {

                ToastUtils.showToast("订单生成成功！");

                finish();

                Intent intent = new Intent(MallMakeOrderActivity.this, MallMakeOrderInfoActivity.class);
                intent.putExtra(Configure.ORDERINFOID, t);
                intent.putExtra(Configure.shop_info, shop.getShop());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("cart_goods", (ArrayList<? extends Parcelable>) cartItems);
                intent.putExtras(bundle);

                startActivity(intent);


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
                //判断是否是401 token失效
                SystemUtil.isReLogin(message, MallMakeOrderActivity.this);
            }
        });


    }

    //生成下单对象
    private XgxPurchaseOrderPojo getOrderPojo() {
        XgxPurchaseOrderPojo pojo = new XgxPurchaseOrderPojo();
        String price = upDataPrice(cartItems);
        pojo.setPayType(1);
        pojo.setShopId(shop.getShop().getId());
        pojo.setDiscountPrice(null);//优惠金额
        pojo.setOrderPrice(price);//订单价格
        pojo.setRealPrice(price);//实付金额
        List<XgxPurchaseOrderGoodsPojo> goodsPojoLists = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            XgxPurchaseOrderGoodsPojo goodsPojo = new XgxPurchaseOrderGoodsPojo();
            goodsPojo.setGoodsId(String.valueOf(cartItem.getGoods_id()));
            goodsPojo.setGoodsPrice(cartItem.getRetail_product_price());
            goodsPojo.setGoodsStandardId(String.valueOf(cartItem.getProduct_id()));
            goodsPojo.setNumber(cartItem.getNumber());
            goodsPojoLists.add(goodsPojo);

        }

        pojo.setXgxPurchaseOrderGoodsPojoList(goodsPojoLists);


        return pojo;

    }


    //计算价格
    private String upDataPrice(List<CartItem> cartItems) {
        if (null == cartItems || cartItems.size() == 0)
            return "0.00";
        else {
            BigDecimal allPrice = new BigDecimal(0);

            for (CartItem cartItem : cartItems) {
                BigDecimal price = new BigDecimal(cartItem.getRetail_product_price());
                BigDecimal num = new BigDecimal(cartItem.getNumber());
                allPrice = allPrice.add(price.multiply(num));

            }
            return MathUtil.twoDecimal(allPrice.doubleValue());
        }
    }
}