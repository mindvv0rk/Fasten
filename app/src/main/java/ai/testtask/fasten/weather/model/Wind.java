package ai.testtask.fasten.weather.model;

import com.google.gson.annotations.SerializedName;

public final class Wind {

    @SerializedName("mph")
    private int mSpeedInMph;

    @SerializedName("kph")
    private int mSpeedInKph;

    @SerializedName("dir")
    private String mDirection;

    public int getSpeedInMph() {
        return mSpeedInMph;
    }

    public int getSpeedInKph() {
        return mSpeedInKph;
    }

    public String getDirection() {
        return mDirection;
    }
}
