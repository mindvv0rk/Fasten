package ai.testtask.fasten.di;

import javax.inject.Singleton;

import ai.testtask.fasten.core.network.IServerAPI;
import ai.testtask.fasten.core.network.NetworkModuleBuilder;
import dagger.Module;
import dagger.Provides;

@Module
public final class NetworkModule {

    @Singleton
    @Provides
    public IServerAPI provideNetworkModule() {
        return NetworkModuleBuilder.createAPI();
    }
}
