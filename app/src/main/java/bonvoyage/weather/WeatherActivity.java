package bonvoyage.weather;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import se.paulo.nackademin.examen.bonvoyage.R;

public class WeatherActivity extends AppCompatActivity {

    /*https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json*/

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private Switch btnSwitchFahrenheit;
    private EditText edtxWeatherLocation;
    private ImageButton btnImageSendRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);

        btnSwitchFahrenheit = (Switch)findViewById(R.id.switchUnit);
        edtxWeatherLocation = (EditText)findViewById(R.id.edtxLocation);
        btnImageSendRequest = (ImageButton)findViewById(R.id.btnSendRequest);

    }




}
