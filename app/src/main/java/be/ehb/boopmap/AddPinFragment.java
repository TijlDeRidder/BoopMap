package be.ehb.boopmap;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import be.ehb.boopmap.data.Pin;
import be.ehb.boopmap.database.Database;
import be.ehb.boopmap.databinding.FragmentAddpinBinding;

public class AddPinFragment extends Fragment {
    private FragmentAddpinBinding binding;
    private FirebaseAuth mAuth;
    private Database db = new Database();
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        binding = FragmentAddpinBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText txtTitel = binding.getRoot().findViewById(R.id.txt_pinTitel);
        EditText txtDescription = binding.getRoot().findViewById(R.id.txt_pinDescription);
        Button btnCancel = binding.getRoot().findViewById(R.id.btn_cancel);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddPinFragment.this).navigate(R.id.action_addPinFragment_to_mapFragment);
            }
        });
        Button btnSubmit = binding.getRoot().findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDescription.setBackgroundColor(Color.WHITE);
                txtTitel.setBackgroundColor(Color.WHITE);
                if(!txtTitel.getText().toString().isEmpty() && !txtDescription.getText().toString().isEmpty()){
                    Location location;
                    fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            db.getPinsCount().observe(getViewLifecycleOwner(), integer -> {
                                int tempId = integer.intValue() + 1;
                                Pin pin = new Pin(mAuth.getUid(),location.getLatitude(),location.getLongitude(),txtTitel.getText().toString(), txtDescription.getText().toString(), tempId);
                                db.addPin(pin);
                                NavHostFragment.findNavController(AddPinFragment.this).navigate(R.id.action_addPinFragment_to_mapFragment);
                            });
                        }
                    });
                }else{
                    if(txtTitel.getText().toString().isEmpty()){
                        txtTitel.setBackgroundColor(Color.RED);
                        txtTitel.setHint("This cannot be empty");
                    }
                    if(txtDescription.getText().toString().isEmpty()){
                        txtDescription.setBackgroundColor(Color.RED);

                        txtDescription.setHint("This cannot be empty");
                    }
                }
            }
        });
    }
}
