package com.ideaxen.hr.ideasms.utility.smsUtilities;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MaxSendSmsDefiner {
    Context context;

    public MaxSendSmsDefiner(Context context) {
        this.context = context;
    }

    public static int getSmsMaxTimeLimitations(){

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
        smsMaxAllowed = smsMaxAllowed - 10; //This is to give us a little buffer to be extra safe (like a condom ;)
        Log.d(TAG, "maxAllowed = "+smsMaxAllowed);
//        Toast.makeText(context, "maxAllowed = "+smsMaxAllowed,
//                Toast.LENGTH_LONG).show();
        return smsMaxAllowed;
    }


    public static int getSmsMaxDurationLimitations(){

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
//        Toast.makeText(context.getApplicationContext(), "checkPeriod = "+(smsCheckPeriod/60000) + " minutes",
//                Toast.LENGTH_LONG).show();
        return smsCheckPeriod;
    }
}
