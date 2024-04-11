package com.example.myappweather.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import com.example.myappweather.Activities.MainActivity;

public class NetWorkBRC extends BroadcastReceiver {
    MainActivity mainActivity;
    public NetWorkBRC(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    };
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if(isOnline(context)){
                Toast.makeText(context,"ONLINE",Toast.LENGTH_SHORT).show();
                //khi co mang goi phuong thuc nay
                mainActivity.setView();
                mainActivity.ktraQuyen();
                mainActivity.setRefresh();
            }else {
                Intent settingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(settingsIntent);
                Toast.makeText(context,"NO INTERNET",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Boolean isOnline(Context context){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null) && (networkInfo.isConnected());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
