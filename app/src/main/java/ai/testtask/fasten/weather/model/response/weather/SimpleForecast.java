package ai.testtask.fasten.weather.model.response.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class SimpleForecast {

    @SerializedName("forecastday")
    private List<ForecastDay> mForecastDayList;

    public List<ForecastDay> getForecastDayList() {
        return mForecastDayList;
    }
}
