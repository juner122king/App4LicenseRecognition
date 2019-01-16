package com.eb.new_line_seller.mvp.model;

import android.content.Context;

import com.eb.new_line_seller.mvp.contacts.FixPickServiceContacts;
import com.eb.new_line_seller.util.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.FixServiceList;

public class FixPickServiceMdl extends BaseModel implements FixPickServiceContacts.FixPickServiceMdl {
    Context context;

    public FixPickServiceMdl(Context context) {
        this.context = context;
    }


    @Override
    public void getServiceData(RxSubscribe<FixServiceList> rxSubscribe) {

        sendRequest(HttpUtils.getFix().serveHourList(getToken(context)).compose(RxHelper.<FixServiceList>observe()), rxSubscribe);

    }


}
