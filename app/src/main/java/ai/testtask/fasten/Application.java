package ai.testtask.fasten;

import ai.testtask.fasten.di.AppModule;
import ai.testtask.fasten.di.Dagger;
import ai.testtask.fasten.di.DaggerAppComponent;

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
