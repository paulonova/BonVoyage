package se.paulo.nackademin.examen.bonvoyage;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import bonvoyage.database.DatabaseHelper;
import bonvoyage.fragments.FirstAppInfoFragment;
import bonvoyage.fragments.NewVoyageFragment;
import bonvoyage.fragments.SpendingFragment;
import bonvoyage.objects.Spending;
import bonvoyage.objects.Voyage;
import bonvoyage.preferences.SettingsActivity;

public class MenuVoyageActivity extends AppCompatActivity
                                implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    Voyage voyage;
    Spending spending;

    public static final String SPENDING_PREFERENCE = "spending_info";
    public static final String VOYAGE_PREFERENCE = "voyage_info";

    Bundle bundle;

    //All Fragments..
    NewVoyageFragment voyageFragment = null;
    SpendingFragment spendingFragment = null;
    VoyageListActivity voyageListActivity = null;


    private DatabaseHelper helper;

    //From nav_header_menu.xml ==> DrawerView viewGroups..
    TextView userName, userEmail;
    SharedPreferences myPreferences;

    int currentUserId;
    CheckBox beConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Lock screen in ORIENTATION_PORTRAIT / don´t crash the Fragments..
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //This is the first view in DrawerLayout..
        FirstAppInfoFragment firstAppInfoFragment = new FirstAppInfoFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_menu_container, firstAppInfoFragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //hiding the soft-keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                                                                R.string.navigation_drawer_open,
                                                                R.string.navigation_drawer_close);

        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Prepare access to database..
        helper = new DatabaseHelper(this);

        // Here we have control on actions in drawerView..
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // To change the TextViews in nav_header_menu layout..
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_menu, navigationView, false);
        navigationView.addHeaderView(headerView);

        userName = (TextView) headerView.findViewById(R.id.txtUsername);
        userEmail = (TextView) headerView.findViewById(R.id.txtUserEmail);

        //Retrieve information from SharedPreferences..
        myPreferences = getSharedPreferences(LoginPage.USER_INFO_PREFERENCE, Context.MODE_PRIVATE);
        userName.setText(myPreferences.getString("user_name", ""));
        userEmail.setText(myPreferences.getString("user_email", ""));
        currentUserId = myPreferences.getInt("user_id", 0);

        Log.i("HEADER INFO","" + userName.getText().toString());
        Log.i("HEADER INFO","" + userEmail.getText().toString());
        Log.i("USER ID","" + currentUserId);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        switch (id){

            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.action_info:
                intent = new Intent(this, BonVoyageInfoActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method to show Calendar in display.
    public void setArriveDate() {
        new DatePickerDialog(MenuVoyageActivity.this, voyageFragment.dArrival,  voyageFragment.arrivalCalendar.get(Calendar.YEAR),
                                                                                voyageFragment.arrivalCalendar.get(Calendar.MONTH),
                                                                                voyageFragment.arrivalCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setExitDate() {
        new DatePickerDialog(MenuVoyageActivity.this, voyageFragment.dExit,     voyageFragment.exitCalendar.get(Calendar.YEAR),
                                                                                voyageFragment.exitCalendar.get(Calendar.MONTH),
                                                                                voyageFragment.exitCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setSpendingDate() {
        new DatePickerDialog(MenuVoyageActivity.this, spendingFragment.dSpend,  spendingFragment.spendCalendar.get(Calendar.YEAR),
                                                                                spendingFragment.spendCalendar.get(Calendar.MONTH),
                                                                                spendingFragment.spendCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.new_register:
                //Set Fragment initially
                voyageFragment = new NewVoyageFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_menu_container, voyageFragment);
                fragmentTransaction.commit();
                break;

            case R.id.new_spending:
                //Set Fragment initially
                spendingFragment = new SpendingFragment();
                android.support.v4.app.FragmentTransaction fragmentSpendingTransaction = getSupportFragmentManager().beginTransaction();
                fragmentSpendingTransaction.replace(R.id.fragment_menu_container, spendingFragment);
                fragmentSpendingTransaction.commit();
                break;

            case R.id.show_voyages:
                Intent intentVoyage = new Intent(this, VoyageListActivity.class);
                intentVoyage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentVoyage);

                break;

            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                break;

            case R.id.nav_info:
                Intent intentInfo = new Intent(this, BonVoyageInfoActivity.class);
                intentInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentInfo);
                break;

            case R.id.logout:

                //make de checkBox NOT CHECKED!
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(LoginPage.DEFAULT_CONNECTED, false);
                editor.commit();

                Intent logout = new Intent(this, LoginPage.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
                finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //Buttons to get the date to travel
    public void showArrivalDatePickerDialog(View v){
        setArriveDate();
    }

    public void showExitDatePickerDialog(View v){
        setExitDate();
    }

    public void showSpendDatePickerDialog(View v){
        setSpendingDate();
    }


    public void saveTrip(View v){

        SQLiteDatabase db = helper.getWritableDatabase();
        voyage = new Voyage();

        int type = voyageFragment.radioGroup.getCheckedRadioButtonId();
        if(type == R.id.vacationBtn){
            voyage.setTypeVoyage(getString(R.string.vacation));
        }else{
            voyage.setTypeVoyage(getString(R.string.business));
        }

        voyage.setDestiny(voyageFragment.destination.getText().toString());
        voyage.setBudget(Double.parseDouble(voyageFragment.budget.getText().toString()));
        voyage.setNumberPeoples(Integer.parseInt(voyageFragment.numberPerson.getText().toString()));
        voyage.setArrivalDate(voyageFragment.arrivalBtn.getText().toString());
        voyage.setExitDate(voyageFragment.exitBtn.getText().toString());
        voyage.setUser_id(currentUserId);

        try {
            ContentValues values = new ContentValues();
            values.put("destiny", voyage.getDestiny());
            values.put("type_voyage", voyage.getTypeVoyage());
            values.put("arrive_date", voyage.getArrivalDate());
            values.put("exit_date", voyage.getExitDate());
            values.put("budget", voyage.getBudget());
            values.put("number_peoples", voyage.getNumberPeoples());
            values.put("user_id", currentUserId);

            long result = db.insert("voyage", null, values);
            if (result != -1) {
                Toast.makeText(MenuVoyageActivity.this, "Voyage was inserted successfully!", Toast.LENGTH_SHORT).show();
                Log.i("INSERT DATABASE", "SUCCESSFULLY");
            }

            //Save voyage info to retrieve before.
            saveVoyageInSharedPreferences(currentUserId);

        }catch (SQLiteException e){
            Log.e("SOMETHING WRONG", "NOT-SUCCESSFULLY " + e.getMessage());
        }

        Log.e("Test database: "," " + voyage.getDestiny() + " : " + voyage.getBudget() + " : " + voyage.getNumberPeoples()
                                + " : " + voyage.getTypeVoyage() + " : " + voyage.getArrivalDate()
                                + " : " + voyage.getExitDate() );

        finish();
        startActivity(getIntent());
    }



    public void saveVoyageInSharedPreferences(int userId){

        SharedPreferences preferences = getSharedPreferences(VOYAGE_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("destiny", helper.getVoyageInfo(userId).getDestiny());
        editor.putString("type_voyage", helper.getVoyageInfo(userId).getTypeVoyage());
        editor.putString("arrive_date", helper.getVoyageInfo(userId).getArrivalDate());
        editor.putString("exit_date", helper.getVoyageInfo(userId).getExitDate());
        editor.putInt("budget", helper.getVoyageInfo(userId).getBudget().intValue());
        editor.putInt("number_peoples", helper.getVoyageInfo(userId).getNumberPeoples());
        editor.putInt("user_id", helper.getVoyageInfo(userId).getUser_id());
        editor.putInt("current_voyage", helper.getVoyageInfo(userId).getId());

        editor.apply();

        Log.d("SharedPreferences", "** VOYAGE - Saved in Preferences**" + helper.getVoyageInfo(userId).getId());
    }






    // ************************************************ IMPLEMENTING OF SPENDING FRAGMENT ****************************************************

    public void saveSpending(View v){

        voyageListActivity = new VoyageListActivity();

        if(checkEmptyData()){

            saveSpendingsToObjectSpending();
            SQLiteDatabase db = helper.getWritableDatabase();

            if(isFromVoyageList()){
                insertInSelectedVoyage(spending.getCategory(), spending.getDate(), spending.getValue(), spending.getDescription(), spending.getPlace(), retrieveSelectedVoyageID());
                Log.i("Comming from VoyageList", spending.getCategory() + spending.getDate() + spending.getValue() + spending.getDescription() + spending.getPlace() + retrieveSelectedVoyageID());
                finish();

            }else{
                ContentValues values = new ContentValues();
                values.put("category", spending.getCategory());
                values.put("date", spending.getDate());
                values.put("place", spending.getPlace());
                values.put("description", spending.getDescription());
                values.put("value", spending.getValue());
                values.put("voyage_id", spending.getVoyageId());

                long result = db.insert("spending", null, values);
                if (result != -1) {
                    Toast.makeText(MenuVoyageActivity.this, "Spending was inserted successfully!", Toast.LENGTH_SHORT).show();
                    Log.i("DATABASE", "SPENDING INSERT SUCCESSFULLY");
                }
            }

        }else{
            Toast.makeText(getApplicationContext(), R.string.no_trip_msg, Toast.LENGTH_LONG).show();
            finish();
        }

        finish();
        startActivity(getIntent());
    }


    public boolean checkEmptyData(){
        // Get the actual id..
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id FROM voyage WHERE user_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(currentUserId)}); // null can be replaced by WHERE argument..
        boolean data = cursor.moveToLast();    // To get the last Voyage table register

        Log.d("checkEmptyData"," Value: " + data);
        return data;

    }

    public void saveSpendingsToObjectSpending(){

        // Get the actual id..
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT _id FROM voyage WHERE user_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(currentUserId)});
        cursor.moveToLast();
        int voyageId = cursor.getInt(0);
        Log.d("TripId from spending", "tripId: " + voyageId);

        try {
            spending = new Spending();

            if(spendingFragment.value.getText().toString().equals("")){
                spending.setValue(0d); //Double
            }else{
                spending.setValue(Double.parseDouble(spendingFragment.value.getText().toString()));
            }

            spending.setDescription(spendingFragment.description.getText().toString());
            spending.setPlace(spendingFragment.place.getText().toString());
            spending.setDate(spendingFragment.dateSpending.getText().toString());
            spending.setCategory(spendingFragment.spinner.getSelectedItem().toString());
            spending.setVoyageId(voyageId);

            Log.d("Saved on Spending", "values: " + spending.getCategory() + " - " + spending.getDate() + " - " +
                    spending.getValue() + " - " + spending.getDescription() + " - " + spending.getPlace() + " actualID: " + spending.getVoyageId());


        }catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Spending was not saved: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Spending not saved: ", ">>: " + e.getMessage());

        }

    }

    // Get from Bundle Extra the selected trip _id..
    public boolean isFromVoyageList(){

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            boolean result = b.getBoolean("isFromVoyageList");
            Log.d("isFromVoyageList", "Bundle Extra: " + result);
            return result;
        }else {
            Log.d("isFromVoyageList", "Bundle Extra: " + "FALSE");
            return false;
        }
    }

    // Used to save spendings in the selected trip from TripList Activity..
    public void insertInSelectedVoyage(String category, String date, double value, String description, String place, int itemId){


        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("category", category);
        values.put("date", date);
        values.put("value", value);
        values.put("description", description);
        values.put("place", place);
        values.put("trip_id", itemId); // maybe it needs +1 because the first item is 0

        long result = db.insert("spending", null, values);

        if (result != -1) {
            Toast.makeText(this, "Register saved successfully..", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Register NOT saved! ", Toast.LENGTH_SHORT).show();

        }

    }

    // Get from Bundle Extra the selected trip _id..
    public int retrieveSelectedVoyageID(){

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            int result = b.getInt("CameFromVoyageList");
            Log.d("DestinationID", "Bundle Extra: " + result);
            return result;
        }else {
            return 0;
        }
    }



}
