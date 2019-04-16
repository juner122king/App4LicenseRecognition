package com.eb.geaiche.activity.fragment;


import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.AboutActivity;
import com.eb.geaiche.activity.AuthenActivity;
import com.eb.geaiche.activity.ChangeStoreActivity;
import com.eb.geaiche.activity.CourseRecordActivity;
import com.eb.geaiche.activity.MyBalanceActivity;
import com.eb.geaiche.activity.SetProjectActivity;
import com.eb.geaiche.activity.ShopInfoActivity;
import com.eb.geaiche.activity.UserReportActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.LoginActivity2;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.VersionInfo;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;

import static com.juner.mvp.Configure.shop_address;
import static com.juner.mvp.Configure.shop_name;
import static com.juner.mvp.Configure.shop_phone;
import static com.juner.mvp.Configure.shop_user_name;

/**
 * 主页页面：我的
 */
public class MainFragment5New extends BaseFragment {
    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.iv_user_pic)
    ImageView iv_user_pic;

    @BindView(R.id.tv_phone_number)
    TextView tv_phone_number;

    @BindView(R.id.updata)
    TextView updata;//版本号

    @BindView(R.id.tv_change_store)
    TextView tv_change_store;//
    String phone;

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment5_main;
    }

    @Override
    protected void setUpView() {
        updata.append(SystemUtil.packaGetName());


    }

    @Override
    protected void onVisible() {
        super.onVisible();
        phone = new AppPreferences(getContext()).getString(Configure.moblie_s, "");
        tv_phone_number.setText("手机号码：" + phone);
        //超级管理员权限
        if (phone.contains("123456789") || phone.equals("13412513007") || phone.equals("13602830779") || phone.equals("13826241081")) {//老板:13602830779
            tv_change_store.setVisibility(View.VISIBLE);
        } else {
            tv_change_store.setVisibility(View.GONE);
        }

        Api().shopInfo().subscribe(new RxSubscribe<Shop>(getContext(), true) {
            @Override
            protected void _onNext(Shop shop) {
                tv_name.setText(shop.getShop().getShopName());


                new AppPreferences(getContext()).put(shop_name, shop.getShop().getShopName());
                new AppPreferences(getContext()).put(shop_address, shop.getShop().getAddress());
                new AppPreferences(getContext()).put(shop_phone, shop.getShop().getPhone());
                new AppPreferences(getContext()).put(shop_user_name, shop.getShop().getName());


                Glide.with(getActivity())//门店图片
                        .load(shop.getShop().getImage())
                        .into(iv_user_pic);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
                //判断是否是401 token失效
                SystemUtil.isReLogin(message, getActivity());
            }
        });

    }

    @OnClick({R.id.tv_my_balance, R.id.rl_to_info, R.id.auth, R.id.project, R.id.about, R.id.updata, R.id.tv_user_report, R.id.tv_out, R.id.tv_change_store, R.id.mystudy})
    public void onclick(View v) {

        switch (v.getId()) {

            case R.id.tv_my_balance:

                toActivity(MyBalanceActivity.class);

                break;

            case R.id.rl_to_info:

                toActivity(ShopInfoActivity.class);

                break;
            case R.id.auth:

                toActivity(AuthenActivity.class);

                break;
            case R.id.project:

                toActivity(SetProjectActivity.class);

                break;
            case R.id.about:

                toActivity(AboutActivity.class);

                break;
            case R.id.updata:


                checkVersionUpDate();

                break;
            case R.id.tv_user_report:

                toActivity(UserReportActivity.class);

                break;

            case R.id.tv_out:
                new AppPreferences(getContext()).remove(Configure.Token);
                toActivity(LoginActivity2.class);
                getActivity().finish();
                break;

            case R.id.tv_change_store:

                toActivity(ChangeStoreActivity.class);
                break;
            case R.id.mystudy:
                toActivity(CourseRecordActivity.class);

                break;


        }


    }

    public static final String TAG = "MainFragment5";

    @Override
    protected String setTAG() {
        return TAG;
    }

    //检查版本更新
    private void checkVersionUpDate() {
        Api().checkVersionUpDate().subscribe(new RxSubscribe<VersionInfo>(getActivity(), true) {
            @Override
            protected void _onNext(final VersionInfo versionInfo) {

                if (versionInfo.getVersionCode() > SystemUtil.packaGetCode()) {

                    //弹出对话框
                    final ConfirmDialogCanlce confirmDialog = new ConfirmDialogCanlce(getActivity(), String.format("检测到新版本:v%s 是否更新？", versionInfo.getVersionName()), "系统消息");
                    confirmDialog.show();
                    confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            confirmDialog.dismiss();

                            starDownload(versionInfo);
                            ToastUtils.showToast("下载中...");

                        }

                        @Override
                        public void doCancel() {
                            confirmDialog.dismiss();
                            getActivity().finish();
                        }
                    });

                } else {
                    ToastUtils.showToast("当前已是最新版本");
                }


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    private void starDownload(VersionInfo versionInfo) {
        String apkPath = String.valueOf(getString(R.string.app_name) + "-v" + versionInfo.getVersionName() + "_upDate" + ".apk");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionInfo.getUrl()));
        request.setDescription("下载中");
        request.setTitle("软件更新");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

        }
        request.allowScanningByMediaScanner();//设置可以被扫描到
        request.setVisibleInDownloadsUi(true);// 设置下载可见
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//下载完成后通知栏任然可见
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, apkPath);
        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        // manager.enqueue(request);
        long Id = manager.enqueue(request);
        //listener(Id);
        SharedPreferences sPreferences = getActivity().getSharedPreferences(
                "downloadapk", 0);
        sPreferences.edit().putLong("apk", Id).commit();//保存此次下载ID

    }

}