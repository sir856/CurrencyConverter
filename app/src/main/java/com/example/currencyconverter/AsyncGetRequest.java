package com.example.currencyconverter;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AsyncGetRequest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        String day = strings[0];
        String month = strings[1];
        String year = strings[2];

        return getContent(day, month, year);
    }

    private String getContent(String day, String month, String year) {
        try {
            URL url=new URL("https://www.cbr-xml-daily.ru/archive/" + year + "/" + month + "/" + day + "/daily_json.js");
            HttpsURLConnection c = (HttpsURLConnection) url.openConnection();

            c.setRequestMethod("GET");
            c.setReadTimeout(1000);
            c.connect();

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



}
