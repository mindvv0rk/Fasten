package ai.testtask.fasten.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {

    private final Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Singleton
    @Provides
    @NonNull
    public Context getContext() {
        return mContext;
    }

    @Singleton
    @Provides
    public Gson getGson() {
        return new Gson();
    }
}
