package se.paulo.nackademin.examen.bonvoyage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SpendingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner categories;
    List<String> categoryList;

    SpendingFragment spendingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categories = (Spinner)findViewById(R.id.spinner_category);
        //categories.setPrompt("Select a category");
        categories.setOnItemSelectedListener(this);

        //TESTING..
        spendingFragment = new SpendingFragment();



        categoryList = new ArrayList<>();
        categoryList.add("Food");
        categoryList.add("Fuel");
        categoryList.add("Transportation");
        categoryList.add("Accommodation");
        categoryList.add("Others");

        ArrayAdapter<String> spinnAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        spinnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(spinnAdapter);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing..
    }
}
