package ai.testtask.fasten.core.network;

import com.google.gson.GsonBuilder;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkModuleBuilder {

    private static final String BASE_URL = "";

    private static final String HTTP_LOG_TAG = "OkHttp";

    private static final int SOCKET_READ_TIMEOUT = 30;
    private static final int SOCKET_CONNECT_TIMEOUT = 30;

    public static IServerAPI createAPI() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(SOCKET_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(SOCKET_READ_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(HTTP_LOG_TAG, message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        OkHttpClient httpClient = builder.build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setPrettyPrinting().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient);

        return retrofitBuilder.build().create(IServerAPI.class);
    }
}
