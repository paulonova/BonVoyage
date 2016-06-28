package bonvoyage.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bonvoyage.database.DatabaseHelper;


public class VoyageArrayAdapter extends ArrayAdapter<Object> {

    private List<Map<String, Object>> itemVoyage;
    private DatabaseHelper helper;

    public VoyageArrayAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);



    }

    private List<Map<String, Object>> listTrips() {

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, type_trip, destiny, arrive_date, exit_date, budget, number_peoples FROM trip";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        itemVoyage = new ArrayList<Map<String, Object>>();


        return itemVoyage;
    }
}
