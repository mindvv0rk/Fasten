package ai.testtask.fasten.core.network;

import android.database.Observable;

import retrofit2.http.GET;

public interface IServerAPI {

    @GET("http://api.wunderground.com/api/6058ac702f7bdd2e/forecast10day/q/37.776289,-122.395234.json")
    Observable<?>  getForecastFor10DaysByLocation();
}
