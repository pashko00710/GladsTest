package me.uptop.gladstest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import me.uptop.gladstest.App;
import rx.Observable;

public class NetworkStatusChecker {
    public static boolean isNetworkAvailable (){
        ConnectivityManager cm = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static Observable<Boolean> isInternetAvialable() {
        return Observable.just(isNetworkAvailable());
    }
}
