package com.ideaxen.hr.ideasms.utility.httpHelper;

import android.content.Context;

import com.ideaxen.hr.ideasms.dbHelper.DbOperations;
import com.ideaxen.hr.ideasms.models.SmsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class JsonHandler {

    private DbOperations dbOperations;

    JsonHandler(Context context) {
        dbOperations = new DbOperations(context);
    }

    boolean addSmsQueue(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            int ln = jsonArray.length();

            for (int i = 0; i < ln; i++) {

                // insert Data in Queue Table
                JSONObject row = jsonArray.getJSONObject(i);
                String mobileNo = row.getString("mobileNo");
                String userName = row.getString("userName");
                String message = row.getString("message");

                SmsModel smsModel = new SmsModel(mobileNo,userName,message);
                dbOperations.insertQData(smsModel);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
