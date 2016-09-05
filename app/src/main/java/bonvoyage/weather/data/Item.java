package bonvoyage.weather.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paulo Vila Nova on 2016-09-01.
 */
public class Item implements JSONPopulator {

    private Condition condition;

    @Override
    public void populate(JSONObject data) {

        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    public Condition getCondition() {
        return condition;
    }
}
