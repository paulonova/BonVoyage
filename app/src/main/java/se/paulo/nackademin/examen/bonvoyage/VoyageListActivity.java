package se.paulo.nackademin.examen.bonvoyage;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;

    private Double limitValue;

    private AlertDialog dialogConfirmation;
    private AlertDialog alertDialog;

    private long selectItemID;
    private String selectedTripDestination;
    private int selectedDestinationId;

    SpendingFragment spendingFragment = null;

    private int selectedVoyage;
    private List<Map<String, Object>> itemVoyage;

    private double totalSpend;
    private double alertLimit;
    ListView listView;
    SharedPreferences myPreferences;
    int currentUserId;

    ImageButton imageButton;

    public static final String TRIP_VACATIONS = "Vacation";
    public static final String TRIP_BUSINESS = "Business";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voyage_list);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        //Connecting to MenuVoyageActivity!
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageButton = (ImageButton)findViewById(R.id.img_menu_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVoyage = new Intent(getApplicationContext(), MenuVoyageActivity.class);
                intentVoyage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentVoyage);
                finish();
            }
        });

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        //Getting a current userId
        myPreferences = getSharedPreferences(LoginPage.USER_INFO_PREFERENCE, Context.MODE_PRIVATE);
        currentUserId = myPreferences.getInt("user_id", 0);


        // Instantiate SharedPreferences and retrieve the limit value of the budget..
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String value = preferences.getString("value_limit", "50"); // 0 to 100%
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

        this.alertDialog = buildAlertDialog();
        this.dialogConfirmation = buildDialogConfirmation();

    }


    private List<Map<String, Object>> listVoyage() {

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, type_voyage, destiny, arrive_date, exit_date, budget, number_peoples, user_id FROM voyage WHERE user_id=" + currentUserId;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        itemVoyage = new ArrayList<Map<String, Object>>();
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

            Log.i("Database Info TRIP", "Info: " + "ID: " + id + " - " + "TypeVoyage: " + typeVoyage + " - " +
                    "Destiny: " + destiny + " - " + "ArrivalDate: " + arrivalDate + " - " +
                    "ExitDate: " + exitDate + " - " + "Budget: " + budget + " - Number of people: " + numberPeoples);

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

            itemVoyage.add(item);
            cursor.moveToNext();

        }

        cursor.close();
        return itemVoyage;
    }

    // Used to get the ID from trip according to destiny
    public int returnSelectedVoyageId(String destiny){

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id FROM voyage WHERE destiny=?";
        Cursor cursorID = db.rawQuery(sql, new String[]{destiny.toString()});
        cursorID.moveToFirst();

        int result = cursorID.getInt(0);
        Log.d("Actual Id", "ID: " + result);
        return result;
    }


    private double calcTotalSpend(SQLiteDatabase db, String id) {
        Cursor cursor = db.rawQuery("SELECT SUM(value) FROM SPENDING WHERE VOYAGE_ID = ?", new String[]{id});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        Log.d("SUM(value)", "VALUE: " + cursor.getDouble(0));
        cursor.close();
        return total;
    }

    //*************************** ALERTS ********************************
    private AlertDialog buildAlertDialog() {
        final CharSequence[] items = {
                "Register new spending",
                "Show my spendings",
                "Remove"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(items, this);
        return builder.create();
    }

    // AlertDialog to confirm the remove..
    private AlertDialog buildDialogConfirmation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are You sure you want to remove?");
        builder.setPositiveButton("Yes", this);
        builder.setNegativeButton("No", this);
        return builder.create();
    }

    //*************************** ALERTS ********************************

    @Override
    public void onClick(DialogInterface dialog, int item) {
        Log.d("Options Dialog", "I am Here in OPTIONS ");

        switch(item){

            case 0: //New Spendings
                Toast.makeText(getApplicationContext(), "Go to Spending Fragment..", Toast.LENGTH_SHORT).show();

                break;

            case 1: //Show my Spendings
                Toast.makeText(getApplicationContext(), "Show my Spendings..", Toast.LENGTH_SHORT).show();

                break;

            case 2: //Remove
                dialogConfirmation.show();
                break;

            case DialogInterface.BUTTON_POSITIVE:
                itemVoyage.remove(selectedVoyage);
                SQLiteDatabase db = helper.getReadableDatabase();
                db.delete("voyage", "_id=?", new String[]{Integer.toString(getSelectedDestinationId())});
                db.delete("spending", "voyage_id=?", new String[]{Integer.toString(getSelectedDestinationId())});
                getListView().invalidateViews();

                Intent intent = new Intent(getApplicationContext(), MenuVoyageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmation.dismiss();
                break;

        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        this.selectedVoyage = position;
        alertDialog.show();

        Map<String, Object> map = itemVoyage.get(position);
        String destiny = (String) map.get("destiny");

        setSelectedTripDestination(destiny);
        setSelectedDestinationId(returnSelectedVoyageId(destiny));  //get the selected voyage id

        setSelectItemID(id + 1);
        Log.d("SelectedTripID", "Destination: " + getSelectedTripDestination() + " ID: " + returnSelectedVoyageId(destiny) + " ItemId: " + getSelectItemID());

        //Toast.makeText(getApplication(), "Item: " + position, Toast.LENGTH_SHORT).show();


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

    public String getSelectedTripDestination() {
        return selectedTripDestination;
    }

    public void setSelectedTripDestination(String selectedTripDestination) {
        this.selectedTripDestination = selectedTripDestination;
    }

    public int getSelectedDestinationId() {
        return selectedDestinationId;
    }

    public void setSelectedDestinationId(int selectedDestinationId) {
        this.selectedDestinationId = selectedDestinationId;
    }

    public long getSelectItemID() {
        return selectItemID;
    }

    public void setSelectItemID(long selectItemID) {
        this.selectItemID = selectItemID;
    }
}
