package com.example.nishchay.suddenmovement;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;

    private ComponentName componentName;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startService(new Intent(this, BackgroundService.class));
        componentName = new ComponentName(this, AdminReceiver.class);
        lock();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

//        Button adminBtn = (Button) findViewById(R.id.device_admin_button);
//
//        adminBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                lock();
//            }
//        });
    }

    public void lock() {

        Intent intent = new Intent
                (DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "");

        startActivity(intent);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdate > 100) {
                long diffTime = currentTime - lastUpdate;
                lastUpdate = diffTime;

                float speed = Math.abs(2 * x + z - last_x - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("TAG", "Threshold reached. Will try to lock phone !");
                    Toast.makeText(this, "Threshold reached. Will try to lock phone !", Toast.LENGTH_SHORT).show();
                    DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                    ComponentName componentName = new ComponentName(this, AdminReceiver.class);
                    devicePolicyManager.lockNow();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this);
//        Toast.makeText(this, "Threshold reached. Will try to lock phone !", Toast.LENGTH_SHORT).show();
//        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
//        ComponentName componentName = new ComponentName(this, AdminReceiver.class);
//        devicePolicyManager.lockNow();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
