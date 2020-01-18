package com.ideaxen.hr.ideasms.utility.smsUtilities;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class SmsDelayHandler {
    Context context;

    public SmsDelayHandler(Context context) {
        this.context = context;
    }

    private static int getSendSmsMaxLimit(){

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
        return smsMaxAllowed;
    }


    private static int getSmsMaxDurationLimitations(){

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

//        Log.d(TAG, " checkPeriod = "+(smsCheckPeriod/60000) + " minutes");
        return smsCheckPeriod;
    }

    public static void delayForSendSms(int smsParts) {
        long delay = ((getSmsMaxDurationLimitations() / getSendSmsMaxLimit()) *smsParts);
        Log.d(TAG, "Delay: "+delay+ " milli seconds");
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
