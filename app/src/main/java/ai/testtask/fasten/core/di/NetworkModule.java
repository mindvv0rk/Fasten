package ai.testtask.fasten.core.di;

import javax.inject.Singleton;

import ai.testtask.fasten.core.network.IAutocompleteAPI;
import ai.testtask.fasten.core.network.IWeatherAPI;
import ai.testtask.fasten.core.network.NetworkModuleFactory;
import dagger.Module;
import dagger.Provides;

@Module
public final class NetworkModule {

    @Singleton
    @Provides
    public IWeatherAPI provideWeatherNetworkModule() {
        return NetworkModuleFactory.createWeatherAPI();
    }

    @Singleton
    @Provides
    public IAutocompleteAPI provideAutocompleteNetworkModule() {
        return NetworkModuleFactory.createAutocompleteAPI();
    }
}
