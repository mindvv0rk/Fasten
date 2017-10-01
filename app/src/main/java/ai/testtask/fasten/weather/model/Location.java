package ai.testtask.fasten.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Location {

    @SerializedName("city")
    @Expose
    private String mCityName;

    @SerializedName("country_name")
    @Expose
    private String mCountryName;


    public String getCityName() {
        return mCityName;
    }

    public String getCountryName() {
        return mCountryName;
    }
}
