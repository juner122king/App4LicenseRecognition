package com.eb.geaiche.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.RolesAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.CommonPopupWindow;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Roles;
import com.juner.mvp.bean.Technician;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StaffInfoFixActivity extends BaseActivity {

    @BindView(R.id.tv_phone)
    EditText tv_phone;

    @BindView(R.id.tv_name)
    EditText tv_name;

    @BindView(R.id.tv_number)
    EditText tv_number;

    @BindView(R.id.tv1)
    TextView tv1;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_staff_info_fix;
    }

    Technician sysUser = new Technician();

    int type;//页面类型  修改，添加

    List<Integer> roleList = new ArrayList<>();

    @Override
    protected void init() {
        type = getIntent().getIntExtra("type", 0);


        if (type == 0) {
            tv_title.setText("修改员工信息");
            setRTitle("权限说明");

            sysUser = getIntent().getParcelableExtra("sysUser");
            tv_phone.setText(sysUser.getMobile());
            tv_name.setText(sysUser.getUsername());
            tv_number.setText(sysUser.getUserSn());
            roleList = sysUser.getRoleList();
        } else {
            tv_title.setText("添加员工信息");
            setRTitle("权限说明");
        }


    }

    @OnClick({R.id.tv_title_r, R.id.tv_cancel, R.id.tv_enter, R.id.tv1})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_title_r:

                break;
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_enter:
                setInfo();


                break;
            case R.id.tv1:
                //选择职位
                //功能按钮

                popupWindow.showAsDropDown(v, -10, 0);

                break;
        }
    }

    RolesAdapter adapter;//弹出框
    CommonPopupWindow popupWindow;

    @Override
    protected void setUpView() {


        View ll = getLayoutInflater().inflate(R.layout.popup3_rv, null);
        RecyclerView rv = ll.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RolesAdapter(null);
        rv.setAdapter(adapter);


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {
                roleList.clear();
                tv1.setText(adapter.getData().get(position).getRoleName());
                int roleId = adapter.getData().get(position).getRoleId();

                roleList.add(roleId);

                popupWindow.dismiss();
            }

        });

        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(ll)
                .create();
    }

    @Override
    protected void setUpData() {

        Api().queryRoles().subscribe(new RxSubscribe<List<Roles>>(this, true) {
            @Override
            protected void _onNext(List<Roles> roles) {

                adapter.setNewData(roles);
                for (Roles roles1 : roles) {
                    for (int i : roleList) {
                        if (roles1.getRoleId() == i) {
                            tv1.setText(roles1.getRoleName());
                            break;
                        }

                    }
                }

                if (type == 1) {
                    tv1.setText(roles.get(0).getRoleName());
                    roleList.add(roles.get(0).getRoleId());
                }
            }

            @Override
            protected void _onError(String message) {

            }
        });

    }


    private void setInfo() {

        if (TextUtils.isEmpty(tv_name.getText())) {
            ToastUtils.showToast("名字不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tv_phone.getText())) {
            ToastUtils.showToast("手机号不能为空！");
            return;
        }
//        if (TextUtils.isEmpty(tv_number.getText())) {
//            ToastUtils.showToast("工号不能为空！");
//            return;
//        }

        sysUser.setUsername(tv_name.getText().toString());
        sysUser.setMobile(tv_phone.getText().toString());
        sysUser.setUserSn(tv_number.getText().toString());
        sysUser.setRoleList(roleList);

        if (type == 0)
            sysuserUpdate();
        else
            sysuserSave();

    }

    private void sysuserUpdate() {
        Api().sysuserUpdate(sysUser).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity entity) {
                ToastUtils.showToast("修改成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("修改失败！" + message);
                finish();
            }
        });

    }

    private void sysuserSave() {

        Api().sysuserSave(sysUser).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity entity) {
                ToastUtils.showToast("添加成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("添加失败！" + message);
                finish();
            }
        });

    }


}
