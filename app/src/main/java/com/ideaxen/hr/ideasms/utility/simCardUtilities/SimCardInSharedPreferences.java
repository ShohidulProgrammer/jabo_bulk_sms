package com.ideaxen.hr.ideasms.utility.simCardUtilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.sharedPreferenceManager.SimCardReaderFromSharedPref;

public class SimCardInSharedPreferences {
    private Context context;
    private SimCardReaderFromSharedPref simCardReaderFromSharedPref;

    public SimCardInSharedPreferences(Context context) {
        this.context = context;
        this.simCardReaderFromSharedPref = new SimCardReaderFromSharedPref(context);
    }

    // save selected SIM card serial number as icc id in sharedPreferences variable MY_SELECTED_SIM_CARD_ICC_ID.
    public void saveSelectedSimCardInSPS(int selectedSimCardSlot) {
        // read Selected Sim Card ICC ID
        String selectedSimCardIccId = simCardReaderFromSharedPref.getSimCardIccId(selectedSimCardSlot);
        System.out.println("My ICC ID: " + selectedSimCardIccId);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedPreferences.edit();

        // Save SIM card serial number as icc id
        spEdit.putString(Constants.MY_SELECTED_SIM_CARD_ICC_ID, selectedSimCardIccId);
        spEdit.apply();
    }


    // get subscribed SIM card info from shared preference storage
    public String getSimCardIccIdInSPStore() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

        // check the sharedPreferences variable MY_SELECTED_SIM_CARD_ICC_ID
        // has stored any value or not
        if (sharedPreferences.contains(Constants.MY_SELECTED_SIM_CARD_ICC_ID)) {
//            System.out.println("MY_SELECTED_SIM_CARD_ICC_ID: "
//                    + sharedPreferences.getString(Constants.MY_SELECTED_SIM_CARD_ICC_ID, Constants.SIM_CARD_NOT_SELECTED_YET));

            return sharedPreferences.getString(Constants.MY_SELECTED_SIM_CARD_ICC_ID, Constants.SIM_CARD_NOT_SELECTED_YET);
        } else {
            return Constants.SIM_CARD_NOT_SELECTED_YET;
        }
    }
}
