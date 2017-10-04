package ai.testtask.fasten.core.di;

import javax.inject.Singleton;

import ai.testtask.fasten.weather.current.CurrentWeatherPresenter;
import ai.testtask.fasten.weather.search.SearchWeatherPresenter;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class
})
public interface AppComponent {

    void inject(CurrentWeatherPresenter currentWeatherPresenter);

    void inject(SearchWeatherPresenter searchWeatherPresenter);
}
