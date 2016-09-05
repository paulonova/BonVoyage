package bonvoyage.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import bonvoyage.weather.WeatherActivity;
import bonvoyage.weather.data.Channel;
import bonvoyage.weather.data.Item;
import bonvoyage.weather.service.GoogleMapsGeocodingService;
import bonvoyage.weather.service.WeatherCacheService;
import bonvoyage.weather.service.WeatherServiceCallback;
import bonvoyage.weather.service.YahooWeatherService;
import se.paulo.nackademin.examen.bonvoyage.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstAppInfoFragment extends Fragment {





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

        View rootView = inflater.inflate(R.layout.fragment_app_info, container, false);
        // Inflate the layout for this fragment



        return rootView;
    }



}
