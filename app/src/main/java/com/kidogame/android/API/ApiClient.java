package com.kidogame.android.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intuit.sdp.BuildConfig;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        Retrofit build = new Retrofit.Builder().baseUrl("https://control.bdfatafat.live/").client(new OkHttpClient.Builder().connectTimeout(200, TimeUnit.SECONDS).writeTimeout(200, TimeUnit.SECONDS).readTimeout(200, TimeUnit.SECONDS).addInterceptor(loggingInterceptor).build()).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        retrofit = build;
        return build;
    }
}
