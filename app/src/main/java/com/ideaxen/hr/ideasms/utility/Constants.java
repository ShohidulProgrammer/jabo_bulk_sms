package com.ideaxen.hr.ideasms.utility;

public class Constants {
    // permissions constants
    public static final int REQUEST_PERMISSIONS_RESULT_CODE = 0;
    public static final int REQUEST_PERMISSION_SETTING = 0;

    // shared preference Constants
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String MY_APP_NAME = "AppName";
    public static final String APP_NAME_NOT_REGISTERED = "You are\nNot Registered Yet";

    // sim card registration constants
    public static final String SIM_CARD_NOT_SELECTED_YET = "SIM card not selected Yet";
    public static final String MY_SELECTED_SIM_CARD_ICC_ID = SIM_CARD_NOT_SELECTED_YET;
    public static final String SIM_READ_PERMISSION_CANCELED = "SIM Read Permission Canceled";

    // SMS broadcast receiver constants
    public static final String ACTION_SMS_SENT = "com.ideaxen.sms360.SMS_SENT";
    public static final String ACTION_SMS_DELIVERED = "com.ideaxen.sms360.SMS_DELIVERED";
    public static final String RECEIVED_SMS_DELIVERED = "com.ideaxen.sms360.RECEIVED_SMS_DELIVERED";
    public static final String RECEIVED_SMS_INFO = "com.ideaxen.sms360.RECEIVED_SMS_INFO";
    public static final String RECEIVED_SMS_SEND_RESULT = "com.ideaxen.sms360.RECEIVED_SMS_SEND_RESULT";

    public static final String SelectedMobileNo = "com.ideaxen.sms360.SelectedMobileNo";

    // Database Constants
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

}
