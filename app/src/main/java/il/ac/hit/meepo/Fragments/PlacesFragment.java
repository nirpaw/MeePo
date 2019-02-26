package il.ac.hit.meepo.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import il.ac.hit.meepo.Adapters.PlacesAdapter;
import il.ac.hit.meepo.Adapters.UsersAdapter;
import il.ac.hit.meepo.Helpers.Function;
import il.ac.hit.meepo.MainActivity;
import il.ac.hit.meepo.Models.Chatlist;
import il.ac.hit.meepo.Models.Place;
import il.ac.hit.meepo.Models.User;
import il.ac.hit.meepo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFragment extends Fragment {

    private RecyclerView recyclerView;

    private DownloadPlaces mTask;
    private UsersAdapter usersAdapter;
    private List<User> mUsers;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;


    private List<Chatlist> usersList;

    List<Place> listOfFoundedPlaces = new ArrayList<>();
    List<Place> listOfVisitedPlaces = new ArrayList<>();


    double userLat;
    double userLng;
    private SupportMapFragment mapFragment;
    MainActivity activity;
    private GoogleMap mMap;


    DatabaseReference mPlaceReference;

    private static final String TAG = "PlacesFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         activity = (MainActivity) getActivity();
        //firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


        userLat = activity.getLat();
        userLng = activity.getLng();
        Log.d(TAG, "Long_Lang: "+ userLng +" "+userLat);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        recyclerView = view.findViewById(R.id.rv_places);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        Log.d(TAG, "onCreateView: Location "+ userLat+ ", "+userLng);

        mPlaceReference = FirebaseDatabase.getInstance().getReference("VisitedPlaces");
        mPlaceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                ) {
                    Place place = snapshot.getValue(Place.class);
                    listOfVisitedPlaces.add(place);
                    Log.d(TAG, "onVisitedPlaces: "+place.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mTask = new DownloadPlaces();
        listOfFoundedPlaces.clear();
        mTask.execute();

        Switch aSwitch = view.findViewById(R.id.demo_switch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userLat = 32.0811212;
                    userLng = 34.7737281;
                    listOfFoundedPlaces.clear();
                    mTask = new DownloadPlaces();
                    mTask.execute();
                    mapFragment = null;
                    updateMapCam();
                }
                else{
                    userLat = activity.getLat();
                    userLng = activity.getLng();
                    listOfFoundedPlaces.clear();
                    mTask = new DownloadPlaces();
                    mTask.execute();
                    mapFragment = null;
                    updateMapCam();
                }
            }
        });
        updateMapCam();
        return view;
    }

    private void updateMapCam(){
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    LatLng targetedLatLng = new LatLng(userLat ,userLng - 0.0015);
                    LatLng realLatLng = new LatLng(userLat ,userLng );
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(targetedLatLng)      // Sets the center of the map to Mountain View
                            .zoom(17)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    googleMap.addMarker(new MarkerOptions().position(realLatLng));
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            Log.d(TAG, "onMapClick: "+ latLng.toString());
                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions().position(latLng));
                            userLat = latLng.latitude;
                            userLng = latLng.longitude;
                            listOfFoundedPlaces.clear();
                            mTask = new DownloadPlaces();
                            mTask.execute();

                        }
                    });
                }
            });
        }

        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
    }


    class DownloadPlaces extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
             String placesUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+userLat+","+userLng  +"&types=bar"+"&radius=500&key=" +getResources().getString(R.string.google_maps_api_key);
            //String placesUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+"32.0811212,34.7737281"+"&types=bar"+"&radius=4000&key=" +getResources().getString(R.string.google_maps_api_key);
            Log.d(TAG, "debugUrl : "+placesUrl);
            String xml = "";
            String urlParameters = "";
            xml = Function.excuteGet(placesUrl , urlParameters);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            if (xml.length() > 0) {
                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        String name = jsonObject.optString("name").toString();
                        String place_id = jsonObject.optString("place_id").toString();
                        String photo_reference = null;
                        try {
                            // get photo reff
                            JSONArray photos = (JSONArray) jsonObject.get("photos");
                            JSONObject photosJSONObject = photos.getJSONObject(0);
                            photo_reference = photosJSONObject.optString("photo_reference").toString();
                        }
                        catch (Exception e){
                            Log.d(TAG, "onPostExecute: " +e.getMessage());
                        }
                        // get lat and lng

                        //                    HashMap<String, Object> hmap = (Geometry)jsonObject.get("geometry");


                        JSONObject Geo = jsonObject.getJSONObject("geometry");
                        JSONObject Location = Geo.getJSONObject("location");
                        double lat = Location.getDouble("lat");
                        double lng = Location.getDouble("lng");
                        Log.d(TAG, "loactionPlace : "+ lng +",  "+ lat);
//                        mPlaceReference = FirebaseDatabase.getInstance().getReference("Places").child(place_id);
                        listOfFoundedPlaces.add(new Place(name, place_id, lat, lng,photo_reference, null));
//                        mPlaceReference.setValue(listOfFoundedPlaces.get(listOfFoundedPlaces.size()-1));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext().getApplicationContext(), "Unexpected error: " +e.toString(), Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, "BEFORE SORT: "+ printPlaces());

                sortListByDistance();
                PlacesAdapter placesAdapter = new PlacesAdapter(listOfFoundedPlaces, getContext());

                placesAdapter.setClickListener(new PlacesAdapter.IPlaceAdapterListener() {
                    @Override
                    public void OnPlaceClicked(int position, View view) {
                        Place pressedPlace = listOfFoundedPlaces.get(position);

                        setUserLastLocation(pressedPlace);

                        boolean placeIsExist = false;
                        for(Place place : listOfVisitedPlaces){
                            if( pressedPlace.getmPlaceId() == place.getmPlaceId()){
                                placeIsExist = true;
                            }
                        }
                        if(!placeIsExist){
                            listOfVisitedPlaces.add(pressedPlace);
                            reference = FirebaseDatabase.getInstance().getReference("VisitedPlaces");
                            reference.child(pressedPlace.getmPlaceId()).setValue(pressedPlace);
                        }

                        InPlaceFragment inPlaceFragment = new InPlaceFragment();

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("currentPlace", pressedPlace);
                        inPlaceFragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.container_frame, inPlaceFragment);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
                recyclerView.setAdapter(placesAdapter);

            } else {
                //TODO: NO PLACES FOUND
                Toast.makeText(getActivity().getApplicationContext(), "No places found", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "AFTER SORT: "+ printPlaces());
        }
    }

    protected void sortListByDistance(){

        Collections.sort(listOfFoundedPlaces, new Comparator<Place>(){
            public int compare(Place p1, Place p2)
            {
                return calcDistance(p1) > calcDistance(p2) ? 1 : -1;
            }
        });
    }


    String printPlaces(){ // FOR DEBUG
        StringBuilder sb = new StringBuilder();
        for (Place item: listOfFoundedPlaces
        ) {
            sb.append(item.getmPlaceName());
            sb.append(" -- dist: " + calcDistance(item));
            sb.append("\n");

        }
        return sb.toString();
    }



    private double calcDistance(Place place) {
        double theta = userLng - place.getmLng();
        double dist = Math.sin(deg2rad(userLat)) * Math.sin(deg2rad(place.getmLat())) + Math.cos(deg2rad(userLat)) * Math.cos(deg2rad(place.getmLat())) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }
    /*::  This function converts decimal degrees to radians             :*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*::  This function converts radians to decimal degrees             :*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void setUserLastLocation(Place pressedPlace){
        try {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("lastLocationPlaceId", pressedPlace.getmPlaceId());
            reference.updateChildren(hashMap);
        }catch (Exception ex){

        }
    }
}
