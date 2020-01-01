package com.ideaxen.hr.ideasms.utility.smsUtilities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.ideaxen.hr.ideasms.dbHelper.DbOperations;
import com.ideaxen.hr.ideasms.dbHelper.DbProvider;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.Constants;


public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static int oldId = -2;
    private static int newId = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            String action = intent.getAction();
            String[] smsInfo = null;
            int oldSendResult =  -1;

            if (intent.getStringArrayExtra(Constants.RECEIVED_SMS_INFO) != null ) {
                 smsInfo = intent.getStringArrayExtra(Constants.RECEIVED_SMS_INFO) ;
                 oldSendResult = intent.getIntExtra(Constants.RECEIVED_SMS_SEND_RESULT, -1);
            }


            // check SMS sending report
            assert action != null;
            if (action.equals(Constants.ACTION_SMS_SENT)) {
                int isSend = 0;

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        isSend = 1;
                        Toast.makeText(context, "SMS Successfully Send!", Toast.LENGTH_LONG).show();
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
                        break;
                }

//                SmsModel smsModel = getSmsModel(smsInfo, isSend);
                if (smsInfo != null) {
                    newId = Integer.parseInt(smsInfo[0]);
                }
                System.out.println("Multiple sms Send Old id:  "+oldId+ " New Id: "+smsInfo[0]);
                // save the new send result
                if (oldId == newId) {
                    System.out.println("Multiple sms Not Saved for  Old id:  "+oldId+ " New Id: "+smsInfo[0]);
                    Toast.makeText(context, "Multipart Text Message Send to " + smsInfo[1], Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("Multiple sms Saved for  Old id:  "+oldId+ " New Id: "+smsInfo[0]);
                    oldId = newId;
                    saveSmsSendResult(context, smsInfo, isSend, oldSendResult);
                }
            }

            // check delivery report
            else if (action.equals(Constants.ACTION_SMS_DELIVERED)) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        assert smsInfo != null;
//                        Toast.makeText(context, "SMS Successfully Delivered to " + smsInfo[1], Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        assert smsInfo != null;
//                        Toast.makeText(context, "Unfortunately the SMS doesn't Delivered Yet for Mobile No: " + smsInfo[1], Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }

    // insert or update sms sending report to history table
    private void saveSmsSendResult(Context context, String[] smsInfo, int isSend, int oldSendResult) {
        DbOperations dbOperations = new DbOperations(context);
        if (smsInfo != null) {
            String strId = smsInfo[0];
            String mobile = smsInfo[1];
            String user = smsInfo[2];
            String msg = smsInfo[3];
            String date = smsInfo[4];
            SmsModel smsModel = new SmsModel(strId, mobile, user, msg, date, isSend);

            try {
                // check sms previous send result
                if (oldSendResult == 0) {
//                    Update sms info in history table
                    dbOperations.update(DbProvider.HISTORY_TABLE, smsModel);
                } else {
                    // insert Data in history table
                    dbOperations.insertData(smsModel);
                }
            } catch (Exception e) {
                System.out.println("DB Inserting error: $e");
            }
        }
    }

    private SmsModel getSmsModel(String[] smsInfo, int isSend) {
        if (smsInfo != null) {
            String strId = smsInfo[0];
            String mobile = smsInfo[1];
            String user = smsInfo[2];
            String msg = smsInfo[3];
            String date = smsInfo[4];
            SmsModel smsModel = new SmsModel(strId, mobile, user, msg, date, isSend);

            return smsModel;
        }
        return null;
    }
}
