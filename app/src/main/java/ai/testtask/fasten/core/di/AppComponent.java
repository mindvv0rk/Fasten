package ai.testtask.fasten.core.di;

import javax.inject.Singleton;

import ai.testtask.fasten.weather.current.CurrentWeatherPresenter;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class
})
public interface AppComponent {

    void inject(CurrentWeatherPresenter currentWeatherPresenter);
}
