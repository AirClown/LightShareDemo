package com.example.yushichao.lightsharedemo;

import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Camera1 camera1;

    private int sensorShowType = 0;
    private SensorController sensorController = null;
    private SensorController.SensorDataCallback sensorDataCallback=new SensorController.SensorDataCallback() {
        @Override
        public void refreshData(int type, float[] datas) {
            switch (type){
                case Sensor.TYPE_ACCELEROMETER:
                    float acc = (float) Math.sqrt(
                            datas[0]*datas[0]+
                            datas[1]*datas[1]+
                            datas[2]*datas[2]);

                    if(sensorShowType == type) dataView.pushData(type,acc);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    float mag = (float) Math.sqrt(
                                    datas[0]*datas[0]+
                                    datas[1]*datas[1]+
                                    datas[2]*datas[2]);
                    if(sensorShowType == type) dataView.pushData(type,mag);
                    break;
            }
        }
    };

    private SurfaceView surfaceView;
    private DataView dataView;
    private Button Acc,Mag,TackPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sensorController = new SensorController((SensorManager) getSystemService(SENSOR_SERVICE),sensorDataCallback);

        InitUi();

        camera1 = new Camera1(surfaceView, 0);
        camera1.openCamera();
    }

    private void InitUi(){
        Acc = findViewById(R.id.bt_acc);
        Acc.setOnClickListener(this);

        Mag = findViewById(R.id.bt_mag);
        Mag.setOnClickListener(this);

        TackPhoto = findViewById(R.id.take_photo);
        TackPhoto.setOnClickListener(this);

        dataView = findViewById(R.id.dataview);
        dataView.setDataRange(100);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_acc:
                if(sensorController != null){
                    if(sensorController.sensorContains(Sensor.TYPE_ACCELEROMETER)&&sensorShowType == Sensor.TYPE_ACCELEROMETER) {
                        sensorController.unregisterSensor(Sensor.TYPE_ACCELEROMETER);
                    } else {
                        sensorShowType = Sensor.TYPE_ACCELEROMETER;
                        sensorController.registerSensor(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
                    }
                }
                break;
            case R.id.bt_mag:
                if(sensorController != null){
                    if(sensorController.sensorContains(Sensor.TYPE_MAGNETIC_FIELD)&&sensorShowType == Sensor.TYPE_MAGNETIC_FIELD) {
                        sensorController.unregisterSensor(Sensor.TYPE_MAGNETIC_FIELD);
                    } else {
                        sensorShowType = Sensor.TYPE_MAGNETIC_FIELD;
                        sensorController.registerSensor(Sensor.TYPE_MAGNETIC_FIELD, SensorManager.SENSOR_DELAY_GAME);
                    }
                }
                break;
            case R.id.surfaceView:
                if (!camera1.isOpen())
                    camera1.startCamera();
                else
                    camera1.closeCamera();
                break;
            case R.id.take_photo:
                if (camera1.isOpen()){
                    Bitmap bitmap = camera1.TackPhoto();
                    String addr = camera1.savePicture(this.getExternalFilesDir(null).toString(),bitmap);
                    Toast.makeText(this,"图片已保存在："+addr,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //关闭标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //关闭状态栏
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(flag);
        }
    }
}
