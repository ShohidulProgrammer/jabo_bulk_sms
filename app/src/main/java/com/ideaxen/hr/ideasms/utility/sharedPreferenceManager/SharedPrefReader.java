package com.ideaxen.hr.ideasms.utility.sharedPreferenceManager;

import android.content.Context;

import com.ideaxen.hr.ideasms.models.SimInfo;
import com.ideaxen.hr.ideasms.utility.simCardUtilities.SimCardInSharedPreferences;
import com.ideaxen.hr.ideasms.utility.simCardUtilities.SimCardReader;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefReader {
    private Context context;

    public SharedPrefReader(Context context) {
        this.context = context;
    }

    public ArrayList<String> getSimOperatorName() {
        SimCardReader simCardReader = new SimCardReader(context);
        List<SimInfo> simInfoList = simCardReader.getSIMInfo();
        ArrayList<String> operators = new ArrayList<>();

        for (int i = 0; i < simInfoList.size(); i++) {
            operators.add(simInfoList.get(i).getOperatorName());
        }
        return operators;
    }

    // get sim card unique id
    public String getSimCardIccId(int selectedSimCardSlot) {
        SimCardReader simCardReader = new SimCardReader(context);
        List<SimInfo> simInfoList = simCardReader.getSIMInfo();

        return simInfoList.get(selectedSimCardSlot).getIcc_id();
    }

    // get sim card unique id
    public int getSelectedSimCardSlot() {
        int noSelectedSimCardSlot = -1;
        SimCardInSharedPreferences simCardInSharedPreferences = new SimCardInSharedPreferences(context);
        String iccId = simCardInSharedPreferences.getSimCardIccIdInSPStore();

        SimCardReader simCardReader = new SimCardReader(context);
        List<SimInfo> simInfoList = simCardReader.getSIMInfo();
        for (int deviceSimCardSlot = 0; deviceSimCardSlot < simInfoList.size(); deviceSimCardSlot++) {
            if (iccId.equals(simInfoList.get(deviceSimCardSlot).getIcc_id())) {
                System.out.println("selected Sim Card Slot: " + deviceSimCardSlot);
                return deviceSimCardSlot;
            }
        }
        return noSelectedSimCardSlot;
    }
}
