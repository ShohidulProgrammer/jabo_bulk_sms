package com.ideaxen.hr.ideasms.utility.smsUtilities;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;

import android.os.Build;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;


public class SmsSender {
    static final String ACTION_SMS_SENT = "com.ideaxen.sms360.SMS_SENT";
    static final String ACTION_SMS_DELIVERED = "com.ideaxen.sms360.SMS_DELIVERED";
    private static final String RECEIVED_SMS_DELIVERED = "com.ideaxen.sms360.RECEIVED_SMS_DELIVERED";
    static final String RECEIVED_SMS_INFO = "com.ideaxen.sms360.RECEIVED_SMS_INFO";
    static final String RECEIVED_SMS_SEND_RESULT = "com.ideaxen.sms360.RECEIVED_SMS_SEND_RESULT";
    private Context context;

    public SmsSender(Context context) {
        this.context = context;
    }

    // set Que data to send sms
    public void setInfoToSendSms(Cursor cursor) {
        try {
            int smsMaxAllowed = 0;
            int maxSmsLimit = getSmsMaxTimeLimitations();
            while (cursor.moveToNext()) {
               String  id = cursor.getString(0);
               String  mobile = cursor.getString(1);
               String  user = cursor.getString(2);
               String  msg = cursor.getString(3);

                if (smsMaxAllowed >= maxSmsLimit) {
                    smsMaxAllowed = 0;
                    SystemClock.sleep(getSmsMaxDurationLimitations());
                }

                // send sms to mobile devices
                sendSms(id, mobile, user, msg,-1);
                smsMaxAllowed++;
            }
        } catch (Exception e) {
            System.out.println("Read Que data error: " + e);
        }
    }

    // send sms to mobile devices
    @SuppressLint("SimpleDateFormat")
    public void sendSms(String id, String mobile, String user, String msg, int oldSendResult) {
        String currentDateTime = getCurrentDateTime();


        String[] sms = new String[]{
                id,
                mobile,
                user,
                msg,
                currentDateTime,
        };



        // sms Send intent
        Intent iSent = new Intent(ACTION_SMS_SENT);
        iSent.putExtra(RECEIVED_SMS_INFO, sms);
        iSent.putExtra(RECEIVED_SMS_SEND_RESULT, oldSendResult);

        // sms Delivered intent
        Intent iDel = new Intent(ACTION_SMS_DELIVERED);
        iDel.putExtra(RECEIVED_SMS_DELIVERED, id);

        // id convert string to int
        int PIid = Integer.parseInt(id);

        // set broadcast pending intent
        PendingIntent piSent = PendingIntent.getBroadcast(context, PIid, iSent, 0);
        PendingIntent piDel = PendingIntent.getBroadcast(context, PIid, iDel, 0);

        // Send  text SMS
        try {
            // get the default instance of SmsManager
            SmsManager smsManager = SmsManager.getDefault();
//            SmsManager smsManagerForSubscriptionId = SmsManager.getSmsManagerForSubscriptionId(1);
//            SmsManager smsManager = SmsManager.getDefault();

            ArrayList<String> parts = smsManager.divideMessage(msg);
//                iSent.putExtra("phoneNo",mobile);
//                context.startActivity(iSent);
            if (parts.size() == 1) {
                // Send a text SMS within 160 character
                String massage = parts.get(0);
                smsManager.sendTextMessage(mobile, null, massage, piSent, piDel);
                Toast.makeText(context, "SMS has been sending!", Toast.LENGTH_SHORT).show();
            }
            else {
                // send multiline text sms
                ArrayList sentPis = new  ArrayList<PendingIntent>();
                ArrayList delPis = new  ArrayList<PendingIntent>();
                int ct = parts.size();
                for (int i = 0 ;i < ct; i++) {
                    sentPis.add(i, piSent);
                    delPis.add(i, piDel);
                }
                smsManager.sendMultipartTextMessage(mobile, null, parts, sentPis, delPis);
                Toast.makeText(context, "Multipart Text Message has been sending!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "SMS Failed to Send!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String getCurrentDateTime() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy 'at' hh:mm a z");
            return dateFormat.format(new Date()); // Find Today's date
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    private int getSmsMaxTimeLimitations(){

        int apiLevel = Build.VERSION.SDK_INT;
        int smsMaxAllowed;
        switch(apiLevel){
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                smsMaxAllowed = 100;
                break;
            default:
                smsMaxAllowed = 30;
                break;
        }
        smsMaxAllowed = smsMaxAllowed - 1; //This is to give us a little buffer to be extra safe (like a condom ;)
        Log.d(TAG, "maxAllowed = "+smsMaxAllowed);
        Toast.makeText(context.getApplicationContext(), "maxAllowed = "+smsMaxAllowed,
                Toast.LENGTH_LONG).show();
        return smsMaxAllowed;
    }


    private int getSmsMaxDurationLimitations(){

        int apiLevel = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        int smsCheckPeriod;
        switch(apiLevel){
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                smsCheckPeriod = 3600000;
                break;
            case 16:
                smsCheckPeriod = 1800000;
                break;
            case 17:
                if(versionRelease.contains("4.2.2")){
                    smsCheckPeriod = 60000;
                }else {
                    smsCheckPeriod = 1800000;
                }
                break;
            default:

                smsCheckPeriod = 60000;
                break;
        }

        Log.d(TAG, " checkPeriod = "+(smsCheckPeriod/60000) + " minutes");
        Toast.makeText(context.getApplicationContext(), "checkPeriod = "+(smsCheckPeriod/60000) + " minutes",
                Toast.LENGTH_LONG).show();
        return smsCheckPeriod;
    }



}
