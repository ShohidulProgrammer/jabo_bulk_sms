package com.ideaxen.hr.ideasms.utility.smsUtilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;

import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.Toast;

import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.clockUtilities.CurrentTimeReceiver;
import com.ideaxen.hr.ideasms.utility.sharedPreferenceManager.SimCardReaderFromSharedPref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SmsSender {


    SimCardReaderFromSharedPref simCardReaderFromSharedPref;
    private Context context;
    private static int smsSends = 0;
    private static int maxSmsSendAllowed = MaxSendSmsDefiner.getSmsMaxTimeLimitations();

    public SmsSender(Context context) {
        this.context = context;
        this.simCardReaderFromSharedPref = new SimCardReaderFromSharedPref(context);
    }

    // send sms to mobile devices
    @SuppressLint("SimpleDateFormat")
    public void sendSms(String id, String mobile, String user, String msg, int oldSendResult) {
        String currentDateTime = CurrentTimeReceiver.getCurrentDateTime();


        String[] sms = new String[]{
                id,
                mobile,
                user,
                msg,
                currentDateTime,
        };


        // sms Send intent
        Intent iSent = new Intent(Constants.ACTION_SMS_SENT);
        iSent.putExtra(Constants.RECEIVED_SMS_INFO, sms);
        iSent.putExtra(Constants.RECEIVED_SMS_SEND_RESULT, oldSendResult);

        // sms Delivered intent
        Intent iDel = new Intent(Constants.ACTION_SMS_DELIVERED);
        iDel.putExtra(Constants.RECEIVED_SMS_DELIVERED, id);

        // id convert string to int
        int PIid = Integer.parseInt(id);

        // set broadcast pending intent
        PendingIntent piSent = PendingIntent.getBroadcast(context, PIid, iSent, 0);
        PendingIntent piDel = PendingIntent.getBroadcast(context, PIid, iDel, 0);

        // Send  text SMS
        try {
            // get the default instance of SmsManager
//            SmsManager smsManager = SmsManager.getDefault();

            // set sim card to send sms
            SubscriptionManager subscriptionManager = (context).getSystemService(SubscriptionManager.class);
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simCardReaderFromSharedPref.getSelectedSimCardSlot());
            SmsManager subscribedSmsManager = SmsManager.getSmsManagerForSubscriptionId(subscriptionInfo.getSubscriptionId());

            ArrayList<String> parts = subscribedSmsManager.divideMessage(msg);
//            ArrayList<String> parts = smsManager.divideMessage(msg);

            if (parts.size() == 1) {
                // Send a text SMS within 160 character
                String massage = parts.get(0);
                subscribedSmsManager.sendTextMessage(mobile, null, massage, piSent, piDel);
//                smsManager.sendTextMessage(mobile, null, massage, piSent, piDel);
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

//                    if (smsSends >= maxSmsSendAllowed) {
//                        smsSends = 0;
//                        SystemClock.sleep(getSmsMaxDurationLimitations());
//                    }
//
//                    smsSends++;
                }
                subscribedSmsManager.sendMultipartTextMessage(mobile, null, parts, sentPis, delPis);
//                smsManager.sendMultipartTextMessage(mobile, null, parts, sentPis, delPis);
                Toast.makeText(context, "Multipart Text Message has been sending!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "SMS Failed to Send!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
