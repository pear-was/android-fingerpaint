package com.example.fingerpaint_pxl172630;

import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener {
    DrawingView dv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);*/
        //dv = new DrawingView(this, paint);
        dv = (DrawingView)findViewById(R.id.drawCanvas);

//        this.getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        SeekBar seekbar = (SeekBar)findViewById(R.id.seekBar);
        int fourthWidth = (int)(getScreenWidth() * .25);
        System.out.println("Screen width: " + getScreenWidth());
        System.out.println("Fourth width: " + fourthWidth);
        seekbar.setMax(fourthWidth - 3);
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                dv.setBrushWidth(progress + 3);
                System.out.println("Brush size is currently: " + (progress + 3));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekbar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {

            }
        });
    }

    public void clearButton(View view) {
        dv.clearDrawing();
    }

    public void colorButton(View view) {
        new ColorPickerDialog(this, this, dv.getmPaint().getColor()).show();
    }
    public void colorChanged(int color) {
        dv.setPathColor(color);
    }

    public void undoButton(View view) {
        dv.undoPath();
    }

    public int getScreenWidth() {
        int width;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        return width;
    }
}
