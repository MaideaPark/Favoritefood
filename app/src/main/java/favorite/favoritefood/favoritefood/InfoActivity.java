package favorite.favoritefood.favoritefood;

/**
 * Created by macintosh on 2016. 9. 4..
 */
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class InfoActivity extends NMapActivity {
    private static final String LOG_TAG = "LOG_TAG";
    private static final boolean DEBUG = false;
    private EditText address, store, comment;
    //정보 내용
    private Button changeBtn;
    private NMapView nMapView;
    private String clientID = "VrUXt0rZk_RvaIPxuoZg";
    private NMapController nMapController;
    //지도 출력
    private NMapViewerResourceProvider nMapViewerResourceProvider = null;
    private NMapOverlayManager nOverlayManager;
    //overlay
    private NGeoPoint point;
    //위치 값
    private SQLiteDatabase db;
    private Geocoder geocoder;
    private Intent intent;
    private String store_str;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        nMapView = (NMapView) findViewById(R.id.map_info);
        nMapView.setClientId(clientID);
        nMapView.setClickable(true);
        nMapView.setEnabled(true);
        nMapView.setFocusable(true);
        nMapView.setFocusableInTouchMode(true);
        nMapView.requestFocus();
        nMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        nMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        nMapController = nMapView.getMapController();
        super.setMapDataProviderListener(onDataProviderListener);
        super.setHighestZoomLevelEnabled(true);
        nMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        nOverlayManager = new NMapOverlayManager(this, nMapView, nMapViewerResourceProvider);
        point = new NGeoPoint();

        address = (EditText) findViewById(R.id.information_address);
        store = (EditText) findViewById(R.id.information_store);
        comment = (EditText) findViewById(R.id.information_comment);
        changeBtn = (Button) findViewById(R.id.change);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeBtn.getText().equals("수정")) {
                    Database Mydb = new Database(getApplicationContext());
                    db = Mydb.getReadableDatabase();
                    id = Mydb.getIDResult(store.getText().toString());
                    address.setClickable(true);
                    address.setFocusable(true);
                    address.setEnabled(true);
                    address.setFocusableInTouchMode(true);
                    store.setClickable(true);
                    store.setFocusable(true);
                    store.setEnabled(true);
                    store.setFocusableInTouchMode(true);
                    comment.setClickable(true);
                    comment.setFocusable(true);
                    comment.setEnabled(true);
                    comment.setFocusableInTouchMode(true);
                    changeBtn.setText("확인");
                } else if (changeBtn.getText().equals("확인")) {
                    Database Mydb = new Database(getApplicationContext());
                    db = Mydb.getWritableDatabase();
                    Mydb.update(address.getText().toString(), store.getText().toString(), comment.getText().toString(), id);
                    address.setClickable(false);
                    address.setFocusable(false);
                    address.setEnabled(false);
                    address.setFocusableInTouchMode(false);
                    store.setClickable(false);
                    store.setFocusable(false);
                    store.setEnabled(false);
                    store.setFocusableInTouchMode(false);
                    comment.setClickable(false);
                    comment.setFocusable(false);
                    comment.setEnabled(false);
                    comment.setFocusableInTouchMode(false);
                    changeBtn.setText("수정");


                }
            }
        });

    }

    private void overlay(NGeoPoint point) {
        nMapController.setMapCenter(point, 14);
        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(1, nMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(point.getLongitude(), point.getLatitude(), "위치1", markerId, 0);
        poiData.endPOIdata();
        NMapPOIdataOverlay poiDataOverlay = nOverlayManager.createPOIdataOverlay(poiData, null);

        poiDataOverlay.showAllPOIdata(1);
    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {
        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
            if (errInfo != null) {
                Log.e("myLog", "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());
                Toast.makeText(getApplicationContext(), errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            } else {
            }
        }
    };

    private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {
        @Override
        public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
            if (errorInfo == null) { // success
                // restore map view state such as map center position and zoom level.
                //restoreInstanceState();
                Database Mydb = new Database(getApplicationContext());
                db = Mydb.getReadableDatabase();

                intent = getIntent();
                store_str = intent.getExtras().getString("store");
                int id = Mydb.getIDResult(store_str);
                System.out.println(store_str);
                String address_input = Mydb.getAddressResult(id);
                System.out.println(address_input);
                String comment_input = Mydb.getCommentResult(id);
                store.setText(store_str);
                address.setText(address_input);
                comment.setText(comment_input);

                geocoder = new Geocoder(getApplicationContext());
                Address addr = null;
                List<Address> adr = null;
                try {
                    adr = geocoder.getFromLocationName(address_input, 1);
                    addr = adr.get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int lat = (int) (addr.getLatitude() * 1E6);
                int lng = (int) (addr.getLongitude() * 1E6);
                //  System.out.println(lat+","+lng);
                point = new NGeoPoint(lng, lat);
                overlay(point);
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
}