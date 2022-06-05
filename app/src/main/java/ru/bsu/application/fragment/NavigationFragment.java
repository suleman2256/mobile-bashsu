package ru.bsu.application.fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import ru.bsu.application.activity.MainActivity;
import ru.bsu.application.R;
import ru.bsu.application.dto.Constants;

public class NavigationFragment extends Fragment implements UserLocationObjectListener {

    public NavigationFragment() {
    }

    private MapView mapView;
    private UserLocationLayer userLocationLayer;
    private CoordinatorLayout rootCoordinatorLayout;
    private LocationManager locationManager;
    private LocationListener myLocationListener;
    private Point myLocation;

    public static final int COMFORTABLE_ZOOM_LEVEL = 18;
    private static final double DESIRED_ACCURACY = 0;
    private static final long MINIMAL_TIME = 0;
    private static final double MINIMAL_DISTANCE = 50;
    private static final boolean USE_IN_BACKGROUND = true;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private final Point TARGET_LOCATION = new Point(54.735152, 55.958722);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.MapKitInitializer.INSTANCE.initialize(Constants.MAPKIT_API_KEY, getContext());
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        mapView = view.findViewById(R.id.map_navigation);
        mapView.getMap().setRotateGesturesEnabled(false);
        super.onCreate(savedInstanceState);
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 13.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0.5f),
                null);

        requestLocationPermission();

        Button mapButtonZoomPlus = view.findViewById(R.id.buttonZoomPlus);
        mapButtonZoomPlus.setOnClickListener(v -> {
            CameraPosition lCameraPosition = new CameraPosition(
                    mapView.getMapWindow().getMap().getCameraPosition().getTarget(),
                    mapView.getMapWindow().getMap().getCameraPosition().getZoom() + 1, 0f, 0f);
            Animation lAnimation = new Animation(Animation.Type.SMOOTH, 0.3f);
            mapView.getMapWindow().getMap().move(lCameraPosition, lAnimation, null);
        });

        Button mapButtonZoomMinus = view.findViewById(R.id.buttonZoomMinus);
        mapButtonZoomMinus.setOnClickListener(v -> {
            CameraPosition lCameraPosition = new CameraPosition(
                    mapView.getMapWindow().getMap().getCameraPosition().getTarget(),
                    mapView.getMapWindow().getMap().getCameraPosition().getZoom() - 1, 0f, 0f);
            Animation lAnimation = new Animation(Animation.Type.SMOOTH, 0.3f);
            mapView.getMapWindow().getMap().move(lCameraPosition, lAnimation, null);
        });

        MapKit mapKit = MapKitFactory.getInstance();
        locationManager = mapKit.createLocationManager();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);

        myLocationListener = new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                myLocation = location.getPosition();
                Log.w(TAG, "my location - " + myLocation.getLatitude() + "," + myLocation.getLongitude());
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {
                if (locationStatus == LocationStatus.NOT_AVAILABLE) {
                    Snackbar.make(rootCoordinatorLayout, R.string.error_cant_get_my_location, Snackbar.LENGTH_LONG).show();
                }
            }
        };

        Button mapButtonGps = view.findViewById(R.id.buttonGps);
        mapButtonGps.setOnClickListener(v -> {
            checkLocation();
            if (myLocation != null) {
                moveCamera(myLocation, COMFORTABLE_ZOOM_LEVEL);
            }
        });

        return view;
    }

    @SuppressLint("ServiceCast")
    public void checkLocation() {
        android.location.LocationManager locationManager1 = (android.location.LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager1.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {

        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                "android.permission.ACCESS_FINE_LOCATION")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{"android.permission.ACCESS_FINE_LOCATION"},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    private void moveCamera(Point point, float zoom) {
        mapView.getMap().move(
                new CameraPosition(point, zoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null);
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        locationManager.unsubscribe(myLocationListener);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
        locationManager.requestSingleUpdate(myLocationListener);
        subscribeToLocationUpdate();
    }

    private void subscribeToLocationUpdate() {
        if (locationManager != null && myLocationListener != null) {
            locationManager.subscribeForLocationUpdates(DESIRED_ACCURACY, MINIMAL_TIME, MINIMAL_DISTANCE, USE_IN_BACKGROUND, FilteringMode.OFF, myLocationListener);
        }
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.resetAnchor();

        if (myLocation != null) {
            moveCamera(myLocation, COMFORTABLE_ZOOM_LEVEL);
        }

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                getContext(), R.mipmap.icon_arrow));

        userLocationView.getAccuracyCircle().setFillColor(Color.WHITE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }
}