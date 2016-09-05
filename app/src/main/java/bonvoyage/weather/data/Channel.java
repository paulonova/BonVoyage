package bonvoyage.weather.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paulo Vila Nova on 2016-09-01.
 */
public class Channel implements JSONPopulator  {

    private Item item;
    private Units units;



    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }


    public Item getItem() {
        return item;
    }

    public Units getUnits() {
        return units;
    }
}
