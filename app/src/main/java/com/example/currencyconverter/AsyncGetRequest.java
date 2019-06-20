package com.example.currencyconverter;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class AsyncGetRequest extends AsyncTask<Calendar, Void, String> {

    @Override
    protected String doInBackground(Calendar... calendars) {

        return getContent(calendars[0]);
    }

    private String getContent(Calendar calendar) {
        try {

            String myFormat = "yyyy/MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            HttpsURLConnection c = getConnection(sdf.format(calendar.getTime()));

            while (c.getResponseCode() == 404) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                c = getConnection(sdf.format(calendar.getTime()));
            }

            BufferedReader reader = c.getResponseCode() == 200 ? new BufferedReader(new InputStreamReader(c.getInputStream()))
                    : new BufferedReader(new InputStreamReader(c.getErrorStream()));

            StringBuilder buf = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buf.append(line).append("\n");
            }

            reader.close();

            return buf.toString();
        }
        catch (IOException ex) {
            return "{\"explanation\": \"Нет интернет соединения\"}";
        }
    }

    private HttpsURLConnection getConnection(String date) throws IOException {
        URL url = new URL("https://www.cbr-xml-daily.ru/archive/" + date + "/daily_json.js");
        HttpsURLConnection c = (HttpsURLConnection) url.openConnection();

        c.setRequestMethod("GET");
        c.setReadTimeout(1000);
        c.connect();

        return c;
    }



}
