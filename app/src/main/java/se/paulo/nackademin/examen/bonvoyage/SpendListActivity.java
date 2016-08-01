package se.paulo.nackademin.examen.bonvoyage;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bonvoyage.database.DatabaseHelper;

/**
 * Created by Paulo Vila Nova on 2016-07-29.
 */
public class SpendListActivity extends ListActivity implements AdapterView.OnItemClickListener  {

    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private List<Map<String, Object>> spend;
    private String dateBefore = "";

    private String selectedDescription;
    private int selectedIdSpend;



    private int ItemId;

    private AlertDialog alert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.spend_list);

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        String[] from = {"date", "description", "value", "category"};
        int[] to = {R.id.spend_date, R.id.spend_description, R.id.spend_value, R.id.linearLayoutSpendCategory};

        SimpleAdapter adapter = new SimpleAdapter(this, spendList(), R.layout.spend_list, from, to);
        adapter.setViewBinder(new SpendViewBinder()); // class SpendViewBinder
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        // register the new context menu..
        registerForContextMenu(getListView());

        retrieveActualTripID();

    }

    // Get from BundleExtra the selected voyage _id..
    public int retrieveActualTripID(){

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            int result = b.getInt("DestinationID");
            Log.d("DestinationID", "Bundle Extra: " + result);
            return result;
        }else {
            return 0;
        }
    }

    private List<Map<String, Object>> spendList() {

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql1 = "SELECT _id, date, description, value, category, voyage_id FROM spending WHERE voyage_id=?";
        Cursor cursor = db.rawQuery(sql1, new String[]{Long.toString(retrieveActualTripID())});
        cursor.moveToFirst();

        spend = new ArrayList<Map<String, Object>>();

        Log.d("SpendListActivity", "Passing here..");


        for (int i = 0; i < cursor.getCount(); i++) {

            Log.d("SpendListActivity", "Passing here too..");
            int id = cursor.getInt(0);
            String date = cursor.getString(1);
            Log.d("Date", "date: " + date);
            String description = cursor.getString(2);
            double value = cursor.getDouble(3);
            String category = cursor.getString(4);
            int tripId = cursor.getInt(5);

            Log.d("Database Info", "BudgetTable: " + " id: "+ id + " - " + date + " - " + description + " - " + value + " - " + category + " - " + tripId);

            Map<String, Object> item = new HashMap<String, Object>();
            item.put("date", date);
            item.put("description", description);
            item.put("value", getString(R.string.money) + " " + value);

            if (category.equalsIgnoreCase("fuel")) {
                item.put("category", R.color.category_fuel);
            } else if (category.equalsIgnoreCase("food")) {
                item.put("category", R.color.category_feeding);
            } else if (category.equalsIgnoreCase("transportation")) {
                item.put("category", R.color.category_transport);
            } else if (category.equalsIgnoreCase("accommodation")) {
                item.put("category", R.color.category_accommodation);
            } else if (category.equalsIgnoreCase("others")) {
                item.put("category", R.color.category_others);
            }

            spend.add(item);
            cursor.moveToNext();
        }

        return spend;
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = spend.get(position);
        setSelectedDescription((String) map.get("description"));
        alertBeforeClose();
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    /**
     * Method to use when onBackPressed() is used.
     */
    public void alertBeforeClose() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(R.string.delete_spendings);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = helper.getReadableDatabase();
                long result = db.delete("spending", "description=?", new String[]{getSelectedDescription()});

                if (result != -1) {
                    Toast.makeText(getApplicationContext(), "Spend Removed..", Toast.LENGTH_SHORT).show();
                    getListView().invalidateViews();
                    spend.remove(getListView());
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(), "Spend NOT Removed..", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext(), VoyageListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do Nothing
            }
        });

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class SpendViewBinder implements SimpleAdapter.ViewBinder {


        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            Log.d("TEst Spend","View :" + view + " data: " + data + " textRepresentation: " + textRepresentation);

            if (view.getId() == R.id.spend_date) {
                if (!dateBefore.equals(data)) {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dateBefore = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }
            if (view.getId() == R.id.linearLayoutSpendCategory) {
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }


    //GETTERS AND SETTERS
    public String getSelectedDescription() {
        return selectedDescription;
    }

    public void setSelectedDescription(String selectedDescription) {
        this.selectedDescription = selectedDescription;
    }

    public int getSelectedIdSpend() {
        return selectedIdSpend;
    }

    public void setSelectedIdSpend(int selectedIdSpend) {
        this.selectedIdSpend = selectedIdSpend;
    }


}
