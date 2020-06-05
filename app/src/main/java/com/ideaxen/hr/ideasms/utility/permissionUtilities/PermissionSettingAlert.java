package com.ideaxen.hr.ideasms.utility.permissionUtilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;

public class PermissionSettingAlert {

    Context context;
    AlertDialog alertDialog;

    public PermissionSettingAlert(Context context) {
        this.context = context;
    }

    public void permissionWarning() {
        if( alertDialog != null && alertDialog.isShowing() ) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permissions Required");
        builder.setMessage("You have forcefully denied some of the required permissions " +
                "for this action. Please open settings, go to permissions and allow them.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                DeviceSettingPermission deviceSettingPermission = new DeviceSettingPermission();
                deviceSettingPermission.goPermissionSetting((Activity) context);

                // reset Shared preferences or clear all data from this app
//                PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
            }});
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }


}
