package ai.testtask.fasten.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Temperature {

    @SerializedName("fahrenheit")
    private String mFahrenheit;

    @SerializedName("celsius")
    private String mCelsius;

    public String getFahrenheit() {
        return mFahrenheit;
    }

    public String getCelsius() {
        return mCelsius;
    }
}
