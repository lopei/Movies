package com.anotap.whatagreatmovie.api;

import android.annotation.SuppressLint;

import com.anotap.whatagreatmovie.MovieApp;
import com.anotap.whatagreatmovie.model.ErrorUser;
import com.anotap.whatagreatmovie.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * class that handles all work with network architecture
 */
public class Api {
    private static final String BASE_URL = "http://46.101.218.241/";

    private WebService webService;
    private OkHttpClient client;
    private Gson gson;

    Api() {
        webService = getRetrofit().create(WebService.class);
    }


    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();
    }

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private OkHttpClient getClient() {

        if (client == null) {
            client = new OkHttpClient
                    .Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new MyInterceptor()).build();
        }
        return client;
    }

    WebService getWebService() {
        return webService;
    }


    /**
     * inner class that intercepts api calls and logs the results
     */
    private class MyInterceptor implements Interceptor {
        private static final int CODE_ERROR_CREATING_USER = 422;
        private static final int CODE_OK = 200;

        @SuppressLint("DefaultLocale")
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            if (!MovieApp.getInstance().isConnectedToInternet()) {
                throw new NoInternetException();
            }

            long t1 = System.nanoTime();
            System.out.println(
                    String.format("Sending request %s on %n%s", request.url(), request.headers()));

            Response response = null;
            try {
                response = chain.proceed(request);
            } catch (ConnectException ignored) {
            }

            if (response != null && response.code() == CODE_ERROR_CREATING_USER) {
                if (response.body() != null) {
                    String errorMessage = gson.fromJson(response.body().string(), ErrorUser.class).getErrorMessage();

                    /**
                     * here we replace response body to be able to show returned error
                     */
                    response = response.newBuilder()
                            .code(CODE_OK)
                            .body(ResponseBody.create(response.body().contentType(),
                                    gson.toJson(new User[]{new User(null, errorMessage)})))
                            .build();
                }
            }
            long t2 = System.nanoTime();

            System.out.println(
                    String.format("Received response for %s in %.1fms%n%s", Objects.requireNonNull(response).request().url(),
                            (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }

    public class NoInternetException extends IOException {
        public NoInternetException() {
            super(MovieApp.getInstance().getNoInternetErrorMessage());
        }
    }
}
