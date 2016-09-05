package bonvoyage.weather.listener;


import bonvoyage.weather.data.Channel;


public interface WeatherServiceListener {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
