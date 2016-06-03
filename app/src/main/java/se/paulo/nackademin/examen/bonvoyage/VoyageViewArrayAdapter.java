package se.paulo.nackademin.examen.bonvoyage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import bonvoyage.objects.Voyage;

/**
 * Adapter used to show all information about a TRIP in ListView
 */


public class VoyageViewArrayAdapter extends ArrayAdapter<Voyage> {

    Context context;
    List<Voyage> voyageList;

    public VoyageViewArrayAdapter(Context context, int resource, List<Voyage> objects) {
        super(context, resource, objects);
        this.context = context;
        voyageList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Skapa den View som visas för varje item i en ListView
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View itemView = convertView;



        return itemView;
    }
}
