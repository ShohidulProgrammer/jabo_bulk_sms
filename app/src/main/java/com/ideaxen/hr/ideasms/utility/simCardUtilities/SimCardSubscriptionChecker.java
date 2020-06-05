package com.ideaxen.hr.ideasms.utility.simCardUtilities;

import android.content.Context;

import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler;

public class SimCardSubscriptionChecker {
    SimCardInSharedPreferences simCardInSharedPreferences;
    SimCardChooserRadioDialog simCardChooserRadioDialog;

    // check SIM card subscription status
    public void checkSimSubscription(Context context) {
        if (simCardInSharedPreferences == null) {
            simCardInSharedPreferences = new SimCardInSharedPreferences(context);
        }
        String simCardIccIdInSPStore = simCardInSharedPreferences.getSimCardIccIdInSPStore();
        System.out.println("simCardIccIdInSPStore: " + simCardIccIdInSPStore);

        // choose sim card if SIM not selected yet
        if (simCardIccIdInSPStore.equals(Constants.SIM_CARD_NOT_SELECTED_YET)) {
             boolean allPermissionsGranted = PermissionHandler.checkPermissions(context);

            if (allPermissionsGranted) {
                if (simCardChooserRadioDialog == null) {
                    simCardChooserRadioDialog = new SimCardChooserRadioDialog(context);
                }
                simCardChooserRadioDialog.registerSimCardRadioDialog();
            }

        }
    }
}
