package com.estimote.notification;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.recognition.packets.EstimoteTelemetry;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    String moveState;

    //ADDED
    private BeaconManager beaconManager;
    public String scanId;
    private String TAG2 = "BeaconsLab";
    private String temperature;

    TextView textViewTemperature, textViewBatteryPercentage, textViewAmbientLight, textViewProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        textViewBatteryPercentage = (TextView) findViewById(R.id.textViewBatteryPercentage);
        textViewAmbientLight = (TextView) findViewById(R.id.textViewAmbientLight);
        textViewProximity = (TextView) findViewById(R.id.textViewProximity);
        textViewTemperature.setText("Getting temperature...");
        beaconManager = new BeaconManager(this);
        startScan();
    }

    public void startScan()
    {
        textViewTemperature.setText("Starting telemetry...");
        beaconManager.setTelemetryListener(new BeaconManager.TelemetryListener() {

            @Override
            public void onTelemetriesFound(List<EstimoteTelemetry> telemetries) {
                for (EstimoteTelemetry tlm : telemetries) {
                    Log.d(TAG, "beaconID: " + tlm.deviceId +
                            ", temperatura: " + tlm.temperature + " °C");

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

                    temperature = String.format("Temperature: %s °C", tlm.temperature);
                }
            }
        });
    }

    @Override protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart TelemetryDiscovery");
        //mTextViewTemperatura.setText("Started");
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
    }

    @Override protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop TelemetryDiscovery");
        //mTextViewTemperatura.setText("Stopped");
        beaconManager.stopTelemetryDiscovery();
    }
}
