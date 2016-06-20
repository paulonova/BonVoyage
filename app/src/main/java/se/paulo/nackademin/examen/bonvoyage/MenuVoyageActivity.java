package se.paulo.nackademin.examen.bonvoyage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import bonvoyage.objects.Spending;
import bonvoyage.objects.Voyage;

public class MenuVoyageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    Voyage voyage;

    //All Fragments..
    NewVoyageFragment voyageFragment = null;
    ShowVoyagesFragment showVoyagesFragment = null;
    SpendingFragment spendingFragment = null;

    //From nav_header_menu.xml ==> DrawerView viewGroups..
    TextView userName, userEmail;
    SharedPreferences myPreferences;

    NavigationView navHeadView;

    String destiny;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


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

        Log.i("HEADER INFO","" + userName.getText().toString());
        Log.i("HEADER INFO","" + userEmail.getText().toString());

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

        switch (id){
            case R.id.action_settings:
                Toast.makeText(MenuVoyageActivity.this, "Settings was selected!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_info:
                Intent intent = new Intent(this, BonVoyageInfoActivity.class);
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

                showVoyagesFragment = new ShowVoyagesFragment();
                android.support.v4.app.FragmentTransaction fragmentVoyageTransaction = getSupportFragmentManager().beginTransaction();
                fragmentVoyageTransaction.replace(R.id.fragment_menu_container, showVoyagesFragment);
                fragmentVoyageTransaction.commit();
                break;

            case R.id.settings:

                break;

            case R.id.nav_share:

                break;

            case R.id.nav_info:
                Intent intentInfo = new Intent(this, BonVoyageInfoActivity.class);
                intentInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentInfo);
                break;

            case R.id.logout:
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
        voyage = new Voyage();

        int type = voyageFragment.radioGroup.getCheckedRadioButtonId();
        if(type == R.id.vacationBtn){
            voyage.setTypeTrip(getString(R.string.vacation));
        }else{
            voyage.setTypeTrip(getString(R.string.business));
        }

        voyage.setDestiny(voyageFragment.destination.getText().toString());
        voyage.setBudget(Double.parseDouble(voyageFragment.budget.getText().toString()));
        voyage.setNumberPeoples(Integer.parseInt(voyageFragment.numberPerson.getText().toString()));
        voyage.setArrivalDate(voyageFragment.arrivalBtn.getText().toString());
        voyage.setExitDate(voyageFragment.exitBtn.getText().toString());

        Log.e("Test Text: "," " + voyage.getDestiny() + " : " + voyage.getBudget() + " : " + voyage.getNumberPeoples()
                                + " : " + voyage.getTypeTrip() + " : " + voyage.getArrivalDate()
                                + " : " + voyage.getExitDate() );
        Toast.makeText(getApplicationContext(), "TESTANDO..." + voyage.getDestiny() + " : " + voyage.getBudget() + " : " + voyage.getNumberPeoples() , Toast.LENGTH_SHORT).show();
    }



    // ************************************************ IMPLEMENTING OF SPENDING FRAGMENT ****************************************************

    public void saveSpending(View v){
        Spending spend = new Spending();

        spend.setValue(Double.parseDouble(spendingFragment.value.getText().toString()));
        spend.setDescription(spendingFragment.description.getText().toString());
        spend.setPlace(spendingFragment.place.getText().toString());
        spend.setDate(spendingFragment.dateSpending.getText().toString());
        spend.setCategory(spendingFragment.spinner.getSelectedItem().toString());
        Toast.makeText(getApplicationContext(), "TESTANDO..."   + spend.getValue() + " - " + spend.getDescription()
                                                                + " - " + spend.getPlace() + " - " + spend.getDate()
                                                                + " - " + spend.getCategory(), Toast.LENGTH_SHORT).show();
    }


}
