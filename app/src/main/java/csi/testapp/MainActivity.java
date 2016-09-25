package csi.testapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;
import com.skp.Tmap.TMapGpsManager.onLocationChangedCallback;

public class MainActivity extends AppCompatActivity implements onLocationChangedCallback,LocationListener{

    public static String mApiKey = "8bdbb125-7d59-3684-84ff-ad4b5bb59e74";
    private TMapView mMapView = null;
    private TMapPoint n_Locate = null;
    private RelativeLayout mMainRelativeLayout=null;
    TMapGpsManager gps;
    final int READ_ROCATE_CODE = 0;

    private LocationManager locationManager;

    private void configureMapView(){
        mMapView.setSKPMapApiKey(mApiKey);
    }

    void initView(){
        mMapView = new TMapView(this);
        mMapView.setCenterPoint(126.65318, 37.449666);
        mMapView.setLocationPoint(126.65318, 37.449666);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        mMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        mMapView.setIcon(bitmap);
        mMapView.setIconVisibility(true);
        mMapView.setSightVisible(true);
        mMapView.setZoomLevel(17);
        gps=new TMapGpsManager(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainRelativeLayout = (RelativeLayout)findViewById(R.id.mainRelativeLayout);
        initView();
        mMainRelativeLayout.addView(mMapView);
        configureMapView();
        tryCheckPermission();
        findViewById(R.id.button).setOnClickListener(handler);



    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            doAction();
        }

    };

    void settingGPS(){
        gps.setMinTime(1000);
        gps.setMinDistance(5);
        gps.setProvider(gps.GPS_PROVIDER);
        gps.OpenGps();
    };

    void doAction(){
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,10,this);

    };

    @Override
    public void onLocationChange(Location location){

    }

    void tryCheckPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) == PackageManager.PERMISSION_GRANTED){
            settingGPS();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.LOCATION},READ_ROCATE_CODE);
        }
    }

   @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
       super.onRequestPermissionsResult(requestCode,permissions,grantResults);

       switch(requestCode){
           case READ_ROCATE_CODE:
               if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                   settingGPS();
               }
       }
   }


    @Override
    public void onLocationChanged(Location location) {
        double Latitude = location.getLatitude();
        double Longitude = location.getLongitude();
        String msg = "New Latitude: " + Latitude
                + "New Longitude: " + Longitude;

        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
        mMapView.setCenterPoint(Longitude, Latitude);
        mMapView.setLocationPoint(Longitude, Latitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }
}