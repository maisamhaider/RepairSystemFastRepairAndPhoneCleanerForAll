package com.cleaner.booster.phone.repairer.app.activities;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.utils.speed_test.DownloadTest;
import com.cleaner.booster.phone.repairer.app.utils.speed_test.PingTest;
import com.cleaner.booster.phone.repairer.app.utils.speed_test.SpeedTestHandler;
import com.cleaner.booster.phone.repairer.app.utils.speed_test.UploadTest;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class InternetSpeedAct extends AppCompatActivity {
    static int position = 0;
    static int lastPosition = 0;
    SpeedTestHandler speedTestHandler = null;
    HashSet<String> tempBlackList;
    MainActivity mainActivity;

    @Override
    public void onResume() {
        super.onResume();
        speedTestHandler = new SpeedTestHandler();
        speedTestHandler.start();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_internet_speed );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        mainActivity = new MainActivity();

        final TextView startButton = findViewById( R.id.startButton );
        final DecimalFormat dec = new DecimalFormat( "#.##" );
        startButton.setText( "Begin Test" );

        tempBlackList = new HashSet<>();

        speedTestHandler = new SpeedTestHandler();
        speedTestHandler.start();

        startButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {

                startButton.setEnabled( false );

                //Restart test icin eger baglanti koparsa
                if (speedTestHandler == null) {
                    speedTestHandler = new SpeedTestHandler();
                    speedTestHandler.start();
                }

                new Thread(new Runnable() {
                    RotateAnimation rotate;
                    ImageView barImageView = findViewById( R.id.barImageView );
                    TextView downloadTextView =  findViewById( R.id.downloadTextView );
                    TextView uploadTextView = findViewById( R.id.uploadTextView );

                    @Override
                    public void run() {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                startButton.setText( "Selecting best server based on ping..." );
                            }
                        } );

                        //Get egcodes.speedtest hosts
                        int timeCount = 600; //1min
                        while (!speedTestHandler.isDone()) {
                            timeCount--;
                            try {
                                Thread.sleep( 100 );
                            } catch (InterruptedException e) {
                            }
                            if (timeCount <= 0) {
                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText( getApplicationContext(), "No Connection...", Toast.LENGTH_LONG ).show();
                                        startButton.setEnabled( true );
                                        startButton.setTextSize( 16 );
                                        startButton.setText( "Restart Test" );
                                    }
                                } );
                                speedTestHandler = null;
                                return;
                            }
                        }

                        //Find closest server
                        HashMap<Integer, String> mapKey = speedTestHandler.getmKey();
                        HashMap<Integer, List<String>> mapValue = speedTestHandler.getmValue();
                        double selfLat = speedTestHandler.getsLat();
                        double selfLon = speedTestHandler.getsLon();
                        double tmp = 19349458;
                        double dist = 0.0;
                        int findServerIndex = 0;
                        for (int index : mapKey.keySet()) {
                            if (tempBlackList.contains( mapValue.get( index ).get( 5 ) )) {
                                continue;
                            }

                            Location source = new Location( "Source" );
                            source.setLatitude( selfLat );
                            source.setLongitude( selfLon );

                            List<String> ls = mapValue.get( index );
                            Location dest = new Location( "Dest" );
                            dest.setLatitude( Double.parseDouble( ls.get( 0 ) ) );
                            dest.setLongitude( Double.parseDouble( ls.get( 1 ) ) );

                            double distance = source.distanceTo( dest );
                            if (tmp > distance) {
                                tmp = distance;
                                dist = distance;
                                findServerIndex = index;
                            }
                        }
                        String uploadAddr = mapKey.get( findServerIndex );
                        final List<String> info = mapValue.get( findServerIndex );
                        final double distance = dist;

                        if (info == null) {
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    startButton.setTextSize( 12 );
                                    startButton.setText( "There was a problem in getting Host Location. Try again later." );
                                }
                            } );
                            return;
                        }

                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                startButton.setTextSize( 13 );
                                startButton.setText( String.format( "Host Location: %s [Distance: %s km]", info.get( 2 ), new DecimalFormat( "#.##" ).format( distance / 1000 ) ) );
                            }
                        } );
                        
                        //Reset value, graphics
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
//                                pingTextView.setText("0 ms");
//                                chartPing.removeAllViews();
                                downloadTextView.setText( "0 Mbps" );
//                                chartDownload.removeAllViews();
                                uploadTextView.setText( "0 Mbps" );
//                                chartUpload.removeAllViews();
                            }
                        } );
                        final List<Double> pingRateList = new ArrayList<>();
                        final List<Double> downloadRateList = new ArrayList<>();
                        final List<Double> uploadRateList = new ArrayList<>();
                        boolean pingTestStarted = false;
                        boolean pingTestFinished = false;
                        boolean downloadTestStarted = false;
                        boolean downloadTestFinished = false;
                        boolean uploadTestStarted = false;
                        boolean uploadTestFinished = false;

                        //Init Test
                        final PingTest pingTest = new PingTest( info.get( 6 ).replace( ":8080", "" ), 6 );
                        final DownloadTest downloadTest = new DownloadTest( uploadAddr.replace( uploadAddr.split( "/" )[uploadAddr.split( "/" ).length - 1], "" ) );
                        final UploadTest uploadTest = new UploadTest( uploadAddr );


                        //Tests
                        while (true) {
                            if (!pingTestStarted) {
                                pingTest.start();
                                pingTestStarted = true;
                            }
                            if (pingTestFinished && !downloadTestStarted) {
                                downloadTest.start();
                                downloadTestStarted = true;
                            }
                            if (downloadTestFinished && !uploadTestStarted) {
                                uploadTest.start();
                                uploadTestStarted = true;
                            }


                            //Ping Test
                            if (pingTestFinished) {
                                //Failure
                                if (pingTest.getAvgRtt() == 0) {
                                    System.out.println( "Ping error..." );
                                } else {
                                    //Success
                                    runOnUiThread( new Runnable() {
                                        @Override
                                        public void run() {
//                                            pingTextView.setText(dec.format(pingTest.getAvgRtt()) + " ms");
                                        }
                                    } );
                                }
                            } else {
                                pingRateList.add( pingTest.getInstantRtt() );

                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
//                                        pingTextView.setText(dec.format(pingTest.getInstantRtt()) + " ms");
                                    }
                                } );

                                //Update chart
                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        // Creating an  XYSeries for Income
                                        XYSeries pingSeries = new XYSeries( "" );
                                        pingSeries.setTitle( "" );

                                        int count = 0;
                                        List<Double> tmpLs = new ArrayList<>( pingRateList );
                                        for (Double val : tmpLs) {
                                            pingSeries.add( count++, val );
                                        }

                                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                        dataset.addSeries( pingSeries );
//
//                                        GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, \);
//                                        chartPing.addView(chartView, 0);

                                    }
                                } );
                            }


                            //Download Test
                            if (pingTestFinished) {
                                if (downloadTestFinished) {
                                    //Failure
                                    if (downloadTest.getFinalDownloadRate() == 0) {
                                        System.out.println( "Download error..." );
                                    } else {
                                        //Success
                                        runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                downloadTextView.setText( dec.format( downloadTest.getFinalDownloadRate() ) + " Mbps" );
                                            }
                                        } );
                                    }
                                } else {
                                    //Calc position
                                    double downloadRate = downloadTest.getDownloadRate();
                                    downloadRateList.add( downloadRate );
                                    position = getPositionByRate( downloadRate );

                                    runOnUiThread( new Runnable() {

                                        @Override
                                        public void run() {
                                            rotate = new RotateAnimation( lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
                                            rotate.setInterpolator( new LinearInterpolator() );
                                            rotate.setDuration( 100 );
                                            barImageView.startAnimation( rotate );
                                            downloadTextView.setText( dec.format( downloadTest.getDownloadRate() ) + " Mbps" );

                                        }

                                    } );
                                    lastPosition = position;

                                    //Update chart
                                    runOnUiThread( new Runnable() {
                                        @Override
                                        public void run() {
                                            // Creating an  XYSeries for Income
                                            XYSeries downloadSeries = new XYSeries( "" );
                                            downloadSeries.setTitle( "" );

                                            List<Double> tmpLs = new ArrayList<>( downloadRateList );
                                            int count = 0;
                                            for (Double val : tmpLs) {
                                                downloadSeries.add( count++, val );
                                            }

                                            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                            dataset.addSeries( downloadSeries );
//
//                                            GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiDownloadRenderer);
//                                            chartDownload.addView(chartView, 0);
                                        }
                                    } );

                                }
                            }


                            //Upload Test
                            if (downloadTestFinished) {
                                if (uploadTestFinished) {
                                    //Failure
                                    if (uploadTest.getFinalUploadRate() == 0) {
                                        System.out.println( "Upload error..." );
                                    } else {
                                        //Success
                                        runOnUiThread( new Runnable() {
                                            @Override
                                            public void run() {
                                                uploadTextView.setText( dec.format( uploadTest.getFinalUploadRate() ) + " Mbps" );
                                            }
                                        } );
                                    }
                                } else {
                                    //Calc position
                                    double uploadRate = uploadTest.getUploadRate();
                                    uploadRateList.add( uploadRate );
                                    position = getPositionByRate( uploadRate );

                                    runOnUiThread( new Runnable() {

                                        @Override
                                        public void run() {
                                            rotate = new RotateAnimation( lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
                                            rotate.setInterpolator( new LinearInterpolator() );
                                            rotate.setDuration( 100 );
                                            barImageView.startAnimation( rotate );
                                            uploadTextView.setText( dec.format( uploadTest.getUploadRate() ) + " Mbps" );
                                        }

                                    } );
                                    lastPosition = position;

                                    //Update chart
                                    runOnUiThread( new Runnable() {
                                        @Override
                                        public void run() {
                                            // Creating an  XYSeries for Income
                                            XYSeries uploadSeries = new XYSeries( "" );
                                            uploadSeries.setTitle( "" );

                                            int count = 0;
                                            List<Double> tmpLs = new ArrayList<>( uploadRateList );
                                            for (Double val : tmpLs) {
                                                if (count == 0) {
                                                    val = 0.0;
                                                }
                                                uploadSeries.add( count++, val );
                                            }

                                            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                            dataset.addSeries( uploadSeries );

//                                            GraphicalView chartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiUploadRenderer);
//                                            chartUpload.addView(chartView, 0);
                                        }
                                    } );

                                }
                            }

                            //Test bitti
                            if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                                break;
                            }

                            if (pingTest.isFinished()) {
                                pingTestFinished = true;
                            }
                            if (downloadTest.isFinished()) {
                                downloadTestFinished = true;
                            }
                            if (uploadTest.isFinished()) {
                                uploadTestFinished = true;
                            }

                            if (pingTestStarted && !pingTestFinished) {
                                try {
                                    Thread.sleep( 300 );
                                } catch (InterruptedException e) {
                                }
                            } else {
                                try {
                                    Thread.sleep( 100 );
                                } catch (InterruptedException e) {
                                }
                            }
                        }

                        //Thread bitiminde button yeniden aktif ediliyor
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                startButton.setEnabled( true );
                                startButton.setTextSize( 16 );
                                startButton.setText( "Restart Test" );
                            }
                        } );


                    }
                } ).start();
            }
        } );
    }


    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }
}