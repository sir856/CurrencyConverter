package converter.pages;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import converter.R;
import converter.history.History;
import converter.history.HistoryItem;


public class HistoryPage extends Fragment {
    History history;
    static HistoryPage fragment;


    public static HistoryPage newInstance() {
        if (fragment == null) {
            fragment = new HistoryPage();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        history = History.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showItems();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("DEBUG", "HistoryPage is visible to user");
            showItems();
        }
    }

    public void showItems() {

        View view = getView();

        if (view == null) {
            return;
        }

        List<HistoryItem> items = history.getItems();
        LinearLayout itemsContainer = view.findViewById(R.id.Container);
        itemsContainer.removeAllViews();

        for (HistoryItem item : items) {
            CardView card = new CardView(view.getContext());
            LinearLayout conversionsContainer = new LinearLayout(view.getContext());
            conversionsContainer.setOrientation(LinearLayout.VERTICAL);
            card.addView(conversionsContainer);

            for (String conversion : item.getConversions()) {
                TextView tv = new TextView(view.getContext());
                tv.setText(conversion);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams lp =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 5, 0, 5);
                conversionsContainer.addView(tv, lp);

            }

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(15, 20, 15, 20);

            itemsContainer.addView(card, lp);
        }
    }


}
