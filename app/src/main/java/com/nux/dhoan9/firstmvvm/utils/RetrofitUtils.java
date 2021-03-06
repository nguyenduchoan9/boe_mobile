package com.nux.dhoan9.firstmvvm.utils;

import android.content.Context;
import android.util.Log;
import com.nux.dhoan9.firstmvvm.BuildConfig;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.manager.EndpointManager;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.HeaderCredential;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by hoang on 29/04/2017.
 */

public class RetrofitUtils {
    PreferencesManager preferencesManager;
    EndpointManager endpointManager;

    public RetrofitUtils(PreferencesManager preferencesManager, EndpointManager endpointManager) {
        this.preferencesManager = preferencesManager;
        this.endpointManager = endpointManager;
    }

    public Retrofit create() {
        return new Retrofit.Builder()
                .baseUrl(endpointManager.getEndpoint() + "api/")
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static Retrofit create(String endPoint) {
        return new Retrofit.Builder()
                .baseUrl(endPoint)
                .client(clientX())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static OkHttpClient clientX() {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptorLevelHeadersX())
                .addInterceptor(loggingInterceptorLevelBodyX())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public Retrofit parseError() {
        return new Retrofit.Builder()
                .baseUrl(Constant.API_ENDPOINT + "/api/")
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public OkHttpClient client() {
        return new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor())
                .addInterceptor(headerAuthenticateInterceptor())
                .addInterceptor(loggingInterceptorLevelHeaders())
                .addInterceptor(loggingInterceptorLevelBody())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    private HttpLoggingInterceptor loggingInterceptorLevelHeaders() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return httpLoggingInterceptor;
    }

    private HttpLoggingInterceptor loggingInterceptorLevelBody() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private static HttpLoggingInterceptor loggingInterceptorLevelHeadersX() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return httpLoggingInterceptor;
    }

    private static HttpLoggingInterceptor loggingInterceptorLevelBodyX() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private Interceptor headerInterceptor() {
        return chain -> {
            Request request = chain.request();
            Headers newHeaders = request.headers()
                    .newBuilder()
                    .add(BuildConfig.HEADER_VERSION_KEY, BuildConfig.HEADER_VERSION_VALUE)
                    .add("Accept", "application/json")
                    .add("Accept-Charset", "utf-8")
                    .add("Cache-Control", "no-cache")
                    .build();
            request = request.newBuilder()
                    .headers(newHeaders)
                    .build();
            return chain.proceed(request);
        };
    }

    private Interceptor headerAuthenticateInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.d("retrofitUtils", "hedaerAuthen");
                Request request = chain.request();
                if (null != preferencesManager) {
                    if (preferencesManager.isLoggedin()) {
                        HeaderCredential headerCredential;
                        if (null != (headerCredential = preferencesManager.getCredentialHeader())) {
                            Headers newHeaders = request.headers()
                                    .newBuilder()
                                    .add(Constant.HEADER_ACCESS_TOKEN, headerCredential.getAccesToken())
                                    .add(Constant.HEADER_TOKEN_TYPE, headerCredential.getTokenType())
                                    .add(Constant.HEADER_CLIENT, headerCredential.getClient())
                                    .add(Constant.HEADER_EXPIRY, headerCredential.getExpiry())
                                    .add(Constant.HEADER_UID, headerCredential.getUid())
                                    .build();
                            request = request.newBuilder()
                                    .headers(newHeaders)
                                    .build();
                        }
                    }
                }
                return chain.proceed(request);
            }
        };
    }

    private Interceptor convertToBaseResponse() {
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            response.code();

            return null;
        };
    }

    public static String getMessageError(Context context, Throwable e) {
        if (e instanceof HttpException) {
            HttpException xException = (HttpException) e;
            int errorCode = xException.code();
            String msg = "";
            Log.d("Hoangcode", "getMessageError: " + errorCode);
            if (errorCode == 404) {
                msg = "The url is not found";
            }
            return context.getString(R.string.text_response_error_not_process);
            // We had non-2XX http error
//"The server is under maintenance"
        } else if (e instanceof IOException) {
            // A network or conversion error happened
//context.getString()
            return context.getString(R.string.text_response_error_connection);
        }
        return context.getString(R.string.text_response_error_msg);
    }
}
