package com.ideaxen.hr.ideasms.httpHelper;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.ideaxen.hr.ideasms.dbOperation.DbOperations;
import com.ideaxen.hr.ideasms.dbOperation.DbProvider;
import com.ideaxen.hr.ideasms.smsHelper.SmsSender;



public class ServerDataManager extends AsyncTask<String, String, String> {
    private JsonHandler jsonHandler;
    private SmsSender smsSender;
    private DbOperations dbOperations;

    public ServerDataManager(Context context) {
        jsonHandler = new JsonHandler(context);
        dbOperations = new DbOperations(context);
        smsSender = new SmsSender(context);
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
            boolean isParsed = jsonHandler.parseJson(response);

            // read Queue data
            Cursor cursor = null;
            if (isParsed) {
                cursor = dbOperations.getAllData(DbProvider.QUEUE_TABLE);
            } else {
                System.out.println("queue table data can't received any data ");
            }

            // set sms info for sending new sms
            assert cursor != null;
            if (cursor.getCount() > 0) {
                smsSender.setInfoToSendSms(cursor);
            } else {
                System.out.println("queue table has no data");
            }
        }
    }
}