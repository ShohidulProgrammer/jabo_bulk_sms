package com.ideaxen.hr.ideasms.utility.permissionUtilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.simCardUtilities.SimCardSubscriptionChecker;

public class PermissionHandler {

    private PermissionSettingAlert permissionSettingAlert;
    private SimCardSubscriptionChecker simCardSubscriptionChecker;

    // check SMS send permission
    public static boolean checkPermissions(Context context) {

        boolean allPermissionsGranted = true;
        // if permission not granted
        if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        ) {
            allPermissionsGranted = false;

//            request for Permissions
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_PHONE_STATE,


                    },
                    Constants.REQUEST_PERMISSIONS_RESULT_CODE);
        }
        return allPermissionsGranted;

    }

    public void onRequestPermissionResult(Context context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length == 0) {
            Log.e("permissions", "permissions length " + permissions.length);
            return;
        }

        boolean allPermissionsGranted = true;
        int i = -1;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Log.e("permission", "permission Not Granted:  " + permissions[++i]);
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsNeverAskAgain = false;
            boolean somePermissionsDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    //denied
                    Log.e("denied", "permission Denied " + permission);
                    somePermissionsDenied = true;
                } else {
                    if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", "permission allowed " + permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", "set to never ask again" + permission);
                        somePermissionsNeverAskAgain = true;
                    }
                }
            }

            // if permission Denied request again
            if (somePermissionsDenied) {
                checkPermissions(context);
            }

            // if permission never ask again display an warning
            // go to device setting permission
            if (somePermissionsNeverAskAgain) {
                Log.e("set to never ask again", "somePermissionsNeverAskAgain " + true);
                if (permissionSettingAlert == null) {
                    permissionSettingAlert = new PermissionSettingAlert(context);
                }
                permissionSettingAlert.permissionWarning();

            }
        }
        else {
            Toast.makeText(context, "Required Permissions are Granted",
                    Toast.LENGTH_LONG).show();
        }

        if (simCardSubscriptionChecker == null) {
            simCardSubscriptionChecker = new SimCardSubscriptionChecker();
        }
        simCardSubscriptionChecker.checkSimSubscription(context);
    }
}
