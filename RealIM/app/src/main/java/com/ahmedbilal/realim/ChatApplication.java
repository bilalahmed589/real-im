package com.ahmedbilal.realim;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Ashi_3 on 5/2/2015.
 */
public class ChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "I0chGdodqu6WDSiTTVdSaOpNdPLd51PrCI51A5Ol", "QbCsaksXw5Q1P29ztBHjexCKemQErT7jS28Mq4Fj");
    }
}
