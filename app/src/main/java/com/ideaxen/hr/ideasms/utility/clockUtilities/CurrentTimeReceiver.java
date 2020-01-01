package com.ideaxen.hr.ideasms.utility.clockUtilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTimeReceiver {

    public static String getCurrentDateTime() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy 'at' hh:mm a z");
            return dateFormat.format(new Date()); // Find Today's date
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
