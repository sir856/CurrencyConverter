package converter.pages;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import converter.R;
import converter.history.History;
import converter.history.HistoryItem;
import converter.request.AsyncGetRequest;


public class CurrenciesPage extends Fragment {

    EditText date;
    TextView usdText;
    TextView eurText;
    TextView jpyText;
    final Calendar myCalendar = Calendar.getInstance();
    final History history = History.getInstance();

    public static CurrenciesPage newInstance() {
        CurrenciesPage fragment = new CurrenciesPage();
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.currencies, container, false);

        date = view.findViewById(R.id.Date);

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
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

                Calendar minDate = Calendar.getInstance();
                minDate.set(1999, 0, 1);
                dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

                dialog.show();
            }
        });

        view.findViewById(R.id.Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrencies();
            }
        });

        return view;
    }

    private void updateLabel() {
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    private void setCurrencies() {

        try {
            AsyncTask<Calendar, Void, String> task = new AsyncGetRequest().execute(myCalendar);
            String result = task.get();

            updateLabel();
            String dateStr = date.getText().toString();
            View view = getView();

            usdText = view.findViewById(R.id.AnswerUSD);
            eurText = view.findViewById(R.id.AnswerEUR);
            jpyText = view.findViewById(R.id.AnswerJPY);

            JSONObject json = new JSONObject(result);

            if (!json.isNull("explanation")) {
                setErrorMessage(json.getString("explanation"));
                return;
            }

            JSONObject jsonValute = json.getJSONObject("Valute");

            double usd = jsonValute.getJSONObject("USD").getDouble("Value");
            double eur = jsonValute.getJSONObject("EUR").getDouble("Value");
            double jpy = jsonValute.getJSONObject("JPY").getDouble("Value");

            List<String> strings = new ArrayList<>();
            strings.add("1.00 USD -> " + usd + " RUB на " + dateStr);
            strings.add("1.00 EUR -> " + eur + " RUB на " + dateStr);
            strings.add("1.00 JPY -> " + jpy + " RUB на " + dateStr);

            usdText.setText(strings.get(0));
            eurText.setText(strings.get(1));
            jpyText.setText(strings.get(2));

            getView().findViewById(R.id.CardUSD).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.CardEUR).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.CardJPY).setVisibility(View.VISIBLE);

            history.addItem(new HistoryItem(strings));
        }
        catch(JSONException ex) {
            setErrorMessage("Ошибка json");
        }
        catch(ExecutionException | InterruptedException ex) {
            setErrorMessage("Ошибка при выполнении запроса");
        }
    }

    private void setErrorMessage(String message) {
        usdText.setText(message);
        getView().findViewById(R.id.CardUSD).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.CardEUR).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.CardJPY).setVisibility(View.INVISIBLE);
    }

}
