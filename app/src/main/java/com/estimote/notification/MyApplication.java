package com.estimote.notification;

import android.app.Application;

import com.estimote.coresdk.cloud.api.EstimoteApi;
import com.estimote.notification.estimote.BeaconNotificationsManager;
import com.estimote.coresdk.common.config.EstimoteSDK;

public class MyApplication extends Application {

    private boolean beaconNotificationsEnabled = false;
    String enterMessage;

    @Override
    public void onCreate() {
        super.onCreate();
        // You can get them by adding your app on https://cloud.estimote.com/#/apps
        EstimoteSDK.initialize(getApplicationContext(), "t4kitu00-students-oamk-fi--7rx", "02c8d7eb0556810d218bfe75dca7e980");
    }

    public void setTemperature(String temp)
    {
        enterMessage = "Beacon lähellä " + temp;
    }

    public void enableBeaconNotifications() {
        if (beaconNotificationsEnabled) { return; }

        BeaconNotificationsManager beaconNotificationsManager = new BeaconNotificationsManager(this);
        beaconNotificationsManager.addNotification(
                "9a7e34e9683bbfbc2823fee60beab107",
                "Majakka lähietäisyydellä.",
                "Olet liian kaukana majakasta.");
        beaconNotificationsManager.startMonitoring();
        beaconNotificationsEnabled = true;
    }

    public boolean isBeaconNotificationsEnabled() {
        return beaconNotificationsEnabled;
    }
}
