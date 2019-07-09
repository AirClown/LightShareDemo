package com.example.yushichao.lightsharedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class Camera1 {

    private byte[] photo = null;
    private boolean open;
    private Camera camera;
    private int cameraPosition = 0;

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {

            photo = bytes.clone();
        }
    };

    private SurfaceHolder holder;
    private SurfaceView surfaceView = null;

    public Camera1(SurfaceView surfaceView,int cameraPosition){
        this.open = false;
        this.surfaceView = surfaceView;
        this.cameraPosition = cameraPosition;
    }

    public boolean isOpen(){
        return open;
    }

    public void openCamera(){
        if(surfaceView == null || open)
            return;

        holder=surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //startCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    public void startCamera(){
        open = true;
        try {
            camera = Camera.open(cameraPosition);
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(previewCallback);
            camera.startPreview();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap TackPhoto(){
        Bitmap bitmap = null;
        if (photo!=null){
            Camera.Size size = camera.getParameters().getPreviewSize();
            try {
                YuvImage image = new YuvImage(photo, ImageFormat.NV21, size.width, size.height, null);
                if (image != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                    bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                    stream.close();
                }
            } catch (Exception ex) {
                Log.e("Sys", "Error:" + ex.getMessage());
            }
        }

        return bitmap;
    }

    public void closeCamera(){
        if(open){
            open = false;
            camera.stopPreview();
        }
    }

    private static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public String savePicture(String path,Bitmap bm){
        byte[] bytes = Bitmap2Bytes(bm);
        String addr=path + "/";//activity.getExternalFilesDir(null)+"/";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        addr+=  formatter.format(System.currentTimeMillis());
        File file=new File(addr+".jpg");

        try{
            FileOutputStream output = new FileOutputStream(file);
            output.write(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }

        return addr;
    }
}
