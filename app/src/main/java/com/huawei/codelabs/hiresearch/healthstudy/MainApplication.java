package com.huawei.codelabs.hiresearch.healthstudy;

import android.app.Application;

import com.huawei.hiresearch.skin.HiResearch;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HiResearch.init(this, "b03579cb");
    }
}
