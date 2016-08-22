package se.paulo.nackademin.examen.bonvoyage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bonvoyage.adapters.VoyageListAdapter;
import bonvoyage.database.DatabaseHelper;
import bonvoyage.objects.Spending;
import bonvoyage.objects.Voyage;

//ListActivity
public class VoyageListActivity extends AppCompatActivity implements VoyageListAdapter.ItemClickCallBack,
                                                                DialogInterface.OnClickListener{

    public static final String FROM_VOYAGE_LIST = "from_voyage_list";
    public static final String VOYAGE_ID = "voyage_id";
    public static final String IS_CHECK_BOX_SELECTED = "isCheckBoxSelected";
    private DatabaseHelper helper;
    private Double limitValue;
    private AlertDialog dialogConfirmation;
    private AlertDialog alertDialog;
    private long selectItemID;
    private String selectedTripDestination;
    private int selectedDestinationId;
    Voyage voyage;
    Spending spending;
    private int selectedVoyage;
    private double totalSpend;
    private double alertLimit;
    private boolean showLimitCheckBox;
    Switch limitSwitchButton;



    SharedPreferences myPreferences;
    int currentUserId;
    ImageButton imageButton;
    private RecyclerView recView;
    private VoyageListAdapter adapter;
    AlertDialog alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voyage_list);

        helper = new DatabaseHelper(this);
        spending = new Spending();
        limitSwitchButton = (Switch)this.findViewById(R.id.switch_on_off);
//        SharedPreferences prefs = this.getSharedPreferences(IS_CHECK_BOX_SELECTED, Context.MODE_PRIVATE);  //TODO:
//        prefs.getBoolean(IS_CHECK_BOX_SELECTED, false);

        //ToolBar setting
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


        //Getting a current userId
        myPreferences = getSharedPreferences(LoginPage.USER_INFO_PREFERENCE, Context.MODE_PRIVATE);
        currentUserId = myPreferences.getInt("user_id", 0);


        // Instantiate SharedPreferences and retrieve the limit value of the budget..
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String value = preferences.getString("value_limit", "80"); // 0 to 100%
        Log.d("Limit Value Saved", "The Value is: " + value);

        limitValue = Double.valueOf(value);

        /*Implementing of RecyclerView*/
        recView = (RecyclerView)findViewById(R.id.voyage_fragment_list);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VoyageListAdapter(listVoyage(), this);
        recView.setAdapter(adapter);
        adapter.setItemClickCallBack(this);

        this.alertDialog = buildAlertDialog();
        this.dialogConfirmation = buildDialogConfirmation();

    }




    public  List<Voyage> listVoyage() {

        List<Voyage> data = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id, type_voyage, destiny, arrive_date, exit_date, budget, number_peoples, user_id FROM voyage WHERE user_id=" + currentUserId;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        if(cursor.getCount()== 0){
        }

        for (int i = 0; i < cursor.getCount(); i++) {

            voyage = new Voyage();

            //Saving direct into the Voyage
            voyage.setId(cursor.getInt(0));
            voyage.setTypeVoyage(cursor.getString(1));
            voyage.setDestiny(cursor.getString(2));
            voyage.setArrivalDate(cursor.getString(3));
            voyage.setExitDate(cursor.getString(4));
            voyage.setBudget(cursor.getDouble(5));
            voyage.setNumberPeoples(cursor.getInt(6));
            voyage.setUser_id(cursor.getInt(7));

            voyage.setShowLimitAlertDialog(true);

            voyage.setAlertSpend((voyage.getBudget() * limitValue) / 100);  //TODO: working here!
            calcTotalSpend(db, voyage.getId() + "");
            Log.d("SWITCH BUTTON","" + voyage.isShowLimitAlertDialog());

//            if(voyage.isShowLimitAlertDialog()){
//                alertOmLimitValue(voyage);
//            }


            Log.i("Database Info TRIP", "Info: " + "ID: " + voyage.getId() + " - " + "TypeVoyage: " + voyage.getTypeVoyage() + " - " +
                    "Destiny: " + voyage.getDestiny() + " - " + "ArrivalDate: " + voyage.getArrivalDate() + " - " +
                    "ExitDate: " + voyage.getExitDate() + " - " + "Budget: " + voyage.getBudget() + " - Number of people: " + voyage.getNumberPeoples());

            data.add(voyage);
            Log.d("CHECKING THE LIMITS"," limitValue: " + voyage.getAlertSpend() );
            cursor.moveToNext();

        }

        cursor.close();
        return data;
    }

    public String saveSelectedVoyageIDInSharedPreferences(int itemId){
        SharedPreferences pref = getSharedPreferences("selected_item_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("selected_item_id", itemId);
        editor.commit();

        Log.d("SELECTED ITEM ID","" +  pref.getInt("selected_item_id", 0));

        return null;
    }

    // Used to get the ID from trip according to destiny
    public int returnSelectedVoyageId(String destiny){

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id FROM voyage WHERE destiny=?";
        Cursor cursorID = db.rawQuery(sql, new String[]{destiny.toString()});
        cursorID.moveToFirst();

        int result = cursorID.getInt(0);
        Log.d("Actual Id", "ID: " + result);
        saveSelectedVoyageIDInSharedPreferences(result);
        return result;
    }

    //Save value from LimitCheckBox in SheredPreferences to retrieve on start activity..
    public String saveLimitCheckBoxInSharedPreferences(boolean isChecked){
        SharedPreferences pref = getSharedPreferences(IS_CHECK_BOX_SELECTED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_CHECK_BOX_SELECTED, true);
        editor.commit();

        Log.d("SELECTED ITEM ID","" +  pref.getBoolean(IS_CHECK_BOX_SELECTED, false));

        return null;
    }



        //AlertDialog to limitSpending..
        public void alertOmLimitValue(Voyage voyage) {

            if(voyage.getTotalSpend() > voyage.getAlertSpend()){
//                alertOmLimitValue(voyage.getDestiny());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Limit Alert");
                String msg = "Hi! " + "In voyage: " + voyage.getDestiny() + ", You are in the limit of your budget!";
                builder.setIcon(R.drawable.cash_multiple);
                builder.setMessage(msg);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                });
                alert = builder.create();
                alert.show();


            }else{
                //Do nothing
            }

    }


    public double calcTotalSpend(SQLiteDatabase db, String id) {
        Cursor cursor = db.rawQuery("SELECT SUM(value) FROM SPENDING WHERE VOYAGE_ID = ?", new String[]{id});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        Log.d("SUM(value)", "VALUE: " + cursor.getDouble(0));
        voyage.setTotalSpend(cursor.getDouble(0));

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
                Intent spendIntent = new Intent(getApplicationContext(), MenuVoyageActivity.class);
                spendIntent.putExtra(FROM_VOYAGE_LIST, true);
                spendIntent.putExtra(VOYAGE_ID, getSelectedDestinationId());
                Log.d("TEST ID","" + getSelectedDestinationId());
                startActivity(spendIntent);
                finish();

                break;

            case 1: //Show my Spendings
                Intent intent = new Intent(getApplicationContext(),SpendListActivity.class );
                intent.putExtra("DestinationID", getSelectedDestinationId());  // Sending the selected trip..
                intent.putExtra("ItemId", getSelectItemID());
                startActivity(intent);
                finish();

                break;

            case 2: //Remove
                dialogConfirmation.show();
                break;

            case DialogInterface.BUTTON_POSITIVE:
                listVoyage().remove(selectedVoyage);
                SQLiteDatabase db = helper.getReadableDatabase();
                db.delete("voyage", "_id=?", new String[]{Integer.toString(getSelectedDestinationId())});
                db.delete("spending", "voyage_id=?", new String[]{Integer.toString(getSelectedDestinationId())});

                startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));  //to restart the activity without finish()
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmation.dismiss();
                break;

        }


    }

    @Override
    public void onSwitchButtonClick(int position) { //TODO: implement switch button here..

        voyage = (Voyage)listVoyage().get(position);
        //alertOmLimitValue(listVoyage().get(position));
        //limitSwitchButton.isChecked();


//        Log.d("SelectedSwitch", "Item: " + position + " Destiny " + voyage.getDestiny() +  " isChecked: " + limitSwitchButton.isChecked());

//        boolean switchButton = (Boolean) listVoyage().get(position).isShowLimitAlertDialog();
//        listVoyage().get(position).setShowLimitAlertDialog(true);
//        limitSwitchButton.setChecked(listVoyage().get(position).isShowLimitAlertDialog());
//
//        Log.d("SelectedSwitch", "Item: " + position + " isChecked: " + limitSwitchButton.isChecked() + " " + switchButton);
//        //Toast.makeText(getApplication(), "Item: " + position + " isChecked: " + limitSwitchButton.isChecked() + " " + switchButton, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {

        selectedDestinationId = 0;
        this.selectedVoyage = position;
        alertDialog.show();

        listVoyage().get(position);
        String destiny = (String) listVoyage().get(position).getDestiny();
        setSelectedTripDestination(destiny);
        setSelectedDestinationId(returnSelectedVoyageId(destiny));  //get the selected voyage id

        //setSelectItemID(id + 1);
        Log.d("SelectedTripID", "Destination: " + getSelectedTripDestination() + " ID: " + returnSelectedVoyageId(destiny) + " ItemId: " + getSelectItemID());


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


    public boolean isShowLimitCheckBox() {
        return showLimitCheckBox;
    }

    public void setShowLimitCheckBox(boolean showLimitCheckBox) {
        this.showLimitCheckBox = showLimitCheckBox;
    }



}
