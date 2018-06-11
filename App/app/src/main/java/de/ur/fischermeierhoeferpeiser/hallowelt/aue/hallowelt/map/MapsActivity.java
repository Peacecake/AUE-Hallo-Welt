package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.map;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.MainActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.camera.CameraActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.camera.FakeCameraActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.User;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.posts.PostsActivity;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link android.Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class MapsActivity extends HelloWorldActivity
        implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int CAMERA_REQ = 2;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private Map<Marker, de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location> markerMap;
    private Map<Circle, de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location> circleMap;
    private de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location activatedLocation;
    private FloatingActionButton fab;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        setContentView(R.layout.activity_maps);
        setTitle(getString(R.string.overview));
        markerMap = new HashMap<>();
        circleMap = new HashMap<>();
        activatedLocation = null;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent cameraScannerIntent = new Intent(MapsActivity.this, FakeCameraActivity.class);
                startActivity(cameraScannerIntent);*/

                // This fixed the firebase redirect problem: Open camera directly, if camera gets closed onActivityResult is called
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQ);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (auth.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setLoading(false, fab);
        if (requestCode == CAMERA_REQ && resultCode ==RESULT_OK) {
            if (activatedLocation != null) {
                db.checkInUser(user, activatedLocation);
            } else {
                Toast.makeText(this, "Bitte wählen Sie zuerst einen Marker aus!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (setLoginProtected()) {
            db.getUser(auth.getUser().getUid());
        }
        initLoader(R.id.mapsLoader);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocations();
    }

    @Override
    protected void onUserCheckedIn(User user) {
        super.onUserCheckedIn(user);
        Intent postIntent = new Intent(this, PostsActivity.class);
        postIntent.putExtra("locationId", user.getCurrentLocation().getId());
        startActivity(postIntent);
    }

    @Override
    protected void onUserRetrieved(User user) {
        super.onUserRetrieved(user);
        this.user = user;
    }

    @Override
    protected void onDatabaseError(String errorMessage) {
        super.onDatabaseError(errorMessage);
        setLoading(false, fab);
    }

    @Override
    protected void onAllLocationsRetrieved(ArrayList<de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location> locations) {
        super.onAllLocationsRetrieved(locations);
        for(de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location location : locations) {
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            Marker m = mMap.addMarker(new MarkerOptions().position(position).title(location.getName()));
            Circle c = mMap.addCircle(new CircleOptions()
                    .center(position)
                    .radius(100)
                    .strokeColor(0x9C004B));
            markerMap.put(m, location);
            circleMap.put(c, location);

        }
        setLoading(false, fab);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        loadLocations();

        mMap = map;
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(49.0134297,12.1016236) , 11.0f) );

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        /*LatLng westbad = new LatLng(49.024540, 12.054830);
        LatLng neupfarrplatz = new LatLng(49.018618, 12.096435);
        LatLng steinerneBruecke = new LatLng(49.021974, 12.097153);

        mMap.addMarker(new MarkerOptions().position(westbad).title("Westbad").snippet("Beliebtes Schwimmbad in Regensburg"));
        mMap.addMarker(new MarkerOptions().position(neupfarrplatz).title("Neupfarrplatz").snippet("Zentraler Treffpunkt in der Innenstadt"));
        mMap.addMarker(new MarkerOptions().position(steinerneBruecke).title("Steinerne Brücke").snippet("Historische Brücke über die Donau"));

        Circle circleWestbad = map.addCircle(new CircleOptions()
                .center(westbad)
                .radius(100)
                .strokeColor(Color.RED));

        Circle circleNeupfarrplatz = map.addCircle(new CircleOptions()
                .center(neupfarrplatz)
                .radius(100)
                .strokeColor(Color.RED));

        Circle circleSteinerneBruecke = map.addCircle(new CircleOptions()
                .center(steinerneBruecke)
                .radius(100)
                .strokeColor(Color.RED));*/

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    private void loadLocations() {
        if (db != null) {
            setLoading(true, fab);
            db.getAllLocations();
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        activatedLocation = markerMap.get(marker);
        return false;
    }
}
