package com.example.currencyconverter;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class TwoCurrencies extends Fragment {
    EditText nominal;
    EditText date;
    TextView answer;
    Spinner fromCurrency;
    Spinner toCurrency;
    final Calendar myCalendar = Calendar.getInstance();

    public static TwoCurrencies newInstance() {
        TwoCurrencies fragment = new TwoCurrencies();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_currencies, container, false);

        nominal = view.findViewById(R.id.Nominal);
        date = view.findViewById(R.id.Date);
        answer = view.findViewById(R.id.Answer);
        fromCurrency = view.findViewById(R.id.FromCurrency);
        toCurrency = view.findViewById(R.id.ToCurrency);

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

        Button btn = view.findViewById(R.id.ConvertBtn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вычисляем значение конвертируемой валюты
                convert();
            }
        });
        return view;
    }



    private void updateLabel() {
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    private void convert() {

        try {
            AsyncTask<Calendar, Void, String> task = new AsyncGetRequest().execute(myCalendar);
            String result = task.get();

            updateLabel();
            String dateStr = date.getText().toString();

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
