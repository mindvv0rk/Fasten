package ai.testtask.fasten.weather.model.response.weather;

import com.google.gson.annotations.SerializedName;

import ai.testtask.fasten.weather.model.Location;

public final class WeatherResponse {

    @SerializedName("location")
    private Location mLocation;

    @SerializedName("forecast")
    private Forecast mForecast;

    public Forecast getForecast() {
        return mForecast;
    }

    public Location getLocation() {

        return mLocation;
    }
}
