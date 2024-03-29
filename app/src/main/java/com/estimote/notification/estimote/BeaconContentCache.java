package com.estimote.notification.estimote;

import java.util.HashMap;
import java.util.Map;

public class BeaconContentCache implements BeaconContentFactory {

    private BeaconContentFactory beaconContentFactory;

    private Map<String, Object> cachedContent = new HashMap<>();

    public BeaconContentCache(BeaconContentFactory beaconContentFactory) {
        this.beaconContentFactory = beaconContentFactory;
    }

    @Override
    public void getContent(final String beaconID, final Callback callback) {
        if (cachedContent.containsKey(beaconID)) {
            callback.onContentReady(cachedContent.get(beaconID));
        } else {
            beaconContentFactory.getContent(beaconID, new Callback() {
                @Override
                public void onContentReady(Object content) {
                    cachedContent.put(beaconID, content);
                    callback.onContentReady(content);
                }
            });
        }
    }
}
