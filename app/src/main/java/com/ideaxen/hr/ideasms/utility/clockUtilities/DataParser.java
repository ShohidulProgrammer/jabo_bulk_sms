package com.ideaxen.hr.ideasms.utility.clockUtilities;

import android.database.Cursor;

import com.ideaxen.hr.ideasms.dbHelper.DbProvider;
import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class DataParser {
    public  List<SmsModel>  parseData(Cursor cursor){
        List<SmsModel> smsModels = new ArrayList<>();
        // parse database data
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Constants.ID));
            String mobile = cursor.getString(cursor.getColumnIndex(Constants.MOBILE));
            String user = cursor.getString(cursor.getColumnIndex(Constants.USER));
            String msg = cursor.getString(cursor.getColumnIndex(Constants.MESSAGE));
            String date = cursor.getString(cursor.getColumnIndex(Constants.DATE));
            int isSend = cursor.getInt(cursor.getColumnIndex(Constants.SEND));

            SmsModel smsModel = new SmsModel(id, mobile, user, msg, date, isSend);
            smsModels.add(smsModel);
        }

        cursor.close();
        return smsModels;
    }

}
