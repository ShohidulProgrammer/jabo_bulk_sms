package com.ideaxen.hr.ideasms.httpHelper;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.ideaxen.hr.ideasms.dbOperation.DbOperations;
import com.ideaxen.hr.ideasms.dbOperation.DbProvider;
import com.ideaxen.hr.ideasms.smsHelper.SmsSender;



public class ServerDataManager extends AsyncTask<String, String, String> {
    JsonHandler jsonHandler;
    SmsSender smsSender;
    DbOperations dbOperations;
    DbProvider dbProvider;

    public ServerDataManager(Context context) {
        jsonHandler = new JsonHandler(context);
        dbOperations = new DbOperations(context);
        dbProvider = new DbProvider(context);
        smsSender = new SmsSender(context);
    }


    // get data from server
    @Override
    protected String doInBackground(String... args) {
        // get super key
        String superKey = ApiHandler.getSuperKey(args[0], args[1]);

        // get json data from server
        String jsonSMSData = ApiHandler.getInfoToSendSms(args[2], superKey);
        return jsonSMSData;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            // parse  json data and that
            // data save into the que table
            boolean isParsed = jsonHandler.parseJson(response);

            // read Queue data
            Cursor cursor = null;
            if (isParsed) {
                cursor = dbOperations.getAllData(DbProvider.QUEUE_TABLE);
            } else {
                System.out.println("queue table data can't received any data ");
            }

            // set sms info for sending new sms
            if (cursor.getCount() > 0) {
                smsSender.setInfoToSendSms(cursor);
            } else {
                System.out.println("queue table has no data");
            }
        }
    }
}