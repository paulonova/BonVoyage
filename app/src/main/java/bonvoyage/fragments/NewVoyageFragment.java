package bonvoyage.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Calendar;

import bonvoyage.database.DatabaseHelper;
import bonvoyage.objects.Voyage;
import se.paulo.nackademin.examen.bonvoyage.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewVoyageFragment extends Fragment {

    //For the Arrival and Exit date..
//    private int year;
//    private int month;
//    private int day;

    public Button arrivalBtn;
    public Button exitBtn;
    private Button saveBtn;

//    RadioGroup typeTrip;
    public RadioGroup radioGroup;
//    private String typeTripText;

    //Database saving..
    private DatabaseHelper helper;

    public EditText destination;
    public EditText budget;
    public EditText numberPerson;
//    private Voyage voyage;
//
//    private int id_Actual;
//
//    private String compareDestiny;
//    private String selectedDestiny;
//    private String concatDestiny;

    //Empty Constructor
    public NewVoyageFragment() {
        // Required empty public constructor
    }

    //Calendar
    public Calendar arrivalCalendar = Calendar.getInstance();
    public Calendar exitCalendar = Calendar.getInstance();

    public DatePickerDialog.OnDateSetListener dArrival = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            arrivalCalendar.set(Calendar.YEAR, year);
            arrivalCalendar.set(Calendar.MONTH, monthOfYear);
            arrivalCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateArrivalDate();

        }
    };


    public DatePickerDialog.OnDateSetListener dExit = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            exitCalendar.set(Calendar.YEAR, year);
            exitCalendar.set(Calendar.MONTH, monthOfYear);
            exitCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateExitDate();

        }
    };

    public void updateArrivalDate() {

        int year = arrivalCalendar.get(Calendar.YEAR);
        int month = arrivalCalendar.get(Calendar.MONTH);
        int day = arrivalCalendar.get(Calendar.DAY_OF_MONTH);
        String arrivalDate = year + "/" + (month + 1) + "/" + day;

        arrivalBtn.setText(arrivalDate);
    }

    public void updateExitDate() {
        int year = exitCalendar.get(Calendar.YEAR);
        int month = exitCalendar.get(Calendar.MONTH);
        int day = exitCalendar.get(Calendar.DAY_OF_MONTH);
        String exitDate = year + "/" + (month + 1) + "/" + day;

        exitBtn.setText(exitDate);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_voyage, container, false);


        arrivalBtn = (Button)rootView.findViewById(R.id.arrivalBtn);
        exitBtn = (Button)rootView.findViewById(R.id.exitBtn);
        updateArrivalDate();
        updateExitDate();

        destination = (EditText)rootView.findViewById(R.id.destination);
        budget = (EditText)rootView.findViewById(R.id.budget);
        numberPerson = (EditText)rootView.findViewById(R.id.number_people);

        radioGroup = (RadioGroup)rootView.findViewById(R.id.rg_typeTrip);
        saveBtn = (Button)rootView.findViewById(R.id.save_trip);

        // Prepare access to database..
        helper = new DatabaseHelper(getContext());

        // Inflate the layout for this fragment
        return rootView;
    }


}
