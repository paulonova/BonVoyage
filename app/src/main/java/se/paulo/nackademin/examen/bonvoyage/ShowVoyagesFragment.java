package se.paulo.nackademin.examen.bonvoyage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import bonvoyage.objects.Voyage;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowVoyagesFragment extends Fragment {

    VoyageViewArrayAdapter voyageViewArrayAdapter;
    List<Voyage> voyageArray;


    public ShowVoyagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_show_voyages, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.voyageListView);
        voyageViewArrayAdapter = new VoyageViewArrayAdapter(getContext(), android.R.layout.simple_list_item_1, null );

        // Inflate the layout for this fragment
        return rootView;
    }

}
