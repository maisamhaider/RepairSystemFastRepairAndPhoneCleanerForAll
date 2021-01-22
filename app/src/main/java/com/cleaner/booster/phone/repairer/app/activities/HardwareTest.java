package com.cleaner.booster.phone.repairer.app.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cleaner.booster.phone.repairer.app.R;

import java.util.Locale;


public class HardwareTest extends AppCompatActivity implements View.OnClickListener {

    TextView simCardVal_tv, headphone_tv, bluetoothSate_tv,speakerIsW_tv,vibration_tv;
    TextToSpeech txtSpeech;

    int counter = 0;
    boolean isHeadphoneCtd;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_hardware_test);
         setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        ConstraintLayout vibration_ll, simCard_ll, displayTst_ll, tuchSensor_ll, speakerTst_ll, checkHdPhne_ll, checkBlotoooth_ll;
        simCardVal_tv = findViewById( R.id.simCrdTstVal_tv);
        headphone_tv = findViewById( R.id.isHdphne_tv);
        bluetoothSate_tv = findViewById( R.id.blotooothState_tv);
        speakerIsW_tv = findViewById( R.id.speakerIsW_tv);
        vibration_tv = findViewById( R.id.vibration_tv);

        vibration_ll = findViewById( R.id.vibration_ll);
        simCard_ll = findViewById( R.id.simCard_ll);
        displayTst_ll = findViewById( R.id.displayTst_ll);
        tuchSensor_ll = findViewById( R.id.tuchSnsor_ll);
        speakerTst_ll = findViewById( R.id.speakerTst_ll);
        checkHdPhne_ll = findViewById( R.id.checkHeadPhne_ll);
        checkBlotoooth_ll = findViewById( R.id.checkBlotoooth_ll);

        vibration_ll.setOnClickListener( this );
        simCard_ll.setOnClickListener( this );
        displayTst_ll.setOnClickListener( this );
        speakerTst_ll.setOnClickListener( this );
        tuchSensor_ll.setOnClickListener( this );
        checkHdPhne_ll.setOnClickListener( this );
        checkBlotoooth_ll.setOnClickListener( this );

        txtSpeech = new TextToSpeech( getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    txtSpeech.setLanguage( Locale.US );
                }
            }
        } );


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vibration_ll:
                Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vb.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    vibration_tv.setVisibility( View.VISIBLE );
                    new Handler(Looper.getMainLooper()).postDelayed( () -> vibration_tv.setVisibility( View.GONE ), 2000 );
                } else {
                    //deprecated in API 26
                    vb.vibrate(500);}
                vibration_tv.setVisibility( View.VISIBLE );
                new Handler(Looper.getMainLooper()).postDelayed( () -> vibration_tv.setVisibility( View.GONE ), 2000 );
                break;

            case R.id.simCard_ll:
                TelephonyManager telMgr = (TelephonyManager) getSystemService( Context.TELEPHONY_SERVICE );
                int simState = telMgr.getSimState();
                switch (simState) {
                    case TelephonyManager.SIM_STATE_ABSENT:
                        simCardVal_tv.setVisibility( View.VISIBLE );
                        simCardVal_tv.setText( "Sim card is absent" );
                        new Handler(Looper.getMainLooper()).postDelayed( () -> simCardVal_tv.setVisibility( View.GONE ), 2000 );
                        break;
                    case TelephonyManager.SIM_STATE_READY:
                        simCardVal_tv.setVisibility( View.VISIBLE );
                        simCardVal_tv.setText( "sim card is inserted" );
                        new Handler(Looper.getMainLooper()).postDelayed( () -> simCardVal_tv.setVisibility( View.GONE ), 2000 );
                        break;
                    case TelephonyManager.SIM_STATE_UNKNOWN:
                        simCardVal_tv.setVisibility( View.VISIBLE );
                        simCardVal_tv.setText( "sim card unknown" );
                        new Handler(Looper.getMainLooper()).postDelayed( () -> simCardVal_tv.setVisibility( View.GONE ), 2000 );
                        break;
                }

                break;
            case R.id.displayTst_ll:
                Dialog dDialog = new Dialog( this, android.R.style.Theme_Light );
                dDialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                View view = LayoutInflater.from( this ).inflate( R.layout.custom_test_dialog_layout, null );
                dDialog.setContentView( view );
                ConstraintLayout cL = view.findViewById( R.id.displayDialog_cl);
                cL.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (counter == 0) {
                            cL.setBackgroundColor( Color.parseColor( "#7CFC00" ) );
                        } else if (counter == 1) {
                            cL.setBackgroundColor( Color.parseColor( "#ff0000" ) );

                        } else if (counter == 2) {
                            cL.setBackgroundColor( Color.parseColor( "#3346FF" ) );

                        }
                        if (counter >= 3) {
                            counter = 0;
                            dDialog.dismiss();
                        }
                        counter++;
                    }
                } );
                dDialog.show();
                break;

            case R.id.tuchSnsor_ll:
                    Intent intent = new Intent( HardwareTest.this, DrawingAct.class );
                    startActivity( intent );

                break;
            case R.id.checkHeadPhne_ll:

                IntentFilter iFilter = new IntentFilter( Intent.ACTION_HEADSET_PLUG );
                getApplicationContext().registerReceiver( receiver, iFilter );
                if (isHeadphoneCtd) {
                    headphone_tv.setVisibility( View.VISIBLE );
                    headphone_tv.setText( "Headphone is plugged" );
                    new Handler(Looper.getMainLooper()).postDelayed( () -> headphone_tv.setVisibility( View.GONE ), 2000 );
                } else {
                    headphone_tv.setVisibility( View.VISIBLE );
                    headphone_tv.setText( "Headphone is unplugged" );
                    new Handler(Looper.getMainLooper()).postDelayed( () -> headphone_tv.setVisibility( View.GONE ), 2000 );
                }

                break;
            case R.id.speakerTst_ll:
                txtSpeech.speak( "speaker is working.", TextToSpeech.QUEUE_FLUSH, null ,null);
                if(txtSpeech.isSpeaking()) {
                    speakerIsW_tv.setVisibility( View.VISIBLE );
                } else {
                    vibration_tv.setVisibility( View.GONE );
                }
                new Handler(Looper.getMainLooper()).postDelayed( () -> {
                    vibration_tv.setVisibility(View.GONE);
                }, 2000 );
                break;
            case R.id.checkBlotoooth_ll:
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    bluetoothSate_tv.setVisibility( View.VISIBLE );
                    bluetoothSate_tv.setText( "device does not have bluetooth" );
                    new Handler(Looper.getMainLooper()).postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            bluetoothSate_tv.setVisibility( View.GONE );
                        }
                    }, 2000 );
                } else if (bluetoothAdapter.isEnabled()) {
                    bluetoothSate_tv.setVisibility( View.VISIBLE );
                    bluetoothSate_tv.setText( "Bluetooth is on" );
                    new Handler(Looper.getMainLooper()).postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            bluetoothSate_tv.setVisibility( View.GONE );
                        }
                    }, 2000 );
                } else {
                    bluetoothSate_tv.setVisibility( View.VISIBLE );
                    bluetoothSate_tv.setText( "Bluetooth is off" );
                    new Handler(Looper.getMainLooper()).postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            bluetoothSate_tv.setVisibility( View.GONE );
                        }
                    }, 2000 );
                }

                break;


        }

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int isConnected = intent.getIntExtra( "state", -1 );
            if (isConnected == 0)
            {
                isHeadphoneCtd = false;
            }
            else if (isConnected ==1)
            {
                isHeadphoneCtd = true;
            }
            else
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        txtSpeech.stop();
    }
}
