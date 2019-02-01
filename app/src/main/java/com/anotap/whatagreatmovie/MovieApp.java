package com.anotap.whatagreatmovie;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.anotap.whatagreatmovie.model.User;

public class MovieApp extends Application {
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";

    private static MovieApp instance;

    public static MovieApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public String getUsername() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(KEY_USERNAME, null);
    }

    public void setUsername(String username) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(KEY_USERNAME, username).apply();
    }

    public int getUserId() {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt(KEY_USER_ID, 0);
    }

    public void setUserId(int userId) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(KEY_USER_ID, userId).apply();
    }

    public void setUser(@Nullable User user) {
        if (user == null) {
            setUsername(null);
            setUserId(0);
        } else {
            setUsername(user.getUsername());
            setUserId(user.getId());
        }
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = null;
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public String getNoInternetErrorMessage() {
        return getString(R.string.no_internet_error);
    }
}
