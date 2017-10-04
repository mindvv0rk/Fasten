package ai.testtask.fasten.core.network;


import ai.testtask.fasten.weather.model.response.autocomplete.AutocompleteResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface IAutocompleteAPI {

    @GET("aq")
    Observable<AutocompleteResponse> queryCityList(@Query("query") String partOfCityName);
}
