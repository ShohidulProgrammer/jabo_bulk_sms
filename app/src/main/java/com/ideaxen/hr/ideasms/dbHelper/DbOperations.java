package com.ideaxen.hr.ideasms.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ideaxen.hr.ideasms.models.SmsModel;
import com.ideaxen.hr.ideasms.utility.Constants;


public class DbOperations {

    private static final String LOGTAG = "IDEA_SMS_MNGMNT_SYS";

    private SQLiteOpenHelper dbProvider;
    private SQLiteDatabase db;


    public DbOperations(Context context) {
        if (dbProvider == null) {
            dbProvider = new DbProvider(context);
        }
    }


    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbProvider.close();
    }


    // save in Queue table
    public void insertQData(SmsModel smsModel) {
        db = dbProvider.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.MOBILE, smsModel.getMobileNo());
        values.put(Constants.USER, smsModel.getUserName());
        values.put(Constants.MESSAGE, smsModel.getMessage());

        db.insert(Constants.QUEUE_TABLE, null, values);
        db.close();
    }

    // save data in History table
    public void insertData(SmsModel smsModel) {
        db = dbProvider.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.MOBILE, smsModel.getMobileNo());
        values.put(Constants.USER, smsModel.getUserName());
        values.put(Constants.MESSAGE, smsModel.getMessage());
        values.put(Constants.DATE, smsModel.getDate());
        values.put(Constants.SEND, smsModel.getSend());
        db.insert(Constants.HISTORY_TABLE, null, values);

        // delete q row if the row data successfully inserted to the history table
        db.delete(Constants.QUEUE_TABLE, Constants.ID + " = " + smsModel.getStrId(), null);
        System.out.println("Que row Id: "+smsModel.getStrId()+" successfully deleted!");
        db.close();
    }


    // update data
    public void update(String table, SmsModel smsModel) {
        String where;
        String id = smsModel.getStrId();
        db = dbProvider.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ID, id);
        values.put(Constants.MOBILE, smsModel.getMobileNo());
        values.put(Constants.USER, smsModel.getUserName());
        values.put(Constants.MESSAGE, smsModel.getMessage());
        values.put(Constants.DATE, smsModel.getDate());
        values.put(Constants.SEND, smsModel.getSend());

        if (id != null) {
            where = Constants.ID + " = " + id;
            db.update(table, values, where, null);
        }
//        else {
//            where = DbProvider.ID + " = (SELECT MAX(" + DbProvider.ID + ")+1 FROM " + table + ")";
//        }
//        db.update(table, values, where, null);
        db.close();
    }


    // get all messages for same mobile number
    public Cursor fetchMobileMessages(String mobile) {
        db = dbProvider.getReadableDatabase();

        return db.query(Constants.HISTORY_TABLE,
                new String[]{
                Constants.ID,
                Constants.MOBILE,
                Constants.USER,
                Constants.MESSAGE,
                Constants.DATE,
                Constants.SEND},
                Constants.MOBILE + " = ?" , new String[] {mobile} ,
        null, null, Constants.DATE + " DESC");
    }

    // get all data
    public Cursor getAllData(String table) {
        db = dbProvider.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + table, null);
    }

    // get all data
    public Cursor getAllHistoryData(String table) {
        db = dbProvider.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + table +" group by "+Constants.MOBILE, null);
    }


    /**
     * delete a given row based on the id.
     */
    public void deleteItem(String table, String id) {
        db = dbProvider.getWritableDatabase();
        db.delete(table, Constants.ID + " = " + id, null);
        db.close();
    }


    // delete all messages for the same mobile number
    public void deleteMobileMassages(String table, String mobile) {
        db = dbProvider.getWritableDatabase();
        db.delete(table, Constants.MOBILE + " = ?" , new String[] {mobile});
    }


    // delete all data from the table
    public void deleteAll(String table) {
        db = dbProvider.getWritableDatabase();
        db.delete(table, null, null);
        db.close();
    }
}
