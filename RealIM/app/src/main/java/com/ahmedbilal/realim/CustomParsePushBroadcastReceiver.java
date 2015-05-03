package com.ahmedbilal.realim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Ashi_3 on 5/2/2015.
 */
public class CustomParsePushBroadcastReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return super.getActivity(context, intent);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        //Just prevent system notification
    }
}
