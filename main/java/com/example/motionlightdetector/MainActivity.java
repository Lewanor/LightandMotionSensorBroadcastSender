package com.example.motionlightdetector;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    String packet = "com.sending.forhomework";

//    Intent services;
    checkChange checc;
    double torch = 0;
    double flame = 0;
    double change;

    float lightlevel;
    float x, y, z;

        SensorManager sensorManager;
        Sensor sensor;
        Sensor sensorLight;
        SensorEventListener sensorEventListener;
        SensorEventListener lightListener;

//    MyService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checc = new checkChange();

//        services = new Intent(getApplication(),MainActivity.MyService.class);
//        startService(services);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                z = sensorEvent.values[2];
                torch = Math.sqrt(x*x + y*y + z*z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        lightListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                lightlevel = sensorEvent.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(lightListener, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);


        new Thread(checc).start();

    }

    public class checkChange implements Runnable {
        @Override
        public void run(){
            while(true){
                change = Math.abs(torch - flame);
                if (change > 0.05 && lightlevel < 50) {
                    Intent intent = new Intent();
                    intent.setAction(packet);
                    intent.putExtra("data","playmusic");
                    intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendBroadcast(intent);
                    showToast("BBBBB");

                }
                else if (change <= 0.05){
                    Intent bintent = new Intent();
                    bintent.setAction(packet);
                    bintent.putExtra("data","mutemusic");
                    bintent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendBroadcast(bintent);
                    showToast("AAAAA");
                }

                flame = torch;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);
    }

    public void showToast(String toast)
    {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show());
    }

//    public class MyService extends Service {
//
//        SensorManager sensorManager;
//        Sensor sensor;
//        SensorEventListener sensorEventListener;
//
//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//            sensorManager.unregisterListener(sensorEventListener);
//        }
//
//        @Override
//        public IBinder onBind(Intent intent) {
//            // TODO: Return the communication channel to the service.
//            return null;
//        }
//
//        @Override
//        public void onCreate() {
//            super.onCreate();
//            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//
//

//            showToast("oncreate");
//        }
//
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//
//            sensorEventListener = new SensorEventListener() {
//                @Override
//                public void onSensorChanged(SensorEvent sensorEvent) {
//                    x = sensorEvent.values[0];
//                    y = sensorEvent.values[1];
//                    z = sensorEvent.values[2];
//                    torch = Math.sqrt(x*x + y*y + z*z);
//                    showToast("inside loop");
//                }
//
//                @Override
//                public void onAccuracyChanged(Sensor sensor, int i) {
//
//                }
//            };
//            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
//
//            return START_STICKY;
//        }
//    }

}