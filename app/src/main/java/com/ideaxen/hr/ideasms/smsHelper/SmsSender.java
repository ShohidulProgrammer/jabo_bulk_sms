package com.ideaxen.hr.ideasms.smsHelper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;

import android.telephony.SmsManager;
import android.widget.Toast;
import com.ideaxen.hr.ideasms.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SmsSender {
    public static final String ACTION_SMS_SENT = "com.ideaxen.sms360.SMS_SENT";
    public static final String ACTION_SMS_DELIVERED = "com.ideaxen.sms360.SMS_DELIVERED";
    public static final String RECEIVED_SMS_DELIVERED = "com.ideaxen.sms360.RECEIVED_SMS_DELIVERED";
    public static final String RECEIVED_SMS_INFO = "com.ideaxen.sms360.RECEIVED_SMS_INFO";
    private Context context;

    public SmsSender(Context context) {
        this.context = context;
    }



    public void setInfoToSendSms(Cursor cursor) {
        try {
            // get Queue table data
//            StringBuffer stringBuffer = new StringBuffer();
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String mobile = cursor.getString(1);
                String user = cursor.getString(2);
                String msg = cursor.getString(3);
                System.out.println("MY ID: " + id + " mobile: " + mobile + " user: " + user + " Message: " + msg);


                sendSms(id, mobile, user, msg, -1);

//                    stringBuffer.append("ID: " + id + "\n");
//                    stringBuffer.append("Mobile: " + mobile + "\n");
//                    stringBuffer.append("User: " + user + "\n");
//                    stringBuffer.append("Message: " + msg + "\n");
            }
        } catch (Exception e) {
            System.out.println("Read Que data error: " + e);
        }


    }

    protected void sendSms(String id, String mobile, String user, String msg, int oldSendResult) {

        String currentDateTime = "";

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy 'at' hh:mm a z");
            currentDateTime = dateFormat.format(new Date()); // Find Today's date
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] sms = new String[]{
                id,
                mobile,
                user,
                msg,
                currentDateTime,
                String.valueOf(oldSendResult),
        };


        // sms Send intent
        Intent iSent = new Intent(ACTION_SMS_SENT);
        iSent.putExtra(RECEIVED_SMS_INFO, sms);

        // sms Delivered intent
        Intent iDel = new Intent(ACTION_SMS_DELIVERED);
        iDel.putExtra(RECEIVED_SMS_DELIVERED, id);

        // id convert string to int
        int PIid = Integer.parseInt(id);

        // set broadcast pending intent
        PendingIntent piSent = PendingIntent.getBroadcast(context, PIid, iSent, 0);
        PendingIntent piDel = PendingIntent.getBroadcast(context, PIid, iDel, 0);

        // send sms
        try {
            // get the default instance of SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            // send a text based SMS
            smsManager.sendTextMessage(mobile, null, msg, piSent, piDel);
            Toast.makeText(context, "SMS has been sending!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            MainActivity mainActivity = new MainActivity();
            mainActivity.checkSmsSendingPermission();
            e.printStackTrace();
            System.out.println("\nSMS sending error: " + e);
            Toast.makeText(context, "SMS Failed to Send! Please check your sending permission", Toast.LENGTH_SHORT).show();
        }
    }
}
