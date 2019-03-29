package com.eb.geaiche.api;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.bean.RecordMeal;
import com.juner.mvp.Configure;
import com.eb.geaiche.MyApplication;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.bean.ActivityEntity;
import com.juner.mvp.bean.ActivityPage;
import com.eb.geaiche.bean.AutoBrand;
import com.juner.mvp.bean.AutoModel;
import com.juner.mvp.bean.BankList;
import com.juner.mvp.bean.Banner;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.BillEntity;
import com.juner.mvp.bean.CarCheckResul;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.CarVin;
import com.juner.mvp.bean.Card;
import com.juner.mvp.bean.CategoryBrandList;
import com.juner.mvp.bean.CheckOptions;
import com.juner.mvp.bean.Coupon;
import com.juner.mvp.bean.Course;
import com.juner.mvp.bean.CourseRecord;
import com.juner.mvp.bean.Courses;
import com.juner.mvp.bean.FixInfoList;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsListEntity;
import com.eb.geaiche.bean.Meal;

import com.juner.mvp.bean.Member;
import com.juner.mvp.bean.MemberOrder;
import com.juner.mvp.bean.MyBalanceEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.NumberBean;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.OrderInfoEntity;
import com.juner.mvp.bean.OrderNews;
import com.juner.mvp.bean.ProductList;
import com.juner.mvp.bean.QueryByCarEntity;
import com.juner.mvp.bean.Roles;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.juner.mvp.bean.ServerList;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.ShopCar;
import com.juner.mvp.bean.ShopCarBane;
import com.juner.mvp.bean.Technician;
import com.juner.mvp.bean.TechnicianInfo;
import com.juner.mvp.bean.Token;
import com.juner.mvp.bean.UserBalanceAuthPojo;
import com.juner.mvp.bean.VersionInfo;
import com.juner.mvp.bean.VinImageBody;
import com.juner.mvp.bean.WeixinCode;
import com.juner.mvp.bean.WorkIndex;
import com.eb.geaiche.util.ToastUtils;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ApiLoader {

    private ApiService apiService;

    Map<String, Object> map = new HashMap<>();
    String token;

    public ApiLoader(Context context) {

        token = new AppPreferences(context).getString(Configure.Token, "");
        Log.i("apiService", "X-Nideshop-Token:  " + token);
        apiService = RetrofitServiceManager.getInstance().create(ApiService.class);
        map.put("X-Nideshop-Token", new AppPreferences(context).getString(Configure.Token, ""));
    }

    /**
     * 1.拍照接单自动查找订单或车况
     *
     * @return
     */
    public Observable<QueryByCarEntity> queryByCar(String car_no) {
        map.put("car_no", car_no);


        return apiService.queryByCar(map).compose(RxHelper.<QueryByCarEntity>observe());

    }

    /**
     * 账单统计加列表
     *
     * @return
     */
    public Observable<BillEntity> getUserBillList(int isShowAll, int page) {

        //如需分页则添加	分页参数（int page，int limit，String sidx，String order）
        map.put("page", page);
        map.put("limit", Configure.limit_page);
        List<Integer> list = new ArrayList<>();
        if (isShowAll == 0) {
            list.add(3);
            list.add(4);

        } else {
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);
        }

        map.put("type", list);
        return apiService.getUserBillList(map).compose(RxHelper.<BillEntity>observe());

    }

    /**
     * 账单统计加列表 收入账单 与我的账单列表一个接口，多一个参数
     *
     * @param isdate 是否用时间
     * @return
     */
    public Observable<BillEntity> getUserBillList(Date after, Date before, boolean isdate, int isShowAll, int page) {


        //选日期需要添加，不添加默认取本月	Date before, Date after
        if (isdate) {
            map.put("before", before.getTime());
            map.put("after", after.getTime());
        }

        return getUserBillList(isShowAll, page);

    }


    /**
     * 会员录入
     *
     * @return
     */
    public Observable<SaveUserAndCarEntity> saveUserAndCar(String car_no, String mobile, String username) {

        map.put("car_no", car_no);
        map.put("mobile", mobile);
        map.put("username", username);
        return apiService.saveUserAndCar(map).compose(RxHelper.<SaveUserAndCarEntity>observe());
    }

    /**
     * 2.会员快捷录入
     *
     * @return
     */
    public Observable<SaveUserAndCarEntity> addUser(String mobile, String username) {

        map.put("mobile", mobile);
        map.put("username", username);

        return apiService.addUser(map).compose(RxHelper.<SaveUserAndCarEntity>observe());
    }

    /**
     * 7.新增车况
     *
     * @return
     */
    public Observable<NullDataEntity> addCarInfo(CarInfoRequestParameters parameters) {

        return apiService.addCarInfo(token, parameters).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 8.修改车况
     *
     * @return
     */
    public Observable<NullDataEntity> fixCarInfo(CarInfoRequestParameters parameters) {

        return apiService.fixCarInfo(token, parameters).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 4.批量删除车况图片
     *
     * @return
     */
    public Observable<NullDataEntity> delete(List<Integer> integers) {

        return apiService.delete(token, integers).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 4.批量删除车况图片
     *
     * @return
     */
    public Observable<NullDataEntity> delete(int id) {


        List<Integer> integers = new ArrayList<>();
        integers.add(id);


        return delete(integers);


    }


    /**
     * 确认下单
     *
     * @return
     */
    public Observable<OrderInfo> submit(OrderInfoEntity infoEntity) {


        return apiService.submit(token, infoEntity).compose(RxHelper.<OrderInfo>observe());
    }


    /**
     * 订单修改 orderInfo类
     *
     * @return
     */
    public Observable<OrderInfo> remake(OrderInfoEntity infoEntity) {


        return apiService.remake(token, infoEntity).compose(RxHelper.<OrderInfo>observe());
    }


    /**
     * 4.开始服务(修改订单状态为服务中)
     *
     * @return
     */
    public Observable<NullDataEntity> beginServe(int order_id, String order_sn, String district) {

        map.put("order_id", order_id);
        map.put("order_sn", order_sn);
        map.put("district", district);
        return apiService.beginServe(map).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 14.车况详情显示
     *
     * @param id 车况主键
     * @return
     */
    public Observable<CarInfoRequestParameters> showCarInfo(int id) {

        map.put("id", id);

        return apiService.showCarInfo(map).compose(RxHelper.<CarInfoRequestParameters>observe());
    }


    /**
     * 15.当前门店用户（技师）列表
     *
     * @return
     */
    public Observable<BasePage<Technician>> sysuserList() {
        map.put("limit", 100);//页数
        return apiService.sysuserList(map).compose(RxHelper.<BasePage<Technician>>observe());
    }


    /**
     * 服务工时列表 ps：与商品分类一样，初始返回了第一种类的显示服务
     */
    public Observable<CategoryBrandList> categoryServeList() {
        return apiService.categoryServeList(map).compose(RxHelper.<CategoryBrandList>observe());
    }

    /**
     * 门店服务列表
     */
    public Observable<ServerList> goodsServeList() {
        return apiService.goodsServeList(token).compose(RxHelper.<ServerList>observe());
    }

    /**
     * 9.分类下品牌列表加第一个品牌第一页下商品
     *
     * @return
     */
    public Observable<CategoryBrandList> categoryBrandList() {
        return apiService.categoryBrandList(map).compose(RxHelper.<CategoryBrandList>observe());
    }

    /**
     * 10.查询任意条件商品 目前主要存brand_id品牌，category_id类型，name商品名
     *
     * @return
     */
    public Observable<GoodsListEntity> queryAnyGoods(String category_id, String brand_id, String name) {


        map.put("attribute_category", category_id); //商品类别
        map.put("brand_id", brand_id);//品牌
        map.put("name", name);//查询关键字

        return apiService.queryAnyGoods(map).compose(RxHelper.<GoodsListEntity>observe());
    }

    /**
     * 10.查询任意条件商品 目前主要存brand_id品牌，category_id类型，name商品名
     *
     * @return
     */
    public Observable<GoodsListEntity> queryAnyGoods(String name) {


        map.put("name", name);//查询关键字

        return apiService.queryAnyGoods(map).compose(RxHelper.<GoodsListEntity>observe());
    }

    /**
     * 10.四个火热商品
     *
     * @return
     */
    public Observable<GoodsListEntity> shopeasyList() {

        return apiService.shopeasyList(map).compose(RxHelper.<GoodsListEntity>observe());
    }

    /**
     * 确认订单最后完成
     *
     * @return
     */
    public Observable<NullDataEntity> confirmFinish(int order_id) {

        map.put("order_id", order_id);
        return apiService.confirmFinish(map).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 确认支付
     *
     * @return
     */
    public Observable<NullDataEntity> confirmPay(OrderInfoEntity infoEntity) {

        return apiService.confirmPay(token, infoEntity).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList() {

        return apiService.orderList(map).compose(RxHelper.<BasePage<OrderInfoEntity>>observe());
    }


    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList(int position, int page) {
        map.clear();
        map.put("X-Nideshop-Token", token);
        map.put("page", page);
        map.put("limit", Configure.limit_page);
        switch (position) {
            case 0:
                break;
            case 1:
                map.put("order_status", "0");
                map.put("pay_status", "0");
                break;
            case 2:
                map.put("order_status", "0");
                map.put("pay_status", "2");
                break;
            case 3:
                map.put("order_status", "1");
                break;
            case 4:
                map.put("order_status", "2");
                map.put("pay_status", "2");

                break;


        }

        return apiService.orderList(map).compose(RxHelper.<BasePage<OrderInfoEntity>>observe());
    }

    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList(String name) {
        map.clear();
        map.put("X-Nideshop-Token", token);
        map.put("limit", Configure.limit_page);
        map.put("name", name);
        return apiService.orderList(map).compose(RxHelper.<BasePage<OrderInfoEntity>>observe());
    }


    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList4DayOfMoon(int page, int type) {
        map.clear();
        map.put("X-Nideshop-Token", token);
        map.put("page", page);
        map.put("limit", Configure.limit_page);

        if (type == 0) {
            map.put("dateStart", System.currentTimeMillis() / 1000 * 1000);//当前
            map.put("dateEnd", System.currentTimeMillis() / 1000 * 1000 + 60 * 60 * 24 * 1000);
        } else {
            Calendar startDate = Calendar.getInstance();
            startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), 1, 0, 0, 0);
            map.put("dateStart", startDate.getTime().getTime() / 1000 * 1000);//当前
//            map.put("dateEnd", System.currentTimeMillis() / 1000 * 1000 + 60 * 60 * 24 * 1000);
        }

        return apiService.orderList(map).compose(RxHelper.<BasePage<OrderInfoEntity>>observe());
    }


    /**
     * 订单详情页
     *
     * @returnD
     */
    public Observable<OrderInfo> orderDetail(int id) {

        map.put("id", id);
        return apiService.orderDetail(map).compose(RxHelper.<OrderInfo>observe());
    }

    /**
     * 账单详情（同订单详情一个接口，入参不同）
     *
     * @returnD
     */
    public Observable<OrderInfo> orderDetail(String order_sn) {

        map.put("order_sn", order_sn);
        return apiService.orderDetail(map).compose(RxHelper.<OrderInfo>observe());
    }


    /**
     * 活动列表
     */
    public Observable<ActivityPage> activityList(int activity_type) {

//        map.put("activity_type", activity_type);//=1.平台活动 =3.门店活动
//        map.put("activity_name", activity_name);//查询关键字

        return apiService.activityList(map).compose(RxHelper.<ActivityPage>observe());
    }

    /**
     * 活动详情
     */
    public Observable<ActivityEntity> activityDetail(int id) {

        map.put("id", id);

        return apiService.activityDetail(map).compose(RxHelper.<ActivityEntity>observe());
    }

//    /**
//     * 活动详情
//     */
//    public Observable<ActivityEntityItem> activityDetail(int id) {
//
//        map.put("id", id);
//
//        return apiService.activityDetail(map).compose(RxHelper.<ActivityEntityItem>observe());
//    }

    /**
     * 品牌查询列表
     */
    public Observable<List<AutoBrand>> listByName() {

        return apiService.listByName(token).compose(RxHelper.<List<AutoBrand>>observe());
    }

    /**
     * 通过品牌查车型列表
     */
    public Observable<List<AutoModel>> listByBrand(int brand_id) {

        map.put("brand_id", brand_id);
        return apiService.listByBrand(map).compose(RxHelper.<List<AutoModel>>observe());
    }


    /**
     * 工作台首页
     */
    public Observable<WorkIndex> workIndex() {

        return apiService.workIndex(map).compose(RxHelper.<WorkIndex>observe());
    }


    /**
     * 会员管理页面数据
     */
    public Observable<Member> memberList(int page, String name) {

        map.put("page", page);
        map.put("limit", Configure.limit_page);
        map.put("name", name);
        return apiService.memberList(map).compose(RxHelper.<Member>observe());
    }


    /**
     * 查看会员信息及订单记录
     */
    public Observable<MemberOrder> memberOrderList(int user_id) {

        map.put("user_id", user_id);

        return apiService.memberOrderList(map).compose(RxHelper.<MemberOrder>observe());
    }

    /**
     * 修改用户名
     */
    public Observable<NullDataEntity> remakeUserName(String user_name, int user_id) {

        map.put("user_name", user_name);
        map.put("user_id", user_id);
        return apiService.remakeUserName(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 门店信息
     */
    public Observable<Shop> shopInfo() {
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        return apiService.shopInfo(map).compose(RxHelper.<Shop>observe());
    }

    /**
     * 工作台信息
     */
    public Observable<List<Banner>> getWorkHeaderAd() {
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        return apiService.getWorkHeaderAd(map).compose(RxHelper.<List<Banner>>observe());
    }

    /**
     * 未读新消息数量
     */
    public Observable<Integer> needRead() {
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        return apiService.needRead(map).compose(RxHelper.<NumberBean>observeNub());
    }


    /**
     * 未读新消息list
     */
    public Observable<List<OrderNews>> pushlogList(int page) {
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        map.put("page", page);
        return apiService.pushlogList(map).compose(RxHelper.<List<OrderNews>>observe());
    }

    /**
     * 标为已读
     */
    public Observable<NullDataEntity> updateRead(int id) {
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        map.put("id", id);
        return apiService.updateRead(map).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 获取优惠券列表 [达到满减，未到期，未用过]
     */
    public Observable<List<Coupon>> couponList(String order_price, int user_id) {

        map.put("order_price", order_price);
        map.put("user_id", user_id);

        return apiService.couponList(map).compose(RxHelper.<List<Coupon>>observe());
    }


    /**
     * 获取优惠券列表 [达到满减，未到期，未用过]
     */
    public Observable<MyBalanceEntity> balanceInfo() {

        return apiService.balanceInfo(map).compose(RxHelper.<MyBalanceEntity>observe());
    }

    /**
     * 课程列表
     */
    public Observable<List<Course>> courseList(int course_type) {
        // course_type	1为线下 2为线上
        map.put("course_type", course_type);
        return apiService.courseList(map).compose(RxHelper.<List<Course>>observe());
    }

    /**
     * 课程报名
     */
    public Observable<NullDataEntity> coursejoinnameSave(String name, String mobile) {

        map.put("name", name);
        map.put("mobile", mobile);
        return apiService.coursejoinnameSave(map).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 添加反馈
     */
    public Observable<String> feedbackSave(String content) {

        map.put("content", content);
        return apiService.feedbackSave(map).compose(RxHelper.<String>observe());
    }


    /**
     * 短信验证码
     */
    public Observable<NullDataEntity> smsSendSms(String mobile, int type) {

        map.put("mobile", mobile);
        map.put("type", type);//1登陆2体现3银行卡验证
        return apiService.smsSendSms(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 短信验证码
     */
    public Disposable smsSendSms(String mobile, int type, final TextView tv, final Context context) {
        final String TAG = "短信验证码:";
        final int con = 60;

        map.put("mobile", mobile);
        map.put("type", type);//1登陆2体现3银行卡验证
        final Disposable[] disposable = new Disposable[1];

        apiService.smsSendSms(map).compose(RxHelper.<NullDataEntity>observe()).subscribe(new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                ToastUtils.showToast("验证码已发送");
                disposable[0] = Observable //计时器
                        .interval(0, 1, TimeUnit.SECONDS)
                        .take(con)//次数
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) {
                                Log.e(TAG, "onNext");
                                Long l = con - aLong;
                                tv.setText(l + "s");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                Log.e(TAG, "onError");
                                tv.setClickable(true);
                                throwable.printStackTrace();
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                Log.e(TAG, "onComplete");
                                tv.setText("获取验证码");
                                tv.setClickable(true);
                            }
                        }, new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) {
                                Log.e(TAG, "onSubscribe");
                                tv.setClickable(false);
                            }
                        });
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
        return disposable[0];
    }


    /**
     * 更改客户信息发送验证码
     */
    public Disposable updateUserSms(String mobile, final TextView tv, final View v, final Context context) {
        final String TAG = "短信验证码:";
        final int con = 60;

        map.put("mobile", mobile);
        final Disposable[] disposable = new Disposable[1];

        apiService.updateUserSms(map).compose(RxHelper.<NullDataEntity>observe()).subscribe(new RxSubscribe<NullDataEntity>(context, false) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                v.setVisibility(View.VISIBLE);
                disposable[0] = Observable //计时器
                        .interval(0, 1, TimeUnit.SECONDS)
                        .take(con)//次数
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) {
                                Log.e(TAG, "onNext");
                                Long l = con - aLong;
                                tv.setText(l + "s");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                Log.e(TAG, "onError");
                                tv.setClickable(true);
                                throwable.printStackTrace();
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                Log.e(TAG, "onComplete");
                                tv.setText("获取验证码");
                                tv.setClickable(true);
                            }
                        }, new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) {
                                Log.e(TAG, "onSubscribe");
                                tv.setClickable(false);
                            }
                        });
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
        return disposable[0];
    }

    /**
     * 银行卡验证短信
     */
    public Observable<NullDataEntity> sendBankSms() {
        return apiService.sendBankSms(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 登录
     */
    public Observable<Token> login(String mobile, String et_car_code) {
        map.put("mobile", mobile);
        map.put("authCode", et_car_code);
        return apiService.login(map).compose(RxHelper.<Token>observe());
    }


    /**
     * 添加银行卡
     */
    public Observable<NullDataEntity> bankSave(Card card) {
        return apiService.bankSave(token, card).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 微信收款码支付
     */
    public Observable<WeixinCode> prepay(OrderInfoEntity infoEntity) {


        return apiService.prepay(token, infoEntity).compose(RxHelper.<WeixinCode>observe());
    }

    /**
     * 查微信支付成功通知
     */
    public Observable<NullDataEntity> payQuery(int orderId) {
        map.put("id", orderId);
        return apiService.payQuery(map).compose(RxHelper.<NullDataEntity>observe());

    }


    /**
     * 添加快捷主推项目
     */
    public Observable<NullDataEntity> shopeasySave(GoodsEntity setProject) {
        setProject.setType(1);
        return apiService.shopeasySave(token, setProject).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 修改快捷主推项目
     */
    public Observable<Integer> shopeasyUpdate(GoodsEntity setProject) {
        setProject.setType(1);
        return apiService.shopeasyUpdate(token, setProject).compose(RxHelper.<Integer>observe());
    }

    /**
     * 用户可用套餐列表
     */
    public Observable<Meal> queryUserAct(int user_id, String car_no) {
        map.put("user_id", user_id);
        map.put("car_no", car_no);
        return apiService.queryUserAct(map).compose(RxHelper.<Meal>observe());
    }

    /**
     * 申请提现
     */
    public Observable<NullDataEntity> ask(UserBalanceAuthPojo maps) {
        return apiService.ask(token, maps).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 申请提现
     */
    public Observable<ProductList> sku(int id) {
        map.put("id", id);
        return apiService.sku(map).compose(RxHelper.<ProductList>observe());
    }

    /**
     * 查看银行卡 type=1申请中 2申请成功3申请失败
     */
    public Observable<BankList> bankList() {

        return apiService.bankList(token).compose(RxHelper.<BankList>observe());
    }


    /**
     * 车牌识别
     */
    public Observable<CarNumberRecogResult> carLicense(String pic) {

        return apiService.carLicense(Configure.carNumberRecognition, pic).compose(RxHelper.<CarNumberRecogResult>observe2());
    }


    /**
     * 车辆vin识别
     */
    public Observable<CarNumberRecogResult> carVinLicense(String pic) {

        return apiService.carVinLicense(Configure.carVinRecognition, new VinImageBody(pic)).compose(RxHelper.<CarNumberRecogResult>observeVin());
    }

    /**
     * 车辆vin信息查询
     */
    public Observable<CarVin> carVinInfoQuery(String vin) {

        return apiService.carVinInfoQuery(Configure.carVinInfo, vin).compose(RxHelper.<CarVin>observeVin());
    }


    /**
     * 报价单列表条件查询
     */
    public Observable<FixInfoList> quotationList(int status, int page) {


        if (status == -1)
            return apiService.quotationList(token, page, Configure.limit_page).compose(RxHelper.<FixInfoList>observe());
        else
            return apiService.quotationList(token, status, page, Configure.limit_page).compose(RxHelper.<FixInfoList>observe());
    }

    /**
     * 报价单取消
     */
    public Observable<NullDataEntity> quotationCancle(int id) {


        return apiService.quotationCancle(token, id).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 纸卡录入历史记录（与用户可用套餐查询返回的格式相仿）
     */
    public Observable<RecordMeal> queryConnectAct(String name) {
        return apiService.queryConnectAct(token, name).compose(RxHelper.<RecordMeal>observe());

    }

    /**
     * 纸卡录入历史记录（与用户可用套餐查询返回的格式相仿）
     */
    public Observable<RecordMeal> queryConnectAct(int page) {
        return apiService.queryConnectAct(token, page).compose(RxHelper.<RecordMeal>observe());

    }

    /**
     * 课程列表
     */
    public Observable<List<Courses>> courseList2(String name, String course_type) {


        if (course_type.equals("0"))
            return apiService.courseList2(token, 1000).compose(RxHelper.<List<Courses>>observe());
        else
            return apiService.courseList2(token, name, course_type, 1000).compose(RxHelper.<List<Courses>>observe());
    }

    /**
     * 课程学习记录列表
     */
    public Observable<List<CourseRecord>> courseRecord() {
        return apiService.courseRecord(token, 1000).compose(RxHelper.<List<CourseRecord>>observe());
    }

    /**
     * 课程列表
     */
    public Observable<List<Courses>> courseListSearch(String name) {
        return apiService.courseList2(token, name, null, 1000).compose(RxHelper.<List<Courses>>observe());
    }

    /**
     * 用户点击视频观看退出时访问，用来增加记录
     */
    public Observable<NullDataEntity> addWatchLog(CourseRecord courseRecord) {
        return apiService.addWatchLog(token, courseRecord).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 检查版本更新
     */
    public Observable<VersionInfo> checkVersionUpDate() {
        return apiService.checkVersionUpDate(token).compose(RxHelper.<VersionInfo>observe());
    }

    /**
     * 获取员工详情
     */
    public Observable<TechnicianInfo> sysuserDetail(int id) {
        return apiService.sysuserDetail(token, id).compose(RxHelper.<TechnicianInfo>observe());

    }

    /**
     * 修改员工
     */
    public Observable<NullDataEntity> sysuserUpdate(Technician technicianInfo) {
        return apiService.sysuserUpdate(token, technicianInfo).compose(RxHelper.<NullDataEntity>observe());

    }

    /**
     * 修改员工
     */
    public Observable<NullDataEntity> sysuserSave(Technician technicianInfo) {
        return apiService.sysuserSave(token, technicianInfo).compose(RxHelper.<NullDataEntity>observe());

    }

    /**
     * 供选择的角色列表
     */
    public Observable<List<Roles>> queryRoles() {
        return apiService.queryRoles(token).compose(RxHelper.<List<Roles>>observe());

    }

    /**
     * 获取员工销售订单
     */
    public Observable<List<OrderInfoEntity>> saleList(int id) {
        map.put("limit", 100);
        map.put("sysuser_id", id);

        return apiService.saleList(map).compose(RxHelper.<List<OrderInfoEntity>>observe());
    }

    /**
     * 获取员工服务订单
     */
    public Observable<List<OrderInfoEntity>> sysOrderList(int id) {
        map.put("limit", 100);
        map.put("user_id", id);
        return apiService.sysOrderList(map).compose(RxHelper.<List<OrderInfoEntity>>observe());
    }


    /**
     * 更改客户信息发送验证码
     */
    public Observable<NullDataEntity> updateUserSms(String mobile) {

        map.put("mobile", mobile);
        return apiService.updateUserSms(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 更改客户信息
     */
    public Observable<NullDataEntity> remakeUserName(int user_id, String user_name, String authCode, String mobile) {

        map.put("mobile", mobile);
        map.put("user_id", user_id);
        map.put("user_name", user_name);
        map.put("authCode", authCode);
        return apiService.remakeUserName(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 门店检修报告选项列表
     */
    public Observable<List<CheckOptions>> queryCheckOptions() {

        return apiService.queryCheckOptions(token).compose(RxHelper.<List<CheckOptions>>observe());
    }


    /**
     * 暂存或者生成检测报告
     */
    public Observable<NullDataEntity> checkOutResult(CarCheckResul checkResul) {

        return apiService.checkOutResult(token, checkResul).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 检测报告单列表
     */
    public Observable<List<CarCheckResul>> checkResultList(int car_id) {
        if (car_id != -1)
            map.put("car_id", car_id);
        map.put("limit", 100);//页数
        return apiService.checkResultList(map).compose(RxHelper.<List<CarCheckResul>>observe());
    }


    /**
     * 检测报告单结果详情
     */
    public Observable<CarCheckResul> checkResultDetail(int id) {
        map.put("id", id);
        return apiService.checkResultDetail(map).compose(RxHelper.<CarCheckResul>observe());
    }


    /**
     * 修改暂存的门店检测报告 type为1的不可访问此接口
     */
    public Observable<NullDataEntity> updateCheckResult(CarCheckResul checkResul) {

        return apiService.updateCheckResult(token, checkResul).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 门店服务过的车辆管理
     */

    public Observable<ShopCarBane> shopCarList(String key, int page) {
        if (!key.equals(""))
            map.put("name", key);
        map.put("limit", Configure.limit_page);//页数
        map.put("page", page);
        return apiService.shopCarList(map).compose(RxHelper.<ShopCarBane>observe());
    }


    /**
     * 获取商品分类
     */
    public Observable<List<GoodsCategory>> queryShopcategoryAll() {

        return apiService.queryShopcategoryAll(token).compose(RxHelper.<List<GoodsCategory>>observe());
    }


}