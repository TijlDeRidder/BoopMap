package be.ehb.boopmap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import be.ehb.boopmap.adapters.PinListAdapter;
import be.ehb.boopmap.data.Pin;
import be.ehb.boopmap.database.Database;
import be.ehb.boopmap.databinding.FragmentPinListBinding;

public class PinListFragment extends Fragment {
    private FragmentPinListBinding binding;
    private Database db = new Database();

    public PinListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPinListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button btn_Back = binding.getRoot().findViewById(R.id.btn_back);
        db.getAllPinsByUid(FirebaseAuth.getInstance().getUid()).observe(getViewLifecycleOwner(), new Observer<List<Pin>>() {
            @Override
            public void onChanged(List<Pin> pins) {
                displayPin(pins);
            }
        });
        super.onViewCreated(view, savedInstanceState);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PinListFragment.this).navigate(R.id.action_pinListFragment_to_mapFragment);
            }
        });
    }

    private void displayPin(List<Pin> pins){
        PinListAdapter  adapter = new PinListAdapter();
        adapter.addItems(pins);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.pinRecyclerView.setAdapter(adapter);
        binding.pinRecyclerView.setLayoutManager(layoutManager);
    }
}