package bonvoyage.weather.listener;

import bonvoyage.weather.data.LocationResult;

/**
 * Created by Paulo Vila Nova on 2016-09-09.
 */
public interface GeocodingServiceListener {

    void geocodeSuccess(LocationResult location);

    void geocodeFailure(Exception exception);
}
