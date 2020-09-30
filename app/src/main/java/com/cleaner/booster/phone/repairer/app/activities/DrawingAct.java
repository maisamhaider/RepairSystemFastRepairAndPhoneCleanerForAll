package com.cleaner.booster.phone.repairer.app.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cleaner.booster.phone.repairer.app.utils.Drawing;


public class DrawingAct extends AppCompatActivity {
    Drawing drawingClass ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        drawingClass = new Drawing( this,null );
        setContentView( drawingClass );

    }
}
