package com.ideaxen.hr.ideasms.utility.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ideaxen.hr.ideasms.utility.httpHelper.ServerDataManager;

/**
 * This is called whenever app receives notification
 * in background/foreground state so you can
 * apply logic for background task, but still Firebase notification
 * will be shown in notification tray
 */
public class FCMBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "FcmBroadcastReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "I'm in broadcast receiver!!!");
        if (intent.getExtras() != null) {
            // Get notification content
            Bundle extras = intent.getExtras();


            //Don't show a notification on boot
            if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED)
                return;

                // get fire base  push notification
            else if (extras.containsKey("url")) {
                Toast.makeText(context, "New url has been Arrived!\nthrough Push Notification.", Toast.LENGTH_LONG).show();
                String appName = extras.getString("appName");
                String sysVal = extras.getString("sysval");
                String url = extras.getString("url");
                // ServerDataHandler for
                // getting sms info and send sms to mobile device
                setServerUrl(context, appName, sysVal, url);
            }
        }
    }

    // When push notification arrived with a url then
    // the function request for getting sms info
    // from server url
    private void setServerUrl(Context context, String appName, String sysVal, String url) {
        try {
            ServerDataManager serverDataManager = new ServerDataManager(context);
            serverDataManager.execute(appName, sysVal, url);
        } catch (Exception e) {
            System.out.println("Get Data Error: " + e);
        }
    }

}