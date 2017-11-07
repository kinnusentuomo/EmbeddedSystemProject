package com.estimote.notification;
//package com.estimote.proximity;

import android.graphics.Region;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.estimote.coresdk.cloud.model.BeaconInfo;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.recognition.packets.DeviceType;
import com.estimote.coresdk.recognition.packets.EstimoteTelemetry;
import com.estimote.coresdk.service.BeaconManager;
//import com.estimote.sdk.connection.scanner.ConfigurableDevicesScanner;
import com.estimote.coresdk.recognition.packets.ConfigurableDevice;
import com.estimote.mgmtsdk.connection.protocol.characteristic.EstimoteUuid;

import com.estimote.coresdk.cloud.model.Color;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.notification.estimote.ProximityContentManager;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import java.util.List;
import java.util.UUID;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MainActivity extends AppCompatActivity {


    String mintBeaconId = "9a7e34e9683bbfbc2823fee60beab107"; //Mint(tyhjä)

    String yellowBeaconId = "2237fbe66fa7fe58013b4c1986516e2d";


    private static final String TAG = "MainActivity";
    String moveState;

    private static final UUID ESTIMOTE_PROXIMITY_UUID = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");

    //ADDED
    private BeaconManager beaconManager;
    public String scanId;
    private String TAG2 = "BeaconsLab";
    private String temperature;

    private static final Map<Color, Integer> BACKGROUND_COLORS = new HashMap<>();

    TextView textViewTemperature, textViewBatteryPercentage, textViewAmbientLight, textViewProximity, textViewBeaconColor;
    private com.estimote.notification.estimote.ProximityContentManager proximityContentManager;

    static {
        BACKGROUND_COLORS.put(Color.ICY_MARSHMALLOW, android.graphics.Color.rgb(109, 170, 199));
        BACKGROUND_COLORS.put(Color.BLUEBERRY_PIE, android.graphics.Color.rgb(98, 84, 158));
        BACKGROUND_COLORS.put(Color.MINT_COCKTAIL, android.graphics.Color.rgb(155, 186, 160));
    }

    private static final int BACKGROUND_COLOR_NEUTRAL = android.graphics.Color.rgb(160, 169, 172);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        textViewBatteryPercentage = (TextView) findViewById(R.id.textViewBatteryPercentage);
        textViewAmbientLight = (TextView) findViewById(R.id.textViewAmbientLight);
        textViewProximity = (TextView) findViewById(R.id.textViewProximity);
        textViewBeaconColor = (TextView) findViewById(R.id.textViewBeaconColor);
        textViewTemperature.setText("Getting temp...");
        beaconManager = new BeaconManager(this);




        setBeaconId(mintBeaconId);
        //getBeaconSensorInfo();
    }



    public void setBeaconId(String beaconId)
    {
        textViewBeaconColor.setText("Searching for beacon w/ id: " + beaconId);
        proximityContentManager = new ProximityContentManager(this,
                Arrays.asList(
                        beaconId),
                new com.estimote.notification.estimote.EstimoteCloudBeaconDetailsFactory());


        proximityContentManager.setListener(new com.estimote.notification.estimote.ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {
                String text;
                Integer backgroundColor;
                if (content != null) {
                    com.estimote.notification.estimote.EstimoteCloudBeaconDetails beaconDetails = (com.estimote.notification.estimote.EstimoteCloudBeaconDetails) content;
                    text = "You're in " + beaconDetails.getBeaconName() + "'s range! Which colour is: " + beaconDetails.getBeaconColor();
                    backgroundColor = BACKGROUND_COLORS.get(beaconDetails.getBeaconColor());



                    //ADDED
                    beaconManager.setTelemetryListener(new BeaconManager.TelemetryListener() {

                        @Override
                        public void onTelemetriesFound(List<EstimoteTelemetry> telemetries) {
                            for (EstimoteTelemetry tlm : telemetries) {
                                Log.d(TAG, "beaconID: " + tlm.deviceId +
                                        ", temperature: " + tlm.temperature + " °C");

                                //Set text values to UI
                                //textViewTemperature.setText(String.format("Id: [%s", tlm.deviceId.toString().substring(tlm.deviceId.toString().length() - 5)));
                                textViewTemperature.setText(String.format("Temperature: %s °C", tlm.temperature));
                                textViewBatteryPercentage.setText("Battery percentage: " + tlm.batteryPercentage + "%");
                                textViewAmbientLight.setText("Ambient light: " + tlm.ambientLight);

                                if(tlm.motionState)
                                {
                                    moveState = "Moving";
                                }
                                else
                                {
                                    moveState = "Not moving";
                                }
                                textViewProximity.setText(moveState);

                                Log.d("isBeaconMoving", String.valueOf(tlm.motionState));

                                temperature = String.format("Temperature: %s °C", tlm.temperature);
                            }
                        }
                    });

                    //END




                } else {
                    text = "No beacons in range.";
                    backgroundColor = null;
                }
                ((TextView) findViewById(R.id.textViewBeaconColor)).setText(text);
                /*findViewById(R.layout.activity_main).setBackgroundColor(
                        backgroundColor != null ? backgroundColor : BACKGROUND_COLOR_NEUTRAL);*/


            }
        });



    }

    public void getBeaconSensorInfo()
    {
        //final Region ALL_ESTIMOTE_BEACONS = new Region("rid", ESTIMOTE_PROXIMITY_UUID, null, null);

        EstimoteSDK.initialize(getApplicationContext(), "t4kitu00-students-oamk-fi--7rx", "02c8d7eb0556810d218bfe75dca7e980");


        beaconManager.setTelemetryListener(new BeaconManager.TelemetryListener() {

            @Override
            public void onTelemetriesFound(List<EstimoteTelemetry> telemetries) {
                for (EstimoteTelemetry tlm : telemetries) {
                    Log.d(TAG, "beaconID: " + tlm.deviceId +
                            ", temperature: " + tlm.temperature + " °C");

                    //Set text values to UI
                    //textViewTemperature.setText(String.format("Id: [%s", tlm.deviceId.toString().substring(tlm.deviceId.toString().length() - 5)));
                    textViewTemperature.setText(String.format("Temperature: %s °C", tlm.temperature));
                    textViewBatteryPercentage.setText("Battery percentage: " + tlm.batteryPercentage + "%");
                    textViewAmbientLight.setText("Ambient light: " + tlm.ambientLight);

                    if(tlm.motionState)
                    {
                        moveState = "Moving";
                    }
                    else
                    {
                        moveState = "Not moving";
                    }
                    textViewProximity.setText(moveState);

                    Log.d("isBeaconMoving", String.valueOf(tlm.motionState));

                    temperature = String.format("Temperature: %s °C", tlm.temperature);
                }
            }
        });
    }

    @Override protected void onStart() {
        super.onStart();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                //scanId = beaconManager.startTelemetryDiscovery();
                beaconManager.startTelemetryDiscovery();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication app = (MyApplication) getApplication();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else if (!app.isBeaconNotificationsEnabled()) {
            Log.d(TAG, "Enabling beacon notifications");
            app.enableBeaconNotifications();
        }

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ProximityContentManager content updates");
            proximityContentManager.startContentUpdates();
        }
    }

    @Override protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop TelemetryDiscovery");
        //mTextViewTemperatura.setText("Stopped");
        beaconManager.stopTelemetryDiscovery();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Stopping ProximityContentManager content updates");
        proximityContentManager.stopContentUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proximityContentManager.destroy();
    }
}
