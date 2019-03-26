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

        // Find DrawView element in MainActivity
        dv = (DrawingView)findViewById(R.id.drawCanvas);

        // Find SeekBar element in MainActivity, set max size to be 25% of screen width
        SeekBar seekbar = (SeekBar)findViewById(R.id.seekBar);
        int fourthWidth = (int)(getScreenWidth() * .25);
        seekbar.setMax(fourthWidth - 3);

        // SeekBar listener, set brush width in accordance with progress of the seekbar
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
                dv.setBrushWidth(progress + 3);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekbar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {

            }
        });
    }

    // Throw back to DrawView to clear canvas
    public void clearButton(View view) {
        dv.clearDrawing();
    }

    // Throw to ColorPicker to pull up a color picker
    public void colorButton(View view) {
        new ColorPickerDialog(this, this, dv.getmPaint().getColor()).show();
    }
    // Change color based on what is chosen with the color picker
    public void colorChanged(int color) {
        dv.setPathColor(color);
    }

    // Throw back to tDrawView to undo last drawn line
    public void undoButton(View view) {
        dv.undoPath();
    }

    // Find the screen width, for use later with brush size
    public int getScreenWidth() {
        int width;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        return width;
    }
}
