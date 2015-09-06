package com.example.hello;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapManager implements LocationListener {
	private static final long TIME_UPDATE = 3000;
	private static final float DISTANCE_UPDATE = 20;
	private GoogleMap mMap;
	private LocationManager mLocationMgr;
	private Context mContext;
	private PolylineOptions mPolyArrsPath=new PolylineOptions();
	private LatLng oldPosition;

	public MapManager(GoogleMap map, Context context) {
		mMap = map;
		mLocationMgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		mContext = context;
	}

	public void initMap() {
		if (mMap == null)
			return;

		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);
		mMap.getUiSettings().setRotateGesturesEnabled(true);
		mMap.getUiSettings().setAllGesturesEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.setMyLocationEnabled(true);
	}

	public void addMarker(String title, LatLng location) {
		MarkerOptions mark = new MarkerOptions();
		mark.visible(true);
		mark.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
		mark.title(title);
		mark.position(location);
		mMap.clear();
		Marker marker = mMap.addMarker(mark);
		marker.showInfoWindow();
	}

	public void findMyLocation() {
		if (!isGPSOn()) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			mContext.startActivity(intent);
		} else {
			Criteria criteria = new Criteria();
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			String provider = mLocationMgr.getBestProvider(criteria, true);

			Location myLocation = mLocationMgr.getLastKnownLocation(provider);
			if (myLocation != null) {
				LatLng position = new LatLng(myLocation.getLatitude(),
						myLocation.getLongitude());
				oldPosition=position;

				addMarker("My Location", position);
				moveToPosition(position);

				mLocationMgr.requestLocationUpdates(provider, TIME_UPDATE,
						DISTANCE_UPDATE, this);

			}

		}
	}

	public void moveToPosition(LatLng location) {
		CameraPosition.Builder builder = new CameraPosition.Builder();
		builder.target(location);
		builder.zoom(15F);

		CameraPosition camera = builder.build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

	}

	public boolean isGPSOn() {
		return mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	public void drawPath(LatLng newPosition){
		mPolyArrsPath.add(oldPosition).add(newPosition);
		Polyline paths= mMap.addPolyline(mPolyArrsPath);
		paths.setColor(Color.GREEN);
		paths.setVisible(true);
		paths.setWidth(10F);
	}
	@Override
	public void onLocationChanged(Location location) {
		LatLng newPosition = new LatLng(location.getLatitude(),
				location.getLongitude());
		addMarker("My location", newPosition);
		moveToPosition(newPosition);
		drawPath(newPosition);
		oldPosition=newPosition;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}
