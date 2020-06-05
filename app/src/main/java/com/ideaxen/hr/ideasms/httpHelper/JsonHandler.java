package com.ideaxen.hr.ideasms.httpHelper;

import android.content.Context;

import com.ideaxen.hr.ideasms.dbOperation.DbOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHandler {

    private Context context;
    DbOperations dbOperations;

    public JsonHandler(Context context) {
        this.context = context;
        dbOperations = new DbOperations(context);
    }

    public boolean parseJson(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            int ln = jsonArray.length();

            for (int i = 0; i < ln; i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                String mobileNo = row.getString("mobileNo");
                String userName = row.getString("userName");
                String message = row.getString("massage");

                // insert Data in Queue Table
                dbOperations.insertQData(mobileNo, userName, message);


//                SmsModel smsModel = new SmsModel();
//                smsModel.setMobileNo(row.getString("mobileNo"));
//                smsModel.setUserName(row.getString("userName"));
//                smsModel.setMessage(row.getString("massage"));
//                historyModelList.add(smsModel);
            }


            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}
