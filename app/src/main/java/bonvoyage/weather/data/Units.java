package bonvoyage.weather.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paulo Vila Nova on 2016-09-01.
 */
public class Units implements JSONPopulator  {

    private String temperature;

    @Override
    public void populate(JSONObject data) {

        temperature = data.optString("temperature");
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    public String getTemperature() {
        return temperature;
    }
}
