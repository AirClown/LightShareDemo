package com.example.yushichao.lightsharedemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class DataView extends View {

    private int dataType = 0;
    private int dataRange = 0;
    private ArrayList datalist = new ArrayList();

    public DataView(Context context) {
        super(context);
    }

    public DataView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDataRange(int range){
        dataRange = range;
    }

    public void pushData(int type,float data){
        if(type!=dataType){
            dataType = type;
            clearData();
        }

        datalist.add(data);
        if(datalist.size()>dataRange){
            datalist.remove(0);
        }

        invalidate();
    }

    private void clearData(){
        datalist.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(datalist.size()>0){
            int height = canvas.getHeight();
            int width = canvas.getWidth();

            float max = -100000;
            float min = 100000;
            for(int i = 0 ;i < datalist.size();++i){
                max = Math.max(max,(float)datalist.get(i));
                min = Math.min(min,(float)datalist.get(i));
            }

            if(max <= min)
                return;

            Paint paint1 = initPaint(Color.GRAY,10,100,Paint.Style.STROKE);

            canvas.drawLine(width/2,0,width/2,height,paint1);

            Path path=new Path();
            path.moveTo(width/2,0);
            float dx = (float) (width - 200) / (max - min);
            float dy = (float)height / (float)datalist.size();

            for(int i = datalist.size()-1;i>=0;--i){
                float x = dx * ((float)datalist.get(i) - min) + 100;
                float y = (datalist.size()-1-i)*dy;
                path.lineTo(x,y);
            }

            canvas.drawPath(path,paint1);
        }
    }

    private Paint initPaint(int color, int width,int size, Paint.Style style){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(width);
        paint.setTextSize(size);
        paint.setStyle(style);
        return paint;
    }
}
