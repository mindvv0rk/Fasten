package ai.testtask.fasten.weather.current;

import ai.testtask.fasten.core.IView;
import ai.testtask.fasten.weather.model.weather.Weather;


public interface ICurrentWeatherView extends IView {

    void showLoading();
    void showError(String message);
    void showWeather(Weather weather);
}
