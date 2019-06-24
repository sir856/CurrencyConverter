package converter.history;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class History {

    private LinkedList<HistoryItem> items;
    private static History instance;

    int maxSize = 10;

    public static void initInstance(SharedPreferences preferences) {
        if (instance != null) {
            return;
        }

        LinkedList<HistoryItem> items = new LinkedList<>();
        String itemString = preferences.getString("0", null);

        Gson gson = new Gson();

        for (int i = 1; itemString != null; i++) {
            items.add(gson.fromJson(itemString, HistoryItem.class));

            itemString = preferences.getString(String.valueOf(i),null);
        }

        instance = new History(items);
    }

    public static History getInstance() {
        return instance;
    }

    private History (LinkedList<HistoryItem> items) {
        this.items = items;
    }

    public void addItem(HistoryItem item) {
        if (items.size() == maxSize) {
            items.pollLast();
        }

        items.push(item);
    }

    public List<HistoryItem> getItems() {
        return new ArrayList<>(items);
    }
}
