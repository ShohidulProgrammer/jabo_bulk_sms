package com.ideaxen.hr.ideasms.notification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.ideaxen.hr.ideasms.dbOperation.DbOperations;

import com.ideaxen.hr.ideasms.httpHelper.ServerDataManager;
import com.ideaxen.hr.ideasms.smsHelper.SmsSender;

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
//        intent != null && intent.getAction() != null && ACTION.compareToIgnoreCase(intent.getAction()) == 0
        if (intent.getExtras() != null) {
            // Get notification content
            Bundle extras = intent.getExtras();
            // set sms sending report
            String action = intent.getAction();
//            String[] smsInfo = intent.getStringArrayExtra(SmsSender.RECEIVED_SMS_INFO);

            //Don't show a notification on boot
            if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED)
                return;

            // get fire base  push notification
            else if (extras.containsKey("url")) {
//                Toast.makeText(context, "New url has been Arrived!\nthrough Push Notification.", Toast.LENGTH_LONG).show();
                String appName = extras.getString("appName");
                String sysVal = extras.getString("sysval");
                String url = extras.getString("url");
                // ServerDataHandler for
                // getting sms info and send sms to mobile device
                setServerUrl(context, appName, sysVal, url);
            }

//            // check SMS sending report
//            else if (action.equals(SmsSender.ACTION_SMS_SENT)) {
//                DbOperations dbOperations = new DbOperations(context);
//                int isSend = 0;
//
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        isSend = 1;
//                        Toast.makeText(context, "SMS Sent!", Toast.LENGTH_LONG).show();
//                        System.out.println("SMS is sent");
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(context, "Generic failure", Toast.LENGTH_LONG).show();
//                        System.out.println("Generic failure");
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        Toast.makeText(context, "No service", Toast.LENGTH_LONG).show();
//                        System.out.println("No service");
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        Toast.makeText(context, "Null PDU", Toast.LENGTH_LONG).show();
//                        System.out.println("Null PDU");
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        Toast.makeText(context, "Radio off", Toast.LENGTH_LONG).show();
//                        System.out.println("Radio off");
//                        break;
//                }
//
//                // set send result
//                if (smsInfo != null) {
//                    System.out.println("\nSms Send Info: ID: " + smsInfo[0] + " Mobile: " + smsInfo[1] + " User: " + smsInfo[2] + " Date: " + smsInfo[4] + ", Previous Send Result: " + smsInfo[5] + ",  New Send Result: " + isSend);
//
//                    // insert new sms sending report to history table
//                    try {
//                        dbOperations.insertData(smsInfo[0], smsInfo[1], smsInfo[2], smsInfo[3], smsInfo[4], isSend);
//
//
////                        // check sms previous send result
////                        if (smsInfo[4] == "-1") {
////                            System.out.println("\n insert ID: "+smsInfo[0]+" Old send result: "+smsInfo[4]);
////                            // insert Data in history table
////                            dbOperations.insertData( smsInfo[0], smsInfo[1], smsInfo[2], smsInfo[3], smsInfo[4], isSend);
////                        } else {
////                            System.out.println("\nUpdate ID: ${smsInfo[0]} Old send result: ${smsInfo[4]}");
//////                             Update sms info in history table
////                            dbOperations.update(DbProvider.HISTORY_TABLE, smsInfo[0], smsInfo[1], smsInfo[2], smsInfo[3], smsInfo[4], isSend);
////                        }
//
//                    } catch (Exception e) {
//                        System.out.println("DB Inserting error: $e");
//                    }
//                }
//            }
//
//            // check delivery report
//            else if (action == SmsSender.ACTION_SMS_DELIVERED) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(context, "SMS delivered", Toast.LENGTH_LONG).show();
//                        System.out.println("SMS delivered");
//                        System.out.println("Delivered ID: " + smsInfo[0] + " Mobile: " + smsInfo[1]);
//                        break;
//
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(context, "SMS not delivered", Toast.LENGTH_LONG).show();
//                        System.out.println("SMS not delivered");
//                        break;
//                }
//            }
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