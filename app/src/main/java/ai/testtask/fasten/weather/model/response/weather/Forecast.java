package ai.testtask.fasten.weather.model.response.weather;

import com.google.gson.annotations.SerializedName;

public final class Forecast {

    @SerializedName("simpleforecast")
    private SimpleForecast mSimpleForecast;

    public SimpleForecast getSimpleForecast() {
        return mSimpleForecast;
    }
}
