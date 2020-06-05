package com.ideaxen.hr.ideasms.utility;

import android.database.Cursor;

import com.ideaxen.hr.ideasms.dbOperation.DbProvider;
import com.ideaxen.hr.ideasms.models.SmsModel;

import java.util.ArrayList;
import java.util.List;

public class DataParser {
    public  List<SmsModel>  parseData(Cursor cursor){
        List<SmsModel> smsModels = new ArrayList<>();
        // parse database data
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DbProvider.ID));
            String mobile = cursor.getString(cursor.getColumnIndex(DbProvider.MOBILE));
            String user = cursor.getString(cursor.getColumnIndex(DbProvider.USER));
            String msg = cursor.getString(cursor.getColumnIndex(DbProvider.MESSAGE));
            String date = cursor.getString(cursor.getColumnIndex(DbProvider.DATE));
            int isSend = cursor.getInt(cursor.getColumnIndex(DbProvider.SEND));

            System.out.println("\nMy Mobile no: " + mobile);

            SmsModel smsModel = new SmsModel(id, mobile, user, msg, date, isSend);
            smsModels.add(smsModel);
        }

        cursor.close();
        return smsModels;
    }

}
