package ai.testtask.fasten.weather.model.weather;

import java.util.List;

import ai.testtask.fasten.weather.model.Location;
import ai.testtask.fasten.weather.model.response.weather.ForecastDay;

public final class Weather {

    private final Location mLocation;

    private final List<ForecastDay> mForecastDay;

    public Weather(Location location, List<ForecastDay> forecastDay) {
        mLocation = location;
        mForecastDay = forecastDay;
    }

    public Location getLocation() {
        return mLocation;
    }

    public List<ForecastDay> getForecastDay() {
        return mForecastDay;
    }
}
