package com.example.yushichao.lightsharedemo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.HashSet;
import java.util.Set;

public class SensorController implements SensorEventListener {

    public interface SensorDataCallback{
        void refreshData(int type,float[] datas);
    }

    private Set<Integer> sensors;
    private SensorManager manager = null;
    private SensorDataCallback callback = null;

    public SensorController(SensorManager manager,SensorDataCallback callback){
        this.manager = manager;
        this.callback = callback;
        this.sensors = new HashSet<>();
    }

    public boolean registerSensor(int type,int speed){
        if(manager!=null&&!sensors.contains(type)){
            sensors.add(type);
            Sensor sensor=manager.getDefaultSensor(type);
            manager.registerListener(this,sensor,speed);
            return true;
        }
        return false;
    }

    public boolean unregisterSensor(int type){
        if(manager!=null&&sensors.contains(type)){
            sensors.remove(type);
            Sensor sensor=manager.getDefaultSensor(type);
            manager.unregisterListener(this,sensor);
            return true;
        }
        return false;
    }

    public boolean sensorContains(int type){
        return sensors.contains(type);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(callback == null)
            return;

        callback.refreshData(sensorEvent.sensor.getType(),sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
