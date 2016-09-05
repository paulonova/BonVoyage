package bonvoyage.weather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bonvoyage.weather.data.Channel;
import bonvoyage.weather.data.Item;
import bonvoyage.weather.listener.WeatherServiceListener;
import bonvoyage.weather.service.YahooWeatherService;
import se.paulo.nackademin.examen.bonvoyage.R;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceListener {


    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private EditText edtxWeatherLocation;
    private ImageButton btnImageSendRequest;

    WeatherActivity weatherActivity;

    private YahooWeatherService service;
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Lock screen in ORIENTATION_PORTRAIT / don´t crash the Fragments..
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //hiding the soft-keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Connecting to MenuVoyageActivity!
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        edtxWeatherLocation = (EditText)findViewById(R.id.edtxLocation);

        btnImageSendRequest = (ImageButton)findViewById(R.id.btnSendRequest);
        btnImageSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.refreshWeather(edtxWeatherLocation.getText().toString());
            }
        });


        weatherActivity = new WeatherActivity();

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.show();

        //Here I can change the location..
        service.refreshWeather("Stockholm");


    }



    @Override
    public void serviceSuccess(Channel channel) {

        dialog.hide();

        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());  // Changing weather icon..

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
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
