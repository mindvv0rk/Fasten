package ai.testtask.fasten.core.network;

import android.database.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IAutocompleteAPI {

    @GET("/aq")
    Observable<?> queryCityList(@Query("query") String partOfCityName);
}
