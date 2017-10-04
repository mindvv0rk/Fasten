package ai.testtask.fasten.weather.model.response.autocomplete;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ai.testtask.fasten.weather.model.City;

public final class AutocompleteResponse {

    @SerializedName("RESULTS")
    private List<City> mCities;

    public List<City> getCities() {
        return mCities;
    }
}
