package com.nux.dhoan9.firstmvvm.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.nux.dhoan9.firstmvvm.BuildConfig;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Locale;

/**
 * Created by hoang on 27/05/2017.
 */

public class Utils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = null;
            sockaddr = new InetSocketAddress(BuildConfig.IP_BASE_URL, 3000);

            sock.connect(sockaddr, timeoutMs);
            sock.close();
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 " + getHost(Constant.API_ENDPOINT));
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getHost(String url) {
        URL parseUrl = null;
        try {
            parseUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return parseUrl.getHost();
    }

    public static Drawable drawableFromUrl(Resources resources, String url) {
        Bitmap x = null;

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            x = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return new BitmapDrawable(resources, x);
    }

    public static Drawable changeDrawableColor(Drawable drawable, int newColor) {
//        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        drawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    public static String getLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.ID, Context.MODE_PRIVATE);
        return sharedPreferences.getString("LANGUAGE_INFO", Constant.VI_LANGUAGE_STRING);
    }

    @SuppressWarnings("deprecation")
    public static void handleSelectLanguage(Activity activity, String lang) {
        String languageToLoad = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
//        Context context  = createConfigurationContext(config);
//        Resources resources = context.getResources();
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }
}
