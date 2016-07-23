package bonvoyage.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import se.paulo.nackademin.examen.bonvoyage.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstAppInfoFragment extends Fragment implements TabHost.OnTabChangeListener {

    private FragmentTabHost mTabHost;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_app_info, container, false);
    }




    @Override
    public void onTabChanged(String tabId) {

    }
}
