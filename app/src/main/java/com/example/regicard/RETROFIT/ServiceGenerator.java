package com.example.regicard.RETROFIT;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator{
//    private static final String URL = "http://192.168.0.61:5408/";
//    private static final String URL = "http://192.168.0.10:98/";
//    private static final String URL = "http://192.168.0.177:8080/AM/";
//    private static final String URL = "http://localhost:8083/";    //test
    private static final String URL = "http://192.168.0.177:8080/";


    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build());

    private  static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
