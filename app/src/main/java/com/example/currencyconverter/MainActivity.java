package com.example.currencyconverter;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText nominal;
    EditText date;
    TextView answer;
    Spinner fromCurrency;
    Spinner toCurrency;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nominal = findViewById(R.id.Nominal);
        date = findViewById(R.id.Date);
        answer = findViewById(R.id.Answer);
        fromCurrency = findViewById(R.id.FromCurrency);
        toCurrency = findViewById(R.id.ToCurrency);

        updateLabel();

        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button btn = findViewById(R.id.ConvertBtn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вычисляем значение конвертируемой валюты
                convert();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    private void convert() {
        String dateStr = date.getText().toString();

        String[] dateSplit = dateStr.split("\\.");

        try {
            AsyncTask<String, Void, String> task = new AsyncGetRequest().execute(dateSplit);
            String result = task.get();

            JSONObject json = new JSONObject(result);

            if (!json.isNull("explanation")) {
                answer.setText(json.getString("explanation"));
                return;
            }

            JSONObject jsonValute = json.getJSONObject("Valute");

            String fromCurrencyName = fromCurrency.getSelectedItem().toString();
            String toCurrencyName = toCurrency.getSelectedItem().toString();

            double value = Double.valueOf(nominal.getText().toString());
            double from = fromCurrencyName.equals("RUB") ? 1 : jsonValute.getJSONObject(fromCurrencyName).getDouble("Value");
            double to = toCurrencyName.equals("RUB") ? 1 : jsonValute.getJSONObject(toCurrencyName).getDouble("Value");

            double convert = from / to * value;

            String text = String.format("%.2f %s -> %.2f %s на %s", value, fromCurrencyName, convert, toCurrencyName, dateStr);
            answer.setText(text);
        }
        catch(JSONException ex) {

        }
        catch(ExecutionException | InterruptedException ex) {

        }
    }
}
