package com.ideaxen.hr.ideasms.utility.smsUtilities;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;

import com.ideaxen.hr.ideasms.utility.clockUtilities.CurrentTimeReceiver;

public class SmsInfoSetter {

    SmsSender smsSender;
    private Context context;
    private static int smsSends = 0;
    private static int maxSmsSendAllowed = MaxSendSmsDefiner.getSmsMaxTimeLimitations();

    public SmsInfoSetter(Context context) {
        this.context = context;
        smsSender = new SmsSender(context);
    }


    // set Que data to send sms
    public void setInfoToSendSms(Cursor cursor) {
        try {
            smsSends = 0;
//            int smsSends = 0;

            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String mobile = cursor.getString(1);
                String user = cursor.getString(2);
                String msg = cursor.getString(3);

                System.out.println("Current time: "+ CurrentTimeReceiver.getCurrentDateTime());
                int sleepDuration = (MaxSendSmsDefiner.getSmsMaxDurationLimitations()/maxSmsSendAllowed)*100;
                SystemClock.sleep(sleepDuration);


//                if (smsSends >= maxSmsSendAllowed) {
//                    smsSends = 0;
//                    SystemClock.sleep(MaxSendSmsDefiner.getSmsMaxDurationLimitations()/maxSmsSendAllowed);
//                }

                System.out.println("Current time: "+ CurrentTimeReceiver.getCurrentDateTime());
                smsSends++;
                System.out.println("smsSends: "+smsSends);

                // send sms to mobile devices
                smsSender.sendSms(id, mobile, user, msg, -1);
                SystemClock.sleep(2000);

            }
        } catch (Exception e) {
            System.out.println("Read Que data error: " + e);
        }
    }
}
