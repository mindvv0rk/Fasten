package ai.testtask.fasten.core.network;

import com.google.gson.GsonBuilder;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkModuleFactory {

    private static final String TOKEN = "6058ac702f7bdd2e";
    private static final String WEATHER_BASE_URL = "http://api.wunderground.com/api/".concat(TOKEN);
    private static final String AUTOCOMPLETE_BASE_URL = "http://autocomplete.wunderground.com";

    private static final String WEATHER_HTTP_LOG_TAG = "WeatherOkHttp";
    private static final String AUTOCOMPLETE__HTTP_LOG_TAG = "AutocompleteOkHttp";

    private static final int SOCKET_READ_TIMEOUT = 30;
    private static final int SOCKET_CONNECT_TIMEOUT = 30;

    public static IWeatherAPI createWeatherAPI() {
        return createAPI(WEATHER_BASE_URL, IWeatherAPI.class, WEATHER_HTTP_LOG_TAG);
    }

    public static IAutocompleteAPI createAutocompleteAPI() {
        return createAPI(AUTOCOMPLETE_BASE_URL, IAutocompleteAPI.class, AUTOCOMPLETE__HTTP_LOG_TAG);
    }

    private static <T> T createAPI(String baseUrl, Class<T> apiClass, final String httpLogTag) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(SOCKET_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(SOCKET_READ_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(httpLogTag, message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        OkHttpClient httpClient = builder.build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setPrettyPrinting().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient);

        return retrofitBuilder.build().create(apiClass);
    }
}
