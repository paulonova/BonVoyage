package se.paulo.nackademin.examen.bonvoyage;


import android.os.Bundle;
//import android.app.ListFragment;

import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;



public class VoyageListFragment extends ListFragment implements OnItemClickListener  {

    String[] contactList = {"Person1","Person2","Person3","Person4","Person5","Person6"};


    public VoyageListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_voyage_list, container, false);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //This adapter uses to set an ArrayList into ListView.
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, contactList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

}
