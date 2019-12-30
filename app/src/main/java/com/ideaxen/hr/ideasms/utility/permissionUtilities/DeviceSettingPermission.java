package com.ideaxen.hr.ideasms.utility.permissionUtilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.ideaxen.hr.ideasms.MainActivity;
import com.ideaxen.hr.ideasms.utility.Constants;

public class DeviceSettingPermission {
    public void goPermissionSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", MainActivity.PACKAGE_NAME, null);
        intent.setData(uri);
        activity.startActivityForResult(intent, Constants.REQUEST_PERMISSION_SETTING);
    }

}
