package com.ideaxen.hr.ideasms.dbOperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class DbOperations {


    public static final String LOGTAG = "IDEA_SMS_MNGMNT_SYS";
    private Context context;

    SQLiteOpenHelper dbProvider;
    SQLiteDatabase db;


    private static final String[] allColumns = {
            DbProvider.ID,
            DbProvider.MOBILE,
            DbProvider.USER,
            DbProvider.MESSAGE,
            DbProvider.DATE,
            DbProvider.SEND
    };

    public DbOperations(Context context) {
        dbProvider = new DbProvider(context);
        this.context = context;
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        db = dbProvider.getWritableDatabase();


    }


    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbProvider.close();
    }


    // save in Queue table
    public void insertQData(String mobile, String user, String msg) {
//        System.out.println("ID: " + mobile + " Message: " + msg);
        db = dbProvider.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbProvider.MOBILE, mobile);
        values.put(DbProvider.USER, user);
        values.put(DbProvider.MESSAGE, msg);

        db.insert(DbProvider.QUEUE_TABLE, null, values);
//        Log.d(DbProvider.QUEUE_TABLE, "Inserted to QUEUE_TABLE ");

        Toast.makeText(context, "Successfully Inserted to Queue table!", Toast.LENGTH_SHORT).show();
        System.out.println("Successfully Inserted to QUEUE_TABLE table Mobile: " + mobile + ", User: " + user + ", Message: " + msg);
        db.close();
    }

    // save data in History table
    public void insertData(String id, String mobile, String user, String msg, String date, int send) {
        System.out.println("ID: " + id + " Date: " + date);
        db = dbProvider.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbProvider.MOBILE, mobile);
        values.put(DbProvider.USER, user);
        values.put(DbProvider.MESSAGE, msg);
        values.put(DbProvider.DATE, date);
        values.put(DbProvider.SEND, send);
        db.insert(DbProvider.HISTORY_TABLE, null, values);

        Toast.makeText(context, "Successfully Inserted to new message History from "+mobile+"!", Toast.LENGTH_SHORT).show();
        System.out.println("Inserted to History table Mobile: "+mobile+", User: "+user+", Message: "+msg+" Date: "+date +" Send Result: "+send);

        // delete q row if the row data successfully inserted to the history table
//        db.delete(QUEUE_TABLE, ID + " = ?", new String[]{id});
        db.delete(DbProvider.QUEUE_TABLE, DbProvider.ID + " = " + id, null);
        Toast.makeText(context, "Que table Row ID: " + id + " successfully deleted! : ", Toast.LENGTH_SHORT).show();
        System.out.println("Que row Id: "+id+" successfully deleted!");

        db.close();
    }


    // update data
    public void update(String table, String id, String mobile, String user, String msg, String date, int send) {
        String where;
        db = dbProvider.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbProvider.ID, id);
        values.put(DbProvider.MOBILE, mobile);
        values.put(DbProvider.USER, user);
        values.put(DbProvider.MESSAGE, msg);
        values.put(DbProvider.DATE, date);
        values.put(DbProvider.SEND, send);
        if (id != null) {
            where = DbProvider.ID + " = " + id;
        } else {
            where = DbProvider.ID + " = (SELECT MAX(" + DbProvider.ID + ")+1 FROM " + table + ")";
        }
        db.update(table, values, where, null);
        db.close();
    }

    // get one row item by id
    public Cursor fetchItem(String table, String id) {
        db = dbProvider.getWritableDatabase();
        Cursor cursor = db.query(table, new String[]
                        {
                                DbProvider.ID,
                                DbProvider.MOBILE,
                                DbProvider.USER,
                                DbProvider.MESSAGE,
                                DbProvider.DATE,
                                DbProvider.SEND},
                DbProvider.ID + " = " + id,
                null, null, null,
                DbProvider.DATE + " DESC");
        cursor.moveToFirst();
        db.close();
        return cursor;
    }

    // get all messages for same mobile number
    public Cursor fetchMobileMessages(String mobile) {
        db = dbProvider.getReadableDatabase();
        System.out.println("fetchMobileMessages called for get all data from db:" + db);

        Cursor cursor =
//                db.rawQuery("SELECT * FROM " + DbProvider.HISTORY_TABLE +" WHERE "+DbProvider.MOBILE+ " = "+mobile,null);

                db.query(DbProvider.HISTORY_TABLE,
                        new String[]{
                        DbProvider.ID,
                        DbProvider.MOBILE,
                        DbProvider.USER,
                        DbProvider.MESSAGE,
                        DbProvider.DATE,
                        DbProvider.SEND},
                        DbProvider.MOBILE + " = ?" , new String[] {mobile} ,
                null, null,
                DbProvider.DATE + " DESC");

        System.out.println("DB Cursor length : " + cursor.getCount());

//        cursor.moveToFirst();
//        db.close();
        return cursor;
    }

    // get all data
    public Cursor getAllData(String table) {
        SQLiteDatabase db = dbProvider.getReadableDatabase();
        System.out.println("get all data from db:" + db);
        Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);

        return cursor;
    }

    // get all data
    public Cursor getAllHistoryData(String table) {
        SQLiteDatabase db = dbProvider.getWritableDatabase();
        System.out.println("get all data from db:" + db);
        Cursor cursor = db.rawQuery("SELECT * FROM " + table +" group by "+DbProvider.MOBILE, null);

        return cursor;
    }


    /**
     * delete a given row based on the id.
     */
    public void deleteItem(String table, String id) {
        db = dbProvider.getWritableDatabase();
        db.delete(table, DbProvider.ID + " = " + id, null);
        db.close();
    }

    // delete last row from table
    public void deleteLastItem(String table) {
        db = dbProvider.getWritableDatabase();
        db.delete(table, DbProvider.ID +
                " = (SELECT MAX(" + DbProvider.ID +
                ") FROM " + table + ")", null);
        db.close();
    }

    // delete all messages for the same mobile number
    public void deleteMobileMassages(String table, String mobile) {
        db = dbProvider.getWritableDatabase();
        db.delete(table, DbProvider.MOBILE + " = ?" , new String[] {mobile});
//        db.delete(table, DbProvider.MOBILE + " = " + mobile, null);
        System.out.println("Messages are deleted successfully for mobile: "+mobile);
    }


    // delete all data from the table
    public void deleteAll(String table) {
        db = dbProvider.getWritableDatabase();
        int result = db.delete(table, null, null);
        db.close();
    }

}
