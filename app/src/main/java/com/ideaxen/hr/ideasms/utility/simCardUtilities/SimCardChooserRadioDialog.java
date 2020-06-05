package com.ideaxen.hr.ideasms.utility.simCardUtilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.ideaxen.hr.ideasms.utility.Constants;
import com.ideaxen.hr.ideasms.utility.sharedPreferenceManager.SimCardReaderFromSharedPref;

import static com.ideaxen.hr.ideasms.utility.permissionUtilities.PermissionHandler.checkPermissions;

public class SimCardChooserRadioDialog {
    private Context context;
    private SimCardReaderFromSharedPref simCardReaderFromSharedPref;
    private SimCardInSharedPreferences simCardInSharedPreferences;

    public SimCardChooserRadioDialog(Context context) {
        this.context = context;
        this.simCardReaderFromSharedPref = new SimCardReaderFromSharedPref(context);
        this.simCardInSharedPreferences = new SimCardInSharedPreferences(context);
    }

    public void registerSimCardRadioDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        final String[] simOperators = simCardReaderFromSharedPref.getSimOperatorName().toArray(new String[0]);

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
            alertDialog.setTitle("To send SMS, choose SIM card");

            // select initial sim card
            final int checkedItem = simCardReaderFromSharedPref.getSelectedSimCardSlot();
            if (checkedItem >= 0 && checkedItem < simOperators.length) {
                // Yes button
                alertDialog.setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // display saved toast
//                                int savedSim = simCardReaderFromSharedPref.getSelectedSimCardSlot();
//                                Toast.makeText(context, simOperators[savedSim] + " Successfully Saved", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        });

                // Yes button
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // save selected sim serial number in Shared Preference Storage
                                simCardInSharedPreferences.saveSelectedSimCardInSPS(checkedItem);
//                                Toast.makeText(context, "Canceled!", Toast.LENGTH_LONG).show();
//                                Toast.makeText(context, simOperators[checkedItem] + " was Previously Saved", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        });
            }

            alertDialog.setSingleChoiceItems(simOperators, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(context, simOperators[which] + " Selected", Toast.LENGTH_LONG).show();

                    if (which >= 0 && which < simOperators.length) {
                        // save selected sim serial number in Shared Preference Storage
                        simCardInSharedPreferences.saveSelectedSimCardInSPS(which);

                        // if first time sim selection
                        // and no (cancel or saved) button display
                        // then cancel after choosing and saving the simInfo
                        if (checkedItem == -1) {
                            dialog.cancel();
                        }
                    }
                }
            });
        }
        AlertDialog alert = alertDialog.create();
        alert.setCancelable(false);
        alert.show();
    }
}
