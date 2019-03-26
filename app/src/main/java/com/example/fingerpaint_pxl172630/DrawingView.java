/******************************************************************************
 * This is an application written for 4301.002, which allows the user to draw
 * on a touchscreen using their finger, otherwise known as "fingerpaint". The
 * user can change the color of the line using a color picker, and the thickness
 * of the line using a slider. Also, the user can clear the canvas or undo the
 * drawing line by line.
 *
 * Written by Perry Lee (pxl172630) at The University of Texas at Dallas
 * starting March 24, 2019, for an Android development course.
 ******************************************************************************/

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
    // We need to use a Pair of <Path, Paint> in order to maintain history of each Path and
    // also its color. This can be resource intensive if many different colored Paths occur
    ArrayList<Pair<Path, Paint>> paths = new ArrayList<Pair<Path, Paint>>();
    ArrayList<Pair<Path, Paint>> undonePaths = new ArrayList<Pair<Path, Paint>>();

    public DrawingView(Context context, AttributeSet attributes) {
        super(context, attributes);
        c = context;
        setupDrawing();
    }
    // Initialize Path and Paint to certain values, these will allow for smooth path creation,
    // as well as initial colors
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

    // Override onSizeChanged to create new Bitmap
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
    // Override onDraw, for each Pair of <Path, Paint> draw to canvas. The second drawPath is
    // necessary to have the user see the Path as it is drawn
    @Override
    protected void onDraw(Canvas canvas) {
        for(Pair<Path, Paint> p : paths ){
            canvas.drawPath(p.first, p.second);
        }
        canvas.drawPath(mPath, mPaint);
    }

    // Initialize mX and mY float variables to hold X and Y values
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    // Detect when user touches touchscreen, and sets mX and mY to current position
    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    // Detect when user moves while touching touchscreen
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    // Detect when user releases their finger from the touchscreen, begin
    // a new path
    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        Paint newPaint = new Paint(mPaint);
        paths.add(new Pair<Path, Paint>(mPath, newPaint));
        mPath = new Path();
    }
    // Override onTouchEvent to run touch_start, touch_move, and touch_up based on which
    // event occurs on the touchscreen
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

    // Clear paths in order to clear the drawing from the canvas
    public void clearDrawing() {
        paths.clear();
        invalidate();
    }
    // Getter for mPaint
    public Paint getmPaint() {
        return mPaint;
    }
    // Sets the path color, should receive the color from the color picker called in
    // MainActivity
    public void setPathColor(int color) {
        mPaint.setColor(color);
    }
    // Undo Path by 1, should be called from undoButton in MainActivity
    public void undoPath() {
        if(paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        }
        else {

        }
    }
    // Sets brush width based on progress from SeekBar in MainActivity, using
    // setStrokeWidth
    public void setBrushWidth(int progress) {
        mPaint.setStrokeWidth(progress);
    }
}
