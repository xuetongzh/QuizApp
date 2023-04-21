package com.google.quizapp;

import android.app.Application;

import com.kongzue.dialogx.DialogX;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DialogX.init(this);
    }
}
