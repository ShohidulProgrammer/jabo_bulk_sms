package com.ideaxen.hr.ideasms.dbOperation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class DbProvider extends SQLiteOpenHelper {

    // db & table name
    private static final String DB_NAME = "IdeaSmsDB.db";
    private static final int DATABASE_VERSION_NO = 1;

    // define table name
    public static final String HISTORY_TABLE = "history_table";
    public static final String QUEUE_TABLE = "queue_table";

    // table columns names
    public static final String ID = "_id";
    public static final String MOBILE = "mobile";
    public static final String USER = "user";
    public static final String MESSAGE = "message";
    public static final String SEND = "send";
    public static final String DATE = "date";

    // create table query
    private static final String CREATE_Q_TABLE = "CREATE TABLE IF NOT EXISTS " + QUEUE_TABLE +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MOBILE + " TEXT NOT NULL, "
            + USER + " TEXT,"
            + MESSAGE + " TEXT);";

    private static final String CREATE_History_TABLE = "CREATE TABLE IF NOT EXISTS " + HISTORY_TABLE +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MOBILE + " TEXT NOT NULL, "
            + USER + " TEXT, "
            + MESSAGE + " TEXT, "
            + SEND + " BIT, "
            + DATE + " TEXT);";

    // delete table query
    private static final String DROP_Q_TABLE = "DROP TABLE IF EXISTS " + QUEUE_TABLE + ";";
    private static final String DROP_History_TABLE = "DROP TABLE IF EXISTS " + HISTORY_TABLE + ";";
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



