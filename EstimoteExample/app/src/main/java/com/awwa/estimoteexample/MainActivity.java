package com.awwa.estimoteexample;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.List;


public class MainActivity extends Activity {


    private TextView textView;
    private BeaconManager beaconManager;
    private Beacon beacon;
    private Region region;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
    private static final String OUT_FORMAT = "{\"key\":\"KEYKEYKEYKEYKEY\",\"mac\":\"%s\",\"acc\":%.2f}";

    private static boolean mSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.rssi);

//        region = new Region("regionid", "uudi", -1, -1);
//
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> rangedBeacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Just in case if there are multiple beacons with the same uuid, major, minor.
                        Beacon foundBeacon = null;
                        for (Beacon rangedBeacon : rangedBeacons) {
                            if (rangedBeacon.getMacAddress().equals("MAC:MAC:MAC:MAC:MAC:MAC")) {
                                foundBeacon = rangedBeacon;
                            }
                        }
                        if (foundBeacon != null) {
                            //updateDistanceView(foundBeacon);
                            double accuracy = Utils.computeAccuracy(foundBeacon);
                            String out = OUT_FORMAT.format(OUT_FORMAT, foundBeacon.getMacAddress(), accuracy);
                            textView.setText(out);
                            Log.i(TAG, foundBeacon.getMacAddress());

                            if (accuracy < 1.0f) {
                                if (mSent == false) {
                                    PostMessageTask post = new PostMessageTask();
                                    post.execute(out);
                                    mSent = true;
                                }
                            }
                            else {
                                mSent = false;
                            }

                        }
                    }
                });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        Log.i("TAG", "onStart");

//        PostMessageTask post = new PostMessageTask();
//        post.execute("{\"mac\":\"value\",\"acc\":\"678\"}");

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {

            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Toast.makeText(MainActivity.this, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        beaconManager.disconnect();

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
