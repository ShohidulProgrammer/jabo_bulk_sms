package com.ideaxen.hr.ideasms.httpHelper;

import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class ApiHandler  {


    // get gist key as super key
    static String getSuperKey(String appName, String sysVal) {
        System.out.println("This my Sys Val"+sysVal);
        String superKey =   sysVal;
        return  superKey;
    }


    // get sms data from server
    static String getInfoToSendSms(String targetURL, String superKey) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(targetURL+""+superKey);
//            URL url = new URL(targetURL+"?superkey="+superKey);
            System.out.println("Sms Info Url: "+url);

            // check the is valid or not
            // if valid url then get sms data
            if (URLUtil.isValidUrl(url.toString()) && Patterns.WEB_URL.matcher(url.toString()).matches()){
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder stringBuffer = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line).append("\n");
                    // Log.d("Response: ", " added to response >  " + line);   //here u ll get whole response...... :-)
                }

                String response = stringBuffer.toString();
                return response;
            }
            else {
                System.out.println("Invalid Url: "+url);
                return null;
            }
        } catch (Exception ex) {
            Log.e("App", "ServerDataManager", ex);
            return null;
        } finally {
            if (connection != null) {
                // on end of data loading close the server
                connection.disconnect();
//                Log.e("App", "Server disconnected Successfully!");
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
