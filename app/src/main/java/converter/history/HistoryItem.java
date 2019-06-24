package converter.history;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HistoryItem {

    @SerializedName("operation")
    private List<String> conversions;

    public HistoryItem(List<String> conversions) {
        this.conversions = conversions;
    }

    public HistoryItem(String operation) {
        conversions = new ArrayList<>();
        conversions.add(operation);
    }

    public List<String> getConversions() {
        return conversions;
    }
}
