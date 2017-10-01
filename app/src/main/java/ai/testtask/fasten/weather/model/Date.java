package ai.testtask.fasten.weather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Date {

    @SerializedName("day")
    @Expose
    private int mDay;

    @SerializedName("monthname")
    @Expose
    private String mMonthName;

    @SerializedName("weekday_short")
    @Expose
    private String mWeekDayShort;

    @SerializedName("weekday")
    @Expose
    private String mWeekDay;


    public int getDay() {
        return mDay;
    }

    public String getMonthName() {
        return mMonthName;
    }

    public String getWeekDayShort() {
        return mWeekDayShort;
    }

    public String getWeekDay() {
        return mWeekDay;
    }
}
