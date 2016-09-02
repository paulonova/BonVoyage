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
import bonvoyage.weather.service.WeatherServiceCallback;
import bonvoyage.weather.service.YahooWeatherService;
import se.paulo.nackademin.examen.bonvoyage.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstAppInfoFragment extends Fragment implements WeatherServiceCallback {

    private FragmentTabHost mTabHost;
    /*https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json*/

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private Switch btnSwitchFahrenheit;
    private EditText edtxWeatherLocation;
    private ImageButton btnImageSendRequest;

    WeatherActivity weatherActivity;

    private YahooWeatherService service;
    private ProgressDialog dialog;

    private String weatherLocation;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherActivity = new WeatherActivity();

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading..");
        dialog.show();

        //Here I can change the location..
        service.refreshWeather("Stockholm, SW");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_weather, container, false);
        // Inflate the layout for this fragment

        weatherIconImageView = (ImageView) rootView.findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) rootView.findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) rootView.findViewById(R.id.conditionTextView);
        locationTextView = (TextView) rootView.findViewById(R.id.locationTextView);

        btnSwitchFahrenheit = (Switch)rootView.findViewById(R.id.switchUnit);
        edtxWeatherLocation = (EditText)rootView.findViewById(R.id.edtxLocation);
        btnImageSendRequest = (ImageButton)rootView.findViewById(R.id.btnSendRequest);

        return rootView;
    }


    @Override
    public void serviceSuccess(Channel channel) {

        dialog.hide();

        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getContext().getPackageName());  // Changing weather icon..

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());  // degree simbol = \u00B0 °
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(service.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
