package com.example.fingerpaint_pxl172630;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View{
    private Path mPath;
    private Paint mPaint, paintCanvas;
    private int paintColor = Color.RED;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    Context c;
    ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
    ArrayList<Pair<Path, Paint>> undonePaths = new ArrayList<Pair<Path, Paint>>();

    public int width;
    public int height;

    public DrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);
        c = context;
        setupDrawing();
    }
    private void setupDrawing() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(paintColor);

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(9);

        paintCanvas = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        /*for(Path p : paths ){
            canvas.drawPath(p, mPaint);
        }
        canvas.drawPath(mPath, mPaint);*/
        //canvas.drawBitmap(mBitmap, 0, 0, paintCanvas);
        for(Pair<Path, Paint> p : paths ){
            //mPaint.setColor(p.color);
            canvas.drawPath(p.first, p.second);
        }
        canvas.drawPath(mPath, mPaint);
        //canvas.drawBitmap(mBitmap, 0, 0, paintCanvas);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        Paint newPaint = new Paint(mPaint);
        paths.add(new Pair<Path, Paint>(mPath, newPaint));
        mPath = new Path();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public void clearDrawing() {
        paths.clear();
        invalidate();
    }
    public Paint getmPaint() {
        return mPaint;
    }
    public void setPathColor(int color) {
        mPaint.setColor(color);
    }
    public void undoPath() {
        if(paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
        else {

        }
    }
    public void setBrushWidth(int progress) {
        mPaint.setStrokeWidth(progress);
    }
}
