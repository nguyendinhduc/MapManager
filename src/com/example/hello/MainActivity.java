package com.example.hello;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity{
	private GoogleMap mMap;
	private MapManager mapMgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		initViews();
	}

	private void initViews() {
		final MapFragment mapFragment=MapFragment.newInstance();
		getFragmentManager().beginTransaction().replace(R.id.content, mapFragment).commit();		
		mMap=mapFragment.getMap();
		mapFragment.getMapAsync(new OnMapReadyCallback() {
			
			@Override
			public void onMapReady(GoogleMap map) {
				mMap=map;
				mapMgr=new MapManager(mMap, MainActivity.this);
				mapMgr.initMap();
				mapMgr.findMyLocation();
			}
		});
	}
}
