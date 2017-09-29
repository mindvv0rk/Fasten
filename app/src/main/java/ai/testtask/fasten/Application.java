package ai.testtask.fasten;

import ai.testtask.fasten.core.di.AppModule;
import ai.testtask.fasten.core.di.Dagger;
import ai.testtask.fasten.core.di.DaggerAppComponent;

public final class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Dagger.init(DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build());
    }
}
