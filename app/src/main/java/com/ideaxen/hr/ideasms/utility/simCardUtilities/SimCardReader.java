package com.ideaxen.hr.ideasms.utility.simCardUtilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import com.ideaxen.hr.ideasms.models.SimInfo;

import java.util.ArrayList;
import java.util.List;

import static com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler.checkPermissions;

public class SimCardReader {
    private Context context;

    public SimCardReader(Context context) {
        this.context = context;
    }

    public  List<SimInfo> getSIMInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                checkPermissions(context);
            }
        }

        List<SubscriptionInfo> subscription = SubscriptionManager.from(context.getApplicationContext()).getActiveSubscriptionInfoList();
        List<SimInfo> simInfoList = new ArrayList<>();

        for (int i = 0; i < subscription.size(); i++) {
            SubscriptionInfo info = subscription.get(i);

            int index = info.getSimSlotIndex();
            int slot = info.getSimSlotIndex()+1;
            String operatorName = (String) info.getCarrierName();
            String icc_id = info.getIccId();

            SimInfo simInfo = new SimInfo(index, operatorName, icc_id, slot);
//            Log.d( "SimInfo: ", simInfo.toString());
            simInfoList.add(simInfo);
        }
        return simInfoList;
    }
}
