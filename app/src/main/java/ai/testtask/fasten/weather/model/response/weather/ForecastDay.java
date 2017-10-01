package ai.testtask.fasten.weather.model.response.weather;

import com.google.gson.annotations.SerializedName;

import ai.testtask.fasten.weather.model.Date;
import ai.testtask.fasten.weather.model.Temperature;
import ai.testtask.fasten.weather.model.Wind;

public final class ForecastDay {

    @SerializedName("date")
    private Date mDate;

    @SerializedName("high")
    private Temperature mHigh;

    @SerializedName("low")
    private Temperature mLow;

    @SerializedName("icon_url")
    private String mIconUrl;

    @SerializedName("avewind")
    private Wind mWind;

    @SerializedName("avehumidity")
    private int mAverageHumidity;

    public Date getDate() {
        return mDate;
    }

    public Temperature getHigh() {
        return mHigh;
    }

    public Temperature getLow() {
        return mLow;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public Wind getWind() {
        return mWind;
    }

    public int getAverageHumidity() {
        return mAverageHumidity;
    }
}
