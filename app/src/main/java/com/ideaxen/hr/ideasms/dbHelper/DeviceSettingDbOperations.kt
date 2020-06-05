package com.ideaxen.hr.ideasms.dbHelper;

import android.database.sqlite.SQLiteDatabase;

public class DeviceSettingDbOperations {
    val  db = SQLiteDatabase.openDatabase("/data/data/com.android.providers.settings/databases/settings.db", null, 0);


}


