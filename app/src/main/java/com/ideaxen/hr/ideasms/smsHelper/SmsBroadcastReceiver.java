package com.ideaxen.hr.ideasms.smsHelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.ideaxen.hr.ideasms.dbOperation.DbOperations;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "SmsBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "I'm in SmsBroadcastReceiver broadcast receiver!!!");
        if (intent.getExtras() != null) {
            String action = intent.getAction();
            String[] smsInfo = intent.getStringArrayExtra(SmsSender.RECEIVED_SMS_INFO);

            // check SMS sending report
            if (action.equals(SmsSender.ACTION_SMS_SENT)) {
                int isSend = 0;

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        isSend = 1;
                        Toast.makeText(context, "SMS Sent!", Toast.LENGTH_LONG).show();
                        System.out.println("SMS is sent");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", Toast.LENGTH_LONG).show();
                        System.out.println("Generic failure");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", Toast.LENGTH_LONG).show();
                        System.out.println("No service");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", Toast.LENGTH_LONG).show();
                        System.out.println("Null PDU");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", Toast.LENGTH_LONG).show();
                        System.out.println("Radio off");
                        break;
                }

                // set send result
                setSmsSendResult(context, smsInfo, isSend);
            }

            // check delivery report
            else if (action == SmsSender.ACTION_SMS_DELIVERED) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered", Toast.LENGTH_LONG).show();
                        System.out.println("SMS delivered");
                        System.out.println("Delivered ID: " + smsInfo[0] + " Mobile: " + smsInfo[1]);
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered", Toast.LENGTH_LONG).show();
                        System.out.println("SMS not delivered");
                        break;
                }
            }
        }
    }

    private void setSmsSendResult(Context context, String[] smsInfo, int isSend) {
        DbOperations dbOperations = new DbOperations(context);
        if (smsInfo != null) {
            System.out.println("\nSms Send Info: ID: " + smsInfo[0] + " Mobile: " + smsInfo[1] + " User: " + smsInfo[2] + " Date: " + smsInfo[4] + ", Previous Send Result: " + smsInfo[5] + ",  New Send Result: " + isSend);
            // insert new sms sending report to history table
            try {
                dbOperations.insertData(smsInfo[0], smsInfo[1], smsInfo[2], smsInfo[3], smsInfo[4], isSend);
//                        // check sms previous send result
//                        if (smsInfo[4] == "-1") {
//                            System.out.println("\n insert ID: "+smsInfo[0]+" Old send result: "+smsInfo[4]);
//                            // insert Data in history table
//                            dbOperations.insertData( smsInfo[0], smsInfo[1], smsInfo[2], smsInfo[3], smsInfo[4], isSend);
//                        } else {
//                            System.out.println("\nUpdate ID: ${smsInfo[0]} Old send result: ${smsInfo[4]}");
////                             Update sms info in history table
//                            dbOperations.update(DbProvider.HISTORY_TABLE, smsInfo[0], smsInfo[1], smsInfo[2], smsInfo[3], smsInfo[4], isSend);
//                        }

            } catch (Exception e) {
                System.out.println("DB Inserting error: $e");
            }
        }
    }
}
