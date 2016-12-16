package com.example.android.sketch;

import android.util.Log;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class DrawingView extends View
{
    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();
    private ArrayList<float[]> PointsCal = new ArrayList<float[]>();
    private ArrayList<float[]> Points = new ArrayList<float[]>();
    private ArrayList<Integer> id = new ArrayList<Integer>();
    private int counter;

    public DrawingView(Context context, AttributeSet attr)
    {
        super(context,attr);
        setupDrawing();
    }

    private void setupDrawing()
    {
        drawPath = new Path();
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        drawPaint = new Paint();
        drawCanvas = new Canvas();
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        counter = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //mPath = new Path();
        //canvas.drawPath(mPath, mPaint);
        for (Path p : paths){
            canvas.drawPath(p, drawPaint);
        }
        canvas.drawPath(drawPath,drawPaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        drawPath.reset();
        drawPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            drawPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        drawPath.lineTo(mX, mY);
        // commit the path to our offscreen
        drawCanvas.drawPath(drawPath, drawPaint);
        // kill this so we don't double draw
        paths.add(drawPath);
        drawPath = new Path();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float coordinates[] = {0f,0f};
        float coord[] = {0f,0f};

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                coordinates[0] = x;
                coordinates[1] = y;
                PointsCal.add(coordinates);
                Points.add(coordinates);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                coordinates[0] = x;
                coordinates[1] = y;
                PointsCal.add(coordinates);
                coord[0] = coordinates[0] - PointsCal.get(PointsCal.size() - 2)[0];
                coord[1] = coordinates[1] - PointsCal.get(PointsCal.size() - 2)[1];
                Points.add(coord);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                coordinates[0] = x;
                coordinates[1] = y;
                PointsCal.add(coordinates);
                coord[0] = coordinates[0] - PointsCal.get(PointsCal.size() - 2)[0];
                coord[1] = coordinates[1] - PointsCal.get(PointsCal.size() - 2)[1];
                Points.add(coord);
                String sample = "";
                if (Points.size() > 2)
                {
                    counter++;
                    id.add(counter);
                    sample+=("#"+counter);
                    for (int i = 0; i < Points.size(); i++) {
                        sample += ("#" + Arrays.toString(Points.get(i)));
                    }
                    Log.d("Size -- ", ""+Points.size());

                    new ClientSend().execute(sample);
                }
                else
                {
                    paths.remove(paths.size()-1);
                }
                Points.clear();
                PointsCal.clear();
                invalidate();
                break;
        }
        return true;
    }

    public void startNew(String state){
        paths.clear();
        id.clear();
        counter=0;
        if (state.equals("reset"))
            new ClientSend().execute("#"+0);
        else
            new ClientSend().execute("!"+0);
        invalidate();
    }

    public void undo(){
        if (paths.size()>0)
        {
            Log.d("Number ", ""+paths.size());
            //undonePaths.add(paths.remove(paths.size()-1));
            new ClientSend().execute("#"+String.valueOf(id.get(id.size()-1)));
            id.remove(id.size()-1);
            paths.remove(paths.size()-1);
            invalidate();
        }
    }
}
