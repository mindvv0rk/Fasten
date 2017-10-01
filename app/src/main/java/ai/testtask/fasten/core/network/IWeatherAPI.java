package ai.testtask.fasten.core.network;

import ai.testtask.fasten.weather.model.response.weather.WeatherResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface IWeatherAPI {

    @GET("geolookup/forecast10day/q/{location}")
    Observable<WeatherResponse> getForecastFor10DaysByLocation(@Path("location") String location);
}
