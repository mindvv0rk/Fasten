package ai.testtask.fasten.core.network;

import android.database.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IWeatherAPI {

    @GET("/forecast10day/q/{location}")
    Observable<?>  getForecastFor10DaysByLocation(@Path("location") String location);
}
