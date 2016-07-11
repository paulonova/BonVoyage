package se.paulo.nackademin.examen.bonvoyage;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bonvoyage.database.DatabaseHelper;
import bonvoyage.objects.Voyage;

public class VoyageListActivity extends ListActivity implements AdapterView.OnItemClickListener,
                                                                DialogInterface.OnClickListener, SimpleAdapter.ViewBinder{

    String[] contactList = {"Person1","Person2","Person3","Person4","Person5","Person6"};
    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;

    private List<Map<String, Object>> itemTrips;
    private Double limitValue;

    private double totalSpend;
    private double alertLimit;
    ListView listView;
    SharedPreferences myPreferences;
    int currentUserId;

    public static final String TRIP_VACATIONS = "Vacation";
    public static final String TRIP_BUSINESS = "Business";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voyage_list);


        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        //Getting a current userId
        myPreferences = getSharedPreferences(LoginPage.USER_INFO_PREFERENCE, Context.MODE_PRIVATE);
        currentUserId = myPreferences.getInt("user_id", 0);


        // Instantiate SharedPreferences and retrieve the limit value of the budget..
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String value = preferences.getString("value_limit", "50"); //deveria ser 0..
        Log.d("Limit Value Saved", "The Value is: " + value);
        limitValue = Double.valueOf(value);

        String[] from = {"image", "destiny", "date", "total", "progressBar"};
        int[] to = {R.id.img_type_voyage, R.id.txtPlaceYouGo, R.id.txtDateYouGo, R.id.txtBudgetYouHave, R.id.progressBar};

        listView = (ListView)findViewById(android.R.id.list);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listVoyage(), R.layout.voyage_list_modell, from, to);
        simpleAdapter.setViewBinder(this);
        setListAdapter(simpleAdapter);


        Log.i("LIST-VOYAGE","" + listVoyage().toString());
        getListView().setOnItemClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuVoyageActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private List<Map<String, Object>> listVoyage() {

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, type_voyage, destiny, arrive_date, exit_date, budget, number_peoples, user_id FROM voyage WHERE user_id=" + currentUserId;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        itemTrips = new ArrayList<Map<String, Object>>();
        Voyage voyage;


        if(cursor.getCount()== 0){

        }

        for (int i = 0; i < cursor.getCount(); i++) {

            // Getting values from DB..
            int id = cursor.getInt(0);
            String typeVoyage = cursor.getString(1);
            String destiny = cursor.getString(2);
            String arrivalDate = cursor.getString(3);
            String exitDate = cursor.getString(4);
            double budget = cursor.getDouble(5);
            int numberPeoples = cursor.getInt(6);
            int userId = cursor.getInt(7);

            double alert = budget * limitValue / 100;
            double totalSpend = calcTotalSpend(db, id + "");
            setTotalSpend(totalSpend);


            Log.d("Database Info TRIP", "Info: " + "ID: " + id + " - " + "TypeVoyage: " + typeVoyage + " - " +
                    "Destiny: " + destiny + " - " + "ArrivalDate: " + arrivalDate + " - " +
                    "ExitDate: " + exitDate + " - " + "Budget: " + budget + " - Number of people: " + numberPeoples);

            // public Voyage(Integer id, Integer user_id, String destiny, String typeVoyage, String arrivalDate, String exitDate, Double budget, Integer numberPeoples, Integer actualVoyage) {
            //voyage = new Voyage(id, userId, destiny, typeVoyage, arrivalDate, exitDate, budget, numberPeoples, id);

            Map<String, Object> item = new HashMap<String, Object>();

            if (typeVoyage.contains(TRIP_VACATIONS)) {
                item.put("image", R.drawable.vacation_photo);
            } else {
                item.put("image", R.drawable.business_man_photo);
            }

            item.put("id", id);
            item.put("destiny", destiny);
            item.put("date", arrivalDate + " to " + exitDate);

            item.put("total", "Total Spend: " + totalSpend + "    Budget: " + budget);
            Log.d("TOTAL_SPEND", "TOTAL: " + totalSpend);

            Double[] values = new Double[]{budget, alert, totalSpend};

            Log.d("ProgressBar", "values: " + "Budget: " + budget + " Alert: " + alert + " Total: " + totalSpend);
            item.put("progressBar", values);

            itemTrips.add(item);
            cursor.moveToNext();

        }

        cursor.close();
        return itemTrips;
    }

    private double calcTotalSpend(SQLiteDatabase db, String id) {
        Cursor cursor = db.rawQuery("SELECT SUM(value) FROM SPENDING WHERE VOYAGE_ID = ?", new String[]{id});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        Log.d("SUM(value)", "VALUE: " + cursor.getDouble(0));
        cursor.close();
        return total;
    }



    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.d("Options Dialog", "I am Here in OPTIONS ");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplication(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if (view.getId() == R.id.progressBar) {
                Double values[] = (Double[]) data;
                ProgressBar progressBar = (ProgressBar) view;
                progressBar.setMax(values[0].intValue());
                progressBar.setSecondaryProgress(values[1].intValue());
                progressBar.setProgress(values[2].intValue());
                return true;
        }

        return false;
    }


    //Getters and Setters
    public double getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(double totalSpend) {
        this.totalSpend = totalSpend;
    }

    public double getAlertLimit() {
        return alertLimit;
    }

    public void setAlertLimit(double alertLimit) {
        this.alertLimit = alertLimit;
    }
}
