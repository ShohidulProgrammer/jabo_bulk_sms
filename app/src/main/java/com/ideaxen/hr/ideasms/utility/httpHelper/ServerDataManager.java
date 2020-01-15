package com.ideaxen.hr.ideasms.utility.httpHelper;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.ideaxen.hr.ideasms.dbHelper.DbOperations;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.smsUtilities.SmsInfoSetter;

import java.util.ArrayList;
import java.util.List;


public class ServerDataManager extends AsyncTask<String, String, String> {
    private JsonHandler jsonHandler;

    private SmsInfoSetter smsInfoSetter;
    private DbOperations dbOperations;
    private List<SmsModel> smsModels;

    public ServerDataManager(Context context) {
        jsonHandler = new JsonHandler(context);
        dbOperations = new DbOperations(context);
        smsInfoSetter = new SmsInfoSetter(context);
        smsModels = new ArrayList<>();
    }


    // get data from server
    @Override
    protected String doInBackground(String... args) {
        // get super key
        String superKey = ApiHandler.getSuperKey(args[0], args[1]);

        // get json data from server
        return ApiHandler.getInfoToSendSms(args[2], superKey);
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            // parse  json data and that
            // data save into the que table
             boolean isParsed = jsonHandler.addSmsQueue(response);

            // read Queue data
            Cursor cursor = null;
            if (isParsed) {
                cursor = dbOperations.getAllData(Constants.QUEUE_TABLE);
            } else {
                System.out.println("queue table data can't received any data ");
            }

            // set sms info for sending new sms
//            assert cursor != null;
            if (cursor.getCount()> 0) {
                smsInfoSetter.setInfoToSendSms(cursor);

            } else {
                System.out.println("queue table has no data");
            }
        }
    }
}