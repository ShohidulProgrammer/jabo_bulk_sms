package com.ideaxen.hr.ideasms.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ideaxen.hr.ideasms.utility.Constants;


public class DbProvider extends SQLiteOpenHelper {

    // db & table name
    private static final String DB_NAME = "IdeaSmsDB.db";
    private static final int DATABASE_VERSION_NO = 1;

    // create table query
    private static final String CREATE_Q_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.QUEUE_TABLE +
            "(" + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Constants.MOBILE + " TEXT NOT NULL, "
            +Constants.USER + " TEXT,"
            + Constants.MESSAGE + " TEXT);";

    private static final String CREATE_History_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.HISTORY_TABLE +
            "(" + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Constants.MOBILE + " TEXT NOT NULL, "
            + Constants.USER + " TEXT, "
            + Constants.MESSAGE + " TEXT, "
            + Constants.SEND + " BIT, "
            + Constants.DATE + " TEXT);";

    // delete table query
    private static final String DROP_Q_TABLE = "DROP TABLE IF EXISTS " + Constants.QUEUE_TABLE + ";";
    private static final String DROP_History_TABLE = "DROP TABLE IF EXISTS " + Constants.HISTORY_TABLE + ";";
    private static final String DROP_TABLE = DROP_Q_TABLE + DROP_History_TABLE;


    // db initialization
    private SQLiteDatabase db;

    DbProvider(Context context) {
        // create db
        super(context, DB_NAME, null, DATABASE_VERSION_NO);
    }


    // create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d("create db", "DB has been created successfully");

//            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_Q_TABLE);
            System.out.println("Queue table has been created successfully!");

            db.execSQL(CREATE_History_TABLE);
            System.out.println("History table has been created successfully!");


        } catch (Exception e) {
            System.out.println("Table Created Exception : " + e);
        }
    }

    // upgrade db version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            System.out.println("Database Successfully Upgraded");
            db.execSQL(DROP_TABLE);
//            db.execSQL(DROP_Q_TABLE);
//            db.execSQL(DROP_History_TABLE);
            onCreate(db);
        } catch (Exception e) {
            System.out.println("Database Upgraded Exception : " + e);
        }

    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

}



