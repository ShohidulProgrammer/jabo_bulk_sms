//package com.ideaxen.hr.ideasms.utility;
//
//import java.text.SimpleDateFormat;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//
//public class DateFormatter {
//        private Date date;
//    //        date = Calendar.getInstance().getTime();
//    String formateDate(Date tm) {
//
//
//        String month = tm.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
//        // Create a calendar object with calendar date. Calendar is in java.util pakage.
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.add(Calendar.DATE, 1);
//        Date oneDay = calendar.getTime();
//
//        calendar.add(Calendar.DATE, 2);
//        Date twoDay = calendar.getTime();
//
//        calendar.add(Calendar.DATE, 7);
//        Date oneWeek = calendar.getTime();
//
//
//
////
////        Duration oneDay = new Duration(days: 1);
////        Duration twoDay = new Duration(days: 2);
////        Duration oneWeek = new Duration(days: 7);
//
//        String month;
////        switch (tm.month) {
//        switch (tm.getMonth()) {
//
//            case 1:
//                month = "january";
//                break;
//            case 2:
//                month = "february";
//                break;
//            case 3:
//                month = "march";
//                break;
//            case 4:
//                month = "april";
//                break;
//            case 5:
//                month = "may";
//                break;
//            case 6:
//                month = "june";
//                break;
//            case 7:
//                month = "july";
//                break;
//            case 8:
//                month = "august";
//                break;
//            case 9:
//                month = "september";
//                break;
//            case 10:
//                month = "october";
//                break;
//            case 11:
//                month = "november";
//                break;
//            case 12:
//                month = "december";
//                break;
//        }
//
//        Duration difference = calendar.difference(tm);
//
//        if (difference.compareTo(oneDay) < 1) {
//            return "Today";
//        } else if (difference.compareTo(twoDay) < 1) {
//            return "yesterday";
//        } else if (difference.compareTo(oneWeek) < 1) {
//            switch (tm.weekday) {
//                case 1:
//                    return "monday";
//                case 2:
//                    return "tuesday";
//                case 3:
//                    return "wednesday";
//                case 4:
//                    return "thurdsday";
//                case 5:
//                    return "friday";
//                case 6:
//                    return "saturday";
//                case 7:
//                    return "sunday";
//            }
//        } else if (tm.year == calendar.year) {
//            return '${tm.day} $month';
//        } else {
//            return '${tm.day} $month ${tm.year}';
//        }
//        return "";
//    }
//
//}
