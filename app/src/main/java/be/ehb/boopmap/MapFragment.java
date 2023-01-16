package be.ehb.boopmap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import be.ehb.boopmap.data.Pin;
import be.ehb.boopmap.database.Database;
import be.ehb.boopmap.databinding.FragmentMapBinding;


public class MapFragment extends Fragment{
    private GoogleMap map;
    private SupportMapFragment supportMapFragment;
    private FragmentMapBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private Database db = new Database();


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMapBinding.inflate(inflater, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton addPin = binding.getRoot().findViewById(R.id.btn_addPin);
        FloatingActionButton seeList = binding.getRoot().findViewById(R.id.btn_seeList);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                db.getAllPinsByUid(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<List<Pin>>() {
                    @Override
                    public void onChanged(List<Pin> pins) {
                        for (Pin pin: pins
                             ) {
                            System.out.println(pins.size() + "test123");
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(pin.getLat(), pin.getLng())).title("Titel: " + pin.getTitel()).snippet("Description: " + pin.getDescription()));
                        }
                    }
                });

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    return;
                }

                googleMap.setMyLocationEnabled(true);
                fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),25));
                    }
                });

                addPin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NavHostFragment.findNavController(MapFragment.this).navigate(R.id.action_mapFragment_to_addPinFragment);
                    }
                });

                seeList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NavHostFragment.findNavController(MapFragment.this).navigate(R.id.action_mapFragment_to_pinListFragment);
                    }
                });
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}