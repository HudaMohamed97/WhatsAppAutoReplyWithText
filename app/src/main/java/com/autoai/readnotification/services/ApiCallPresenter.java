package com.autoai.readnotification.services;

import android.content.Context;

import com.autoai.readnotification.MyServiceLisener;

import java.util.ArrayList;

public class ApiCallPresenter {

    private MyServiceLisener myServiceLisener;

    public ApiCallPresenter() {
    }

    public ApiCallPresenter(Context context, MyServiceLisener myServiceLisener) {
        this.myServiceLisener = myServiceLisener;
    }

    public void getNumber() {
        ArrayList<String> list = myServiceLisener.getList();
        list.get(list.size() - 1);

    }


}
