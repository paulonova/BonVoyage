package se.paulo.nackademin.examen.bonvoyage;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpendingFragment extends Fragment {


    public SpendingFragment() {
        // Required empty public constructor
    }

    Spinner spinner;
    List<String> categoryList;
    EditText value, description, place;
    Button saveSpending, dateSpending;

    private int actualYear, actualMonth, actualDay;
    private int selectedYear, selectedMonth, selectedDay;
    private String selectedDate;
    private String actualDate;

    private AlertDialog alert;


    Calendar spendCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dSpend = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            spendCalendar.set(Calendar.YEAR, year);
            spendCalendar.set(Calendar.MONTH, monthOfYear);
            spendCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();

        }
    };

    public void updateDate() {

        selectedYear = spendCalendar.get(Calendar.YEAR);
        selectedMonth = spendCalendar.get(Calendar.MONTH);
        selectedDay = spendCalendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;

        dateSpending.setText(selectedDate);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_spending, container, false);

        spinner = (Spinner)rootView.findViewById(R.id.spinner_category);
        categoryList = new ArrayList<>();
        categoryList.add("Food");
        categoryList.add("Fuel");
        categoryList.add("Transportation");
        categoryList.add("Accommodation");
        categoryList.add("Others");

        ArrayAdapter<String> spinnAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryList);
        spinnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnAdapter);

        value = (EditText)rootView.findViewById(R.id.edtValue);
        description = (EditText)rootView.findViewById(R.id.edtDescription);
        place = (EditText)rootView.findViewById(R.id.edtPlace);

        dateSpending = (Button)rootView.findViewById(R.id.spendingDate);
        updateDate();
        saveSpending = (Button)rootView.findViewById(R.id.spending);

        actualYear = spendCalendar.get(Calendar.YEAR);
        actualMonth = spendCalendar.get(Calendar.MONTH);
        actualDay = spendCalendar.get(Calendar.DAY_OF_MONTH);
        actualDate = actualYear + "/" + (actualMonth + 1) + "/" + actualDay;
        selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;

        // Inflate the layout for this fragment
        return rootView;
    }




    //Getters and Setters

    public int getActualYear() {
        return actualYear;
    }

    public void setActualYear(int actualYear) {
        this.actualYear = actualYear;
    }

    public int getActualMonth() {
        return actualMonth;
    }

    public void setActualMonth(int actualMonth) {
        this.actualMonth = actualMonth;
    }

    public int getActualDay() {
        return actualDay;
    }

    public void setActualDay(int actualDay) {
        this.actualDay = actualDay;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public int getSelectedDay() {
        return selectedDay;
    }

    public void setSelectedDay(int selectedDay) {
        this.selectedDay = selectedDay;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }
}
