package com.ideaxen.hr.ideasms.utility.smsUtilities;

import android.content.Context;
import android.database.Cursor;

import com.ideaxen.hr.ideasms.utility.Constants;


public class SmsInfoSetter {

    private SmsSender smsSender;


    public SmsInfoSetter(Context context) {
        smsSender = new SmsSender(context);
    }


    // set Que data to send sms
    public void setInfoToSendSms(Cursor cursor) {

        try {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(Constants.ID));
                String mobile = cursor.getString(cursor.getColumnIndex(Constants.MOBILE));
                String user = cursor.getString(cursor.getColumnIndex(Constants.USER));
                String msg = cursor.getString(cursor.getColumnIndex(Constants.MESSAGE));
//                System.out.println("Current time: "+ CurrentTimeReceiver.getCurrentDateTime());


                // send sms to mobile devices
                smsSender.sendSms(id, mobile, user, msg, -1);

                // Sms Send delay
                SmsDelayHandler.delayForSendSms(SmsSender.smsSize);
            }
        } catch (Exception e) {
            System.out.println("Read Que data error: " + e);
        }
    }
}
