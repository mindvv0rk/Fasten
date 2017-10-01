package ai.testtask.fasten.weather.model;

import com.google.gson.annotations.SerializedName;

public final class City {

    @SerializedName("name")
    private String mName;

    @SerializedName("lat")
    private String mLatitude;

    @SerializedName("lon")
    private String mLongitude;

    public String getName() {
        return mName;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public String getLongitude() {
        return mLongitude;
    }
}
