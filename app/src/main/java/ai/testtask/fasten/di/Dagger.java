package ai.testtask.fasten.di;

import android.support.annotation.NonNull;

public final class Dagger {

    private static AppComponent sAppComponent;

    public static void init(@NonNull AppComponent appComponent) {
        sAppComponent = appComponent;
    }

    @NonNull
    public static AppComponent getAppComponent() {
        if (sAppComponent == null) {
            throw new RuntimeException("AppComponent not initialized yet!");
        }
        return sAppComponent;
    }
}
