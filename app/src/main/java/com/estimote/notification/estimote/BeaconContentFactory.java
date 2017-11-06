package com.estimote.notification.estimote;

public interface BeaconContentFactory {

    void getContent(String deviceID, Callback callback);

    interface Callback {
        void onContentReady(Object content);
    }
}
