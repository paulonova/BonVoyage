package se.paulo.nackademin.examen.bonvoyage;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import bonvoyage.objects.Voyage;

public class MenuVoyageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    Voyage voyage;

    NewVoyageFragment voyageFragment = null;
    ShowVoyagesFragment showVoyagesFragment = null;

    //EditText destination, budget, numberPerson;
    Button saveTrip;
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


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //From NewVoyageFragment there I will save trip register..
        //voyageFragment.destination = (EditText)findViewById(R.id.destination);
        saveTrip = (Button)findViewById(R.id.save_trip);


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
                SpendingFragment spendingFragment = new SpendingFragment();
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

    public void saveTrip(View v){
        voyage = new Voyage();
        voyage.setDestiny(voyageFragment.destination.getText().toString());
        Log.e("Test Text: "," " + voyage.getDestiny());
        Toast.makeText(getApplicationContext(), "TESTANDO..." + voyage.getDestiny() , Toast.LENGTH_SHORT).show();
    }
}
