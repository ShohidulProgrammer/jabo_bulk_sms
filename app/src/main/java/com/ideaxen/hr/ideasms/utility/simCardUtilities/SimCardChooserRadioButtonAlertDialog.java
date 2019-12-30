package com.ideaxen.hr.ideasms.utility.simCardUtilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.ideaxen.hr.ideasms.models.SimInfo;
import com.ideaxen.hr.ideasms.utility.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler.checkPermissions;

public class SimCardChooserRadioButtonAlertDialog {
    private Context context;
    private SimCardInSharedPreferences simCardInSharedPreferences;

    public SimCardChooserRadioButtonAlertDialog(Context context) {
        this.context = context;
    }

    public void registerSimCard() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        final String[] simOperators = getSimOperatorName().toArray(new String[0]);

        if (simOperators[0].equals(Constants.SIM_READ_PERMISSION_CANCELED)) {
            alertDialog.setTitle("You must be Permit your SIM card Read Permission");

            // Yes button
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkPermissions(context);
                        }
                    });
        } else {
            alertDialog.setTitle("For Sending SMS Choose SIM card");

            // select initial sim card as sim -1
            final int checkedItem = -1;
            alertDialog.setSingleChoiceItems(simOperators, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, simOperators[which] + " Sim Selected", Toast.LENGTH_LONG).show();

                    if (which >= 0 && which < simOperators.length) {
                        // save selected sim serial number in Shared Preference Storage
                        String iccId = getSimCardIccId(which);
                        System.out.println("My ICC ID: " + iccId);
                        if (simCardInSharedPreferences == null) {
                            simCardInSharedPreferences = new SimCardInSharedPreferences(context);
                        }
                        simCardInSharedPreferences.saveSelectedSimCardInSPS(iccId);

                        iccId = simCardInSharedPreferences.getSimCardIccIdInSPStore();
                        Toast.makeText(context, "Saved SIM Serial number: " + iccId, Toast.LENGTH_LONG).show();
                        System.out.println("Saved SIM Serial number: " + iccId);

                        dialog.cancel();
                    }
                }
            });
        }
        AlertDialog alert = alertDialog.create();
        alert.setCancelable(false);
        alert.show();
    }

    private ArrayList<String> getSimOperatorName() {
        ArrayList<String> operators = new ArrayList<>();
        SimCardReader simCardReader = new SimCardReader(context);
        List<SimInfo> simInfoList = simCardReader.getSIMInfo();

        for (int i = 0; i < simInfoList.size(); i++) {
            operators.add(simInfoList.get(i).getOperatorName());
        }
        return operators;
    }


    // get sim card unique id
    private String getSimCardIccId(int selectedSimCardSlot) {
        SimCardReader simCardReader = new SimCardReader(context);
        List<SimInfo> simInfoList = simCardReader.getSIMInfo();
        return simInfoList.get(selectedSimCardSlot).getIcc_id();
    }
}
