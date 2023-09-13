package com.example.travelmaker;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.annotation.NonNullApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //LatLng sydney = new LatLng(-34, 151);
           // googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
           // googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            getDatafromFirebase(googleMap);
            //if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
               // return;

            //googleMap.setMyLocationEnabled(true);


        }
    };
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_maps, container, false);
        return view;

    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    void getDatafromFirebase(GoogleMap googleMap){

        firebaseDatabase = FirebaseDatabase.getInstance("");
        databaseReference=firebaseDatabase.getReference("placesinfo");
        List<Places> placesList=new ArrayList<>();
        Places places;
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Map<String,String> map=(Map<String, String>) snapshot.getValue();
                Places places=new Places();
                places.setStreetad(map.get("streetad"));
                places.setState(map.get("state"));
                places.setCountry(map.get("country"));
                places.setImage(map.get("image"));
                placesList.add(places);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setPadding(20,20,20,200);
                LatLng address =null;
                for(int i=0;i<placesList.size();i++)
                {
                    try{
                        String addr=placesList.get(i).getStreetad()+','+
                                placesList.get(i).getState()+','+
                                placesList.get(i).getCountry();
                        address=getlanglogifrmaddress(getActivity(),addr);
                        byte[] bimage= Base64.decode(placesList.get(i).getImage().getBytes(),Base64.DEFAULT);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(bimage,0,bimage.length);
                        googleMap.addMarker(new MarkerOptions().position(address).title(placesList.get(i).getStreetad())).setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(address));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }


        });

    }
    LatLng getlanglogifrmaddress(Context context,String Address){
        Geocoder geocoder=new Geocoder(context);
        List<Address> addresses;
        LatLng latLng=null;
        try {
            addresses=geocoder.getFromLocationName(Address,2);
            if(addresses==null)
            {
                return null ;
            }
            Address loc=addresses.get(0);
           latLng=new LatLng(loc.getLatitude(), loc.getLongitude());

        }
        catch (Exception e)
        {

        }
        return latLng;

    }
}