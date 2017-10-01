package ai.testtask.fasten.weather.search;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ai.testtask.fasten.core.AbstractPresenter;
import ai.testtask.fasten.core.di.Dagger;
import ai.testtask.fasten.core.network.IAutocompleteAPI;
import ai.testtask.fasten.core.network.IWeatherAPI;
import ai.testtask.fasten.core.network.NetworkModuleFactory;
import ai.testtask.fasten.weather.current.ICurrentWeatherView;
import ai.testtask.fasten.weather.model.City;
import ai.testtask.fasten.weather.model.response.autocomplete.AutocompleteResponse;
import ai.testtask.fasten.weather.model.response.weather.WeatherResponse;
import ai.testtask.fasten.weather.model.weather.Weather;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class SearchWeatherPresenter extends AbstractPresenter<ISearchWeatherView> {

    private static final String TAG = SearchWeatherPresenter.class.getSimpleName();

    @Inject
    IWeatherAPI mWeatherApi;

    @Inject
    IAutocompleteAPI mAutocompleteApi;

    private List<City> mLastCities;
    private Weather mLastWeather;
    private City mLastCity;
    private List<String> mNames;

    private Subscription mWeatherSubscription;
    private Subscription mSuggestionsSubscription;


    public SearchWeatherPresenter() {
        Dagger.getAppComponent().inject(this);
    }

    @Override
    protected void onViewAttached(@NonNull ISearchWeatherView view) {
        super.onViewAttached(view);

        if (mLastCities != null && mNames != null) {
            showSuggestions(mNames);
        }

        if (mLastWeather != null) {
            showWeather(mLastWeather);
        }
    }

    public void searchQuery(String query) {
        showSuggestionsLoading();

        if (mSuggestionsSubscription != null && !mSuggestionsSubscription.isUnsubscribed()) {
            mSuggestionsSubscription.unsubscribe();
        }
        mSuggestionsSubscription = mAutocompleteApi
                .queryCityList(query)
                .subscribeOn(Schedulers.io())
                .map(new Func1<AutocompleteResponse, List<String>>() {
                    @Override
                    public List<String> call(AutocompleteResponse autocompleteResponse) {
                        mLastCities = new ArrayList<City>(autocompleteResponse.getCities());
                        List<String> names = new ArrayList<String>(autocompleteResponse.getCities().size());
                        for (City city : autocompleteResponse.getCities()) {
                            names.add(city.getName());
                        }
                        return names;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<String>>() {
                            @Override
                            public void call(List<String> response) {
                                mNames = response;
                                showSuggestions(response);
                                mSuggestionsSubscription.unsubscribe();
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Error while executing request for search suggestions.", throwable);
                                showSuggestionsError("Error while executing request for search suggestions.");
                                mSuggestionsSubscription.unsubscribe();
                            }
                        });
    }

    public void searchWeatherForCity(int cityPosition) {
        City city = mLastCities.get(cityPosition);
        mLastCity = city;
        getForecast(Double.parseDouble(city.getLatitude()), Double.parseDouble(city.getLongitude()));
    }

    public void retry() {
        if (mLastCity != null) {
            getForecast(Double.parseDouble(mLastCity.getLatitude()), Double.parseDouble(mLastCity.getLongitude()));
        } else {
            showError("Please select city from suggestions");
        }
    }


    private void getForecast(double  latitude, double longitude) {
        showLoading();
        String param = String.valueOf(latitude)
                .concat(",")
                .concat(String.valueOf(longitude))
                .concat(NetworkModuleFactory.WEATHER_REQUEST_FORMAT);

        mWeatherSubscription = mWeatherApi
                .getForecastFor10DaysByLocation(param)
                .subscribeOn(Schedulers.io())
                .map(new Func1<WeatherResponse, Weather>() {
                    @Override
                    public Weather call(WeatherResponse weatherResponse) {
                        return new Weather(weatherResponse.getLocation(),
                                weatherResponse.getForecast().getSimpleForecast().getForecastDayList());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<Weather>() {
                            @Override
                            public void call(Weather weather) {
                                Log.i(TAG, "Weather data by location loaded");
                                mLastWeather = weather;
                                showWeather(weather);
                                mWeatherSubscription.unsubscribe();
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Error on weather request", throwable);
                                showError("Error while executing the request. Please try again.");
                                mWeatherSubscription.unsubscribe();
                            }
                        }
                );
    }

    private void showLoading() {
        for (ISearchWeatherView view : getViews()) {
            view.showLoading();
        }
    }

    private void showError(String message) {
        for (ISearchWeatherView view : getViews()) {
            view.showError(message);
        }
    }

    private void showSuggestions(List<String> suggestions) {
        for (ISearchWeatherView view : getViews()) {
            view.showSuggestions(suggestions);
        }
    }

    private void showSuggestionsLoading() {
        for (ISearchWeatherView view : getViews()) {
            view.showSuggestionsLoading();
        }
    }

    private void showSuggestionsError(String message) {
        for (ISearchWeatherView view : getViews()) {
            view.showSuggestionsError(message);
        }
    }

    private void showWeather(Weather weather) {
        for (ISearchWeatherView view : getViews()) {
            view.showWeather(weather);
        }
    }

}
