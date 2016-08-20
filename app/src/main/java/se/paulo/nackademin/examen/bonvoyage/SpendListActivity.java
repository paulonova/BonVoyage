package se.paulo.nackademin.examen.bonvoyage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bonvoyage.adapters.SpendingListAdapter;
import bonvoyage.database.DatabaseHelper;
import bonvoyage.objects.Spending;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Paulo Vila Nova on 2016-07-29.
 */
public class SpendListActivity extends AppCompatActivity implements SpendingListAdapter.ItemClickCallBack  {

    private DatabaseHelper helper;
    private String dateBefore = "";

    private String selectedDescription;
    private int selectedIdSpend;
    private int ItemId;
    private AlertDialog alert;


    @Bind(R.id.spendToolbar) Toolbar toolbar;
    private RecyclerView recView;
    private SpendingListAdapter adapter;

    Spending spending;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spend_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        helper = new DatabaseHelper(this);
        //dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        recView = (RecyclerView)findViewById(R.id.spending_fragment_list);
        //LayoutManager: GridLayoutManager and StaggeredGridLayoutManager
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SpendingListAdapter(spendList(), this);
        recView.setAdapter(adapter);
        adapter.setItemClickCallBack(this);

        // register the new context menu..
        registerForContextMenu(recView);
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


    private List<Spending> spendList() {

        List<Spending> data = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql1 = "SELECT _id, date, description, value, category, voyage_id FROM spending WHERE voyage_id=?";
        Cursor cursor = db.rawQuery(sql1, new String[]{Long.toString(retrieveActualTripID())});
        cursor.moveToFirst();

        if(cursor.getCount()== 0){
            //Start en AlertDialog...
        }


        for (int i = 0; i < cursor.getCount(); i++) {

            spending = new Spending();

            spending.setId(cursor.getInt(0));
            spending.setDate(cursor.getString(1));
            spending.setDescription(cursor.getString(2));
            spending.setValue(cursor.getDouble(3));
            spending.setCategory(cursor.getString(4));
            spending.setVoyageId(cursor.getInt(5));

            Log.d("Database Info", "BudgetTable: " + " id: "+ spending.getId() + " - " + spending.getDate() + " - " + spending.getDescription()
                    + " - " + spending.getValue() + " - " + spending.getDescription() + " - " + spending.getVoyageId());

            data.add(spending);
            cursor.moveToNext();
        }

        cursor.close();
        return data;
    }


//Interfaces created in SpendingListAdapter to make the viewItem clickable..
    @Override
    public void onItemClick(int position) {
        spending = spendList().get(position);
        setSelectedDescription(spending.getDescription());
        alertBeforeClose();
    }

    @Override
    public void onStarIconClick(int p) {

    }



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
                    //getListView().invalidateViews();
                    recView.invalidate();
                    spendList().remove(recView);


                } else {
                    Toast.makeText(getApplicationContext(), "Spend NOT Removed..", Toast.LENGTH_SHORT).show();
                }

                startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));  //to restart the activity without finish()
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
        Intent intent = new Intent(this, VoyageListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
