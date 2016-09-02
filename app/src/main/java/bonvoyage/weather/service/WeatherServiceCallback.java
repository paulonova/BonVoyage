package bonvoyage.weather.service;


import bonvoyage.weather.data.Channel;


public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
