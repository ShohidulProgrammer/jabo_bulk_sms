package com.ideaxen.hr.ideasms.dbOperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class DbProvider extends SQLiteOpenHelper {

    // db & table name
    public static final String DB_NAME = "IdeaSmsDB.db";
    public String DB_PATH = "/data/user/0/com.ideaxen.hr.ideasms/databases/IdeaSmsDB.db";
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

    private static final String CREATE_TABLE = CREATE_Q_TABLE + CREATE_History_TABLE;

    // delete table query
    private static final String DROP_Q_TABLE = "DROP TABLE IF EXISTS " + QUEUE_TABLE + ";";
    private static final String DROP_History_TABLE = "DROP TABLE IF EXISTS " + HISTORY_TABLE + ";";
    private static final String DROP_TABLE = DROP_Q_TABLE + DROP_History_TABLE;

    public static final String SELECT_ALL_HISTORY = "SELECT * FROM " + HISTORY_TABLE;
    public static final String SELECT_ALL_QUEUE = "SELECT * FROM " + QUEUE_TABLE;


    // actual path of db
//    public String DB_PATH = "/data/data/com.ideaxen.hr.ideasms/databases/";
//    best way to set datetime in SQLit db
//    query.append(COLUMN_DATETIME+" int)");
//    //And inserting the data includes this:
//    values.put(COLUMN_DATETIME, System.currentTimeMillis());


    // db initialization
    private SQLiteDatabase db;
    private Context context;

    public DbProvider(Context context) {
        // create db
        super(context, DB_NAME, null, DATABASE_VERSION_NO);

//        // check db is open or not
//        // if open then close it
//        if (db != null && db.isOpen())
//            close();

        // set context
        this.context = context;
    }


    // create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d("create db", "DB has been created successfully");

//            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_Q_TABLE);
            Toast.makeText(context, "Queue Table has been created created successfully!", Toast.LENGTH_SHORT).show();
            System.out.println("Queue table has been created successfully!");

            db.execSQL(CREATE_History_TABLE);
            Toast.makeText(context, "History Tables has been created created successfully!", Toast.LENGTH_SHORT).show();
            System.out.println("History table has been created successfully!");

        } catch (Exception e) {
            Toast.makeText(context, "Table Created Exception : " + e, Toast.LENGTH_SHORT).show();
            System.out.println("Table Created Exception : " + e);
        }
    }

    // upgrade db version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Toast.makeText(context, "Database Successfully Upgraded", Toast.LENGTH_SHORT).show();
            db.execSQL(DROP_TABLE);
//            db.execSQL(DROP_Q_TABLE);
//            db.execSQL(DROP_History_TABLE);
            onCreate(db);
        } catch (Exception e) {
            Toast.makeText(context, "Database Upgraded Exception : " + e, Toast.LENGTH_SHORT).show();
        }

    }


    public boolean isOpen() {
        if (db != null)
            return db.isOpen();
        return false;
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
        }

        if (checkDB != null) {
            System.out.println("DB Closed");
            checkDB.close();
//            System.out.println("My db is:- " + checkDB.isOpen());
        }

        return checkDB != null ? true : false;
    }


    public Cursor execCursorQuery(String sql) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cursor;
    }

    public void execNonQuery(String sql) {
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            close();
        }
    }

    //
    // save in Queue table
    public void insertQData(String mobile, String user, String msg) {
        System.out.println("ID: " + mobile + " Message: " + msg);
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MOBILE, mobile);
        values.put(USER, user);
        values.put(MESSAGE, msg);

        db.insert(QUEUE_TABLE, null, values);
        Log.d(QUEUE_TABLE, "Inserted to QUEUE_TABLE ");

//        Toast.makeText(context, "Successfully Inserted to History table!", Toast.LENGTH_SHORT).show();
        System.out.println("Successfully Inserted to QUEUE_TABLE table Mobile: " + mobile + ", User: " + user + ", Message: " + msg);

        db.close();
    }

    //
//    // save data in History table
//    public void insertData( String id, String mobile, String user, String msg, String date, int send) {
//        System.out.println("ID: " + id + " Date: " + date);
//        db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(MOBILE, mobile);
//        values.put(USER, user);
//        values.put(MESSAGE, msg);
//        values.put(DATE, date);
//        values.put(SEND, send);
//        db.insert(HISTORY_TABLE, null, values);
//
//        Toast.makeText(context, "Successfully Inserted to History table!", Toast.LENGTH_SHORT).show();
//        System.out.println("Successfully Inserted to History table Mobile: $mobile, User: $user, Message: $msg Date: $date Send Result: $send");
//
//        // delete q row if the row data successfully inserted to the history table
////        db.delete(QUEUE_TABLE, ID + " = ?", new String[]{id});
//        db.delete(QUEUE_TABLE, ID + " = "+id, null);
//        Toast.makeText(context, "Que table Row ID: " + id + " successfully deleted! : ", Toast.LENGTH_SHORT).show();
////        println("Que successfully deleted!")
//
//        db.close();
//    }
//
//
//    // update data
//    public void update(String table, String id, String mobile, String user, String msg, String date, int send) {
//        String where;
//        db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(ID, id);
//        values.put(MOBILE, mobile);
//        values.put(USER, user);
//        values.put(MESSAGE, msg);
//        values.put(DATE, date);
//        values.put(SEND, send);
//        if (id != null) {
//            where = ID + " = " + id;
//        } else {
//            where = ID + " = (SELECT MAX(" + ID + ") FROM " + table + ")";
//        }
//        db.update(table, values, where, null);
//        db.close();
//    }
//
    // get one row item by id
    public Cursor fetchItem(String table, String id) {
        db = this.getWritableDatabase();
        Cursor cursor = db.query(table, new String[]{
                        ID, MOBILE, USER, MESSAGE
//                DATE, SEND
                }, ID + " = " + id, null, null, null, null
//                DATE + " DESC"
        );
        cursor.moveToFirst();
        db.close();
        return cursor;
    }

//    // get all messages for same mobile number
//    public Cursor fetchMobileMessages(String mobile) {
//        db = this.getWritableDatabase();
//        Cursor messagesInfo = db.query(HISTORY_TABLE, new String[]{ID, MOBILE, USER, MESSAGE, DATE, SEND},
//                MOBILE + " = " + mobile, null, null, null, DATE + " DESC");
//        messagesInfo.moveToFirst();
//        db.close();
//        return messagesInfo;
//    }

    // get all data
    public Cursor getAllData() {
        System.out.println("get all data has been called");
        db = this.getWritableDatabase();
        System.out.println("get all data db:" + db);
        Cursor cursor = db.rawQuery("SELECT * FROM " + QUEUE_TABLE, null);
////        System.out.println("Index 0 Value: "+cursor.getString(1));
//        if (cursor != null && cursor.moveToFirst()) {
//            String col = cursor.getString(1);
//            System.out.println("Row Item: " + col);
//            return cursor;
//
//        }
//        db.close();
        return cursor;

    }
//
//
//    /**
//     * delete a given row based on the id.
//     */
//    public void deleteItem(String table, String id) {
//        db = this.getWritableDatabase();
//        db.delete(table, ID + " = " + id, null);
//        db.close();
//    }
//
//    // delete last row from table
//    public void deleteLastItem(String table) {
//        db = this.getWritableDatabase();
//        db.delete(table, ID + " = (SELECT MAX(" + ID + ") FROM " + table + ")", null);
//        db.close();
//    }
//
//    // delete all messages for the same mobile number
//    public void deleteMobileMassages(String table, String mobile) {
//        db = this.getWritableDatabase();
//        db.delete(table, MOBILE + " = " + mobile, null);
//    }
//
//
//    // delete all data from the table
//    public void deleteAll(String table) {
//        db = this.getWritableDatabase();
//        db.delete(table, null, null);
//        db.close();
//    }


}



