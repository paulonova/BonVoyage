package bonvoyage.weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bonvoyage.preferences.SettingsActivity;
import bonvoyage.weather.data.Channel;
import bonvoyage.weather.data.Condition;
import bonvoyage.weather.data.Item;
import bonvoyage.weather.data.LocationResult;
import bonvoyage.weather.listener.GeocodingServiceListener;
import bonvoyage.weather.listener.WeatherServiceListener;
import bonvoyage.weather.service.GoogleMapsGeocodingService;
import bonvoyage.weather.service.WeatherCacheService;
import bonvoyage.weather.service.YahooWeatherService;
import se.paulo.nackademin.examen.bonvoyage.BonVoyageInfoActivity;
import se.paulo.nackademin.examen.bonvoyage.R;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceListener, GeocodingServiceListener, LocationListener {


    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private EditText edtxWeatherLocation;
    private ImageButton btnImageSendRequest;

    private GoogleMapsGeocodingService geocodingService;
    private WeatherCacheService cacheService;
    private YahooWeatherService weatherService;

    private ProgressDialog dialog;

    // weather weatherService fail flag
    private boolean weatherServicesHasFailed = false;
    private SharedPreferences preferences = null;



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
                weatherService.refreshWeather(edtxWeatherLocation.getText().toString());
            }
        });


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        weatherService = new YahooWeatherService(this);
        weatherService.setTemperatureUnit(preferences.getString(getString(R.string.pref_temperature_unit), null));
        geocodingService = new GoogleMapsGeocodingService(this);
        cacheService = new WeatherCacheService(this);


        //weatherService = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();

        String location = null;

            if(preferences.getBoolean(getString(R.string.pref_geolocation_enabled), true)){
                String locationCache = preferences.getString(getString(R.string.pref_cached_location), null);

                if(locationCache == null){
                    getWeatherFromCurrentLocation();
                }else{
                    location = locationCache;
                }

            }else{
                location = preferences.getString(getString(R.string.pref_manual_location), null);
            }


            if(location != null) {
                weatherService.refreshWeather(location);
            }

    }

    private void getWeatherFromCurrentLocation() {
        // system's LocationManager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // medium accuracy for weather, good for 100 - 500 meters
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM);

        String provider = locationManager.getBestProvider(locationCriteria, true);

        // single location update
        locationManager.requestSingleUpdate(provider, this, null);
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //To sett Settings menu in WeatherActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;

        switch (id){

            case R.id.currentLocation:
                dialog.show();
                getWeatherFromCurrentLocation();
                return true;

            case R.id.weather_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }



    @Override
    public void serviceSuccess(Channel channel) {

        dialog.hide();

        Condition condition = channel.getItem().getCondition();

        int resourceId = getResources().getIdentifier("drawable/icon_" + condition.getCode(), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);

        String temperatureLabel = getString(R.string.temperature_output, condition.getTemperature(), channel.getUnits().getTemperature());

        Log.d("TEMPERATURE","" + temperatureLabel);

        temperatureTextView.setText(temperatureLabel);
        conditionTextView.setText(condition.getDescription());
        locationTextView.setText(channel.getLocation());

    }



    @Override
    public void serviceFailure(Exception exception) {
        // display error if this is the second failure
        if (weatherServicesHasFailed) {
            dialog.hide();
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            // error doing reverse geocoding, load weather data from cache
            weatherServicesHasFailed = true;
            // OPTIONAL: let the user know an error has occurred then fallback to the cached data
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();

            cacheService.load(this);
        }
    }


    @Override
    public void geocodeSuccess(LocationResult location) {
        // completed geocoding successfully
        weatherService.refreshWeather(location.getAddress());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.pref_cached_location), location.getAddress());
        editor.apply();

    }

    @Override
    public void geocodeFailure(Exception exception) {
        // GeoCoding failed, try loading weather data from the cache
        cacheService.load(this);

    }

    @Override
    public void onLocationChanged(Location location) {
        geocodingService.refreshLocation(location);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
