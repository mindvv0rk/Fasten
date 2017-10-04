package ai.testtask.fasten.weather.search;

import java.util.List;

import ai.testtask.fasten.core.IView;
import ai.testtask.fasten.weather.model.weather.Weather;

public interface ISearchWeatherView extends IView {

    void showSuggestionsLoading();
    void showSuggestions(List<String> cities);
    void showSuggestionsError(String msg);

    void showLoading();
    void showError(String msg);
    void showWeather(Weather weather);
}
