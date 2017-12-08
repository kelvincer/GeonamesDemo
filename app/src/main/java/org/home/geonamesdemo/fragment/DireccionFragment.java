package org.home.geonamesdemo.fragment;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.home.geonamesdemo.R;
import org.home.geonamesdemo.listener.UpdateableFragment;
import org.home.geonamesdemo.model.Geoname;
import org.home.geonamesdemo.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kelvin on 5/12/2017.
 */

public class DireccionFragment extends Fragment implements SensorEventListener, UpdateableFragment {

    private static final String TAG = DireccionFragment.class.getSimpleName();
    float currentDegree = 0;
    Location location = new Location("My position");
    Location target;
    SensorManager mSensorManager;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.txv_label_distancia)
    TextView txvLabelDistancia;
    @BindView(R.id.txv_distancia)
    TextView txvDistancia;
    @BindView(R.id.txv_label_grados)
    TextView txvLabelGrados;
    @BindView(R.id.txv_grados)
    TextView txvGrados;
    @BindView(R.id.lnl_grados)
    LinearLayout lnlGrados;
    @BindView(R.id.txv_label_bearing)
    TextView txvLabelBearing;
    @BindView(R.id.txv_bearing)
    TextView txvBearing;
    @BindView(R.id.lnl_bearing)
    LinearLayout lnlBearing;
    @BindView(R.id.igv_arrow)
    ImageView igvArrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Geoname geoname = (Geoname) getArguments().getSerializable(Constants.GEONAME_KEY);

        target = new Location("Target");
        target.setLatitude(Double.parseDouble(geoname.getLat()));
        target.setLongitude(Double.parseDouble(geoname.getLng()));

        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        ButterKnife.bind(this, view);

        ViewCompat.setTranslationZ(progressBar, 2);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this); // to stop the listener and save battery
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);

        GeomagneticField geoField = new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(), 0,
                System.currentTimeMillis());

        degree += geoField.getDeclination();

        float bearing = location.bearingTo(target);
        degree = (bearing - degree) * -1;
        degree = normalizeDegree(degree);

        txvGrados.setText(Integer.toString(Math.round(event.values[0])));
        txvBearing.setText(Double.toString(bearing2(location.getLatitude(), location.getLongitude(), target.getLatitude(), target.getLongitude())));

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);
        // Start the animation
        igvArrow.startAnimation(ra);
        currentDegree = -degree;

        // get the angle around the z-axis rotated

        /*float degree = Math.round(event.values[0]);


        txvGrados.setText("Heading: " + Float.toString(degree) + " degrees");


        // create a rotation animation (reverse turn degree degrees)

        RotateAnimation ra = new RotateAnimation(

                currentDegree,

                -degree,

                Animation.RELATIVE_TO_SELF, 0.5f,

                Animation.RELATIVE_TO_SELF,

                0.5f);


        // how long the animation will take place

        ra.setDuration(210);


        // set the animation after the end of the reservation status

        ra.setFillAfter(true);


        // Start the animation

        igvArrow.startAnimation(ra);

        currentDegree = -degree;*/

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float normalizeDegree(float value) {
        if (value >= 0.0f && value <= 180.0f) {
            return value;
        } else {
            return 360 + value;
        }
    }

    protected static double bearing(double lat1, double lon1, double lat2, double lon2) {

        double longDiff = lon2 - lon1;
        double y = Math.sin(longDiff) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    protected double bearing2(double startLat, double startLng, double endLat, double endLng) {
        double longitude1 = startLng;
        double longitude2 = endLng;
        double latitude1 = Math.toRadians(startLat);
        double latitude2 = Math.toRadians(endLat);
        double longDiff = Math.toRadians(longitude2 - longitude1);
        double y = Math.sin(longDiff) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    @Override
    public void update(Location location) {

        if (progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);
            igvArrow.setVisibility(View.VISIBLE);
        }

        Log.d(TAG, "lat " + location.getLatitude() + " lng:" + location.getLongitude());
        this.location = location;
        double d = location.distanceTo(target);
        txvDistancia.setText(Double.toString(d));
    }
}
