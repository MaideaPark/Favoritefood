package favorite.favoritefood.favoritefood;

/**
 * Created by macintosh on 2016. 9. 4..
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddActivity extends NMapActivity{
    private static final String LOG_TAG = "LOG_TAG";
    private static final boolean DEBUG = false;
    private Button addBtn;
    private EditText address,store,comment;
    //정보 내용
    private NMapView nMapView;
    private String clientID = "VrUXt0rZk_RvaIPxuoZg";
    private NMapController nMapController;
    //지도 출력
    private NMapLocationManager nMapLocationManager;
    //나의 위치
    private NMapViewerResourceProvider nMapViewerResourceProvider = null;
    private NMapOverlayManager nOverlayManager;
    //overlay
    private NGeoPoint point = new NGeoPoint(126.978371, 37.5666091);
    //위치 값
    private MainActivity main;
    private SQLiteDatabase db;
    private String str=null;
    private Geocoder geocoder;
    //주소 상세화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        main = new MainActivity();
        nMapView = (NMapView)findViewById(R.id.map_add);

        nMapView.setClientId(clientID);
        nMapView.setClickable(true);
        nMapView.setEnabled(true);
        nMapView.setFocusable(true);
        nMapView.setFocusableInTouchMode(true);
        nMapView.requestFocus();
        super.setMapDataProviderListener(onDataProviderListener);
        nMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        nMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        nMapController = nMapView.getMapController();
        nMapController.setMapCenter(point, 14);
        super.setHighestZoomLevelEnabled(true);


        nMapLocationManager = new NMapLocationManager(this);
        nMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        nMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        point = new NGeoPoint(1,1);

        address = (EditText)findViewById(R.id.address);
        store = (EditText)findViewById(R.id.store);
        comment = (EditText)findViewById(R.id.comment);
        addBtn = (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database Mydb =new Database(getApplicationContext());
                db=Mydb.getWritableDatabase();
                Mydb.insert(address.getText().toString(),store.getText().toString(),comment.getText().toString());
                finish();
            }
        });
    }
    private void overlay(NGeoPoint point){
        nOverlayManager = new NMapOverlayManager(this, nMapView, nMapViewerResourceProvider);
        int markerId = NMapPOIflagType.PIN;
        nMapController.setMapCenter(point,11);
        Toast.makeText(getApplicationContext(),"2"+nMapController.getZoomLevel(),Toast.LENGTH_SHORT).show();
        NMapPOIdata poiData = new NMapPOIdata(1, nMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(point.getLongitude(),point.getLatitude(), "위치1", markerId, 0);
        poiData.endPOIdata();
        NMapPOIdataOverlay poiDataOverlay = nOverlayManager.createPOIdataOverlay(poiData, null);

        poiDataOverlay.showAllPOIdata(1);
    }
    private void startMyLocation() {

        boolean isMyLocationEnabled = nMapLocationManager.enableMyLocation(true);
        Toast.makeText(getApplicationContext(),"1"+nMapController.getZoomLevel(),Toast.LENGTH_SHORT).show();
        //overlay(point);
        if (!isMyLocationEnabled) {
            Toast.makeText(getApplicationContext(), "Please enable a My Location source in system settings", Toast.LENGTH_LONG).show();
            Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(goToSettings);
            finish();
        } else {
        }
    }
    private void stopMyLocation() {
        nMapLocationManager.disableMyLocation();

    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {
        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
            if (errInfo != null) {
                Log.e("myLog", "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());
                Toast.makeText(getApplicationContext(), errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            }else{
                NGeoPoint location = nMapLocationManager.getMyLocation();
                // Toast.makeText(getApplicationContext(),"3"+nMapController.getZoomLevel(),Toast.LENGTH_SHORT).show();
                nMapController.setMapCenter(location,14);
                geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);
                List<Address> adr = null;
                try {
                    adr = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    str = adr.get(0).getAddressLine(0).toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address.setText(str);
                overlay(location);
                stopMyLocation();

            }
        }
    };

    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {
        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
            if (errorInfo == null) { // success
                // restore map view state such as map center position and zoom level.
                //restoreInstanceState();
                //nMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 14);
                Toast.makeText(getApplicationContext(),"4"+nMapController.getZoomLevel(),Toast.LENGTH_SHORT).show();
                startMyLocation();

            } else { // fail
                Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());
                Toast.makeText(getApplication(), errorInfo.toString(), Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
            }
        }
        @Override
        public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
            }
        }
        @Override
        public void onZoomLevelChange(NMapView mapView, int level) {
            if (DEBUG) {
                Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
            }
        }
        @Override
        public void onMapCenterChangeFine(NMapView mapView) {
        }
    };
    private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {
        @Override
        public void onLongPress(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onLongPressCanceled(NMapView mapView) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onTouchDown(NMapView mapView, MotionEvent ev) {
        }
        @Override
        public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
        }
        @Override
        public void onTouchUp(NMapView mapView, MotionEvent ev) {
            // TODO Auto-generated method stub
        }
    };
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

            Log.d("myLog", "myLocation  lat " + myLocation.getLatitude());
            Log.d("myLog", "myLocation  lng " + myLocation.getLongitude());
            Toast.makeText(getApplicationContext(),"5"+nMapController.getZoomLevel(),Toast.LENGTH_SHORT).show();
            findPlacemarkAtLocation(myLocation.getLongitude(), myLocation.getLatitude());

            //위도경도를 주소로 변환
            return true;
        }
        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

            Toast.makeText(getApplicationContext(), "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
            Toast.makeText(getApplicationContext(), "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
            stopMyLocation();
        }
    };



}
