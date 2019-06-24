package converter;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import java.util.List;

import converter.history.History;
import converter.history.HistoryItem;
import converter.pages.PagerAdapter;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(
                new PagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        History.initInstance(getPreferences(MODE_PRIVATE));
    }

    @Override
    protected void onStop() {
        List<HistoryItem> items = History.getInstance().getItems();

        Gson gson = new Gson();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        for (int i = 0; i < items.size(); i++) {
            HistoryItem item = items.get(i);
            preferences.edit().putString(String.valueOf(i), gson.toJson(item, HistoryItem.class)).commit();
        }

        super.onStop();
    }
}
