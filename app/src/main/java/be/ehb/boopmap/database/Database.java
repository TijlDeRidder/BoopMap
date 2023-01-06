package be.ehb.boopmap.database;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import be.ehb.boopmap.data.Pin;

public class Database {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Pin pin;

    public Database() {
    }

    public Task<Void> addPin(Pin pin){
        return db.collection("Pins").document().set(pin);
    }

    public MutableLiveData<List<Pin>> getAllPinsByUid(String uid){
        MutableLiveData<List<Pin>> pins = new MutableLiveData<>();
        CollectionReference pinsRef = db.collection("Pins");
        pinsRef.whereEqualTo("uid" , uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Pin> pinList = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots.getDocuments()) {
                    Pin pin = documentSnapshot.toObject(Pin.class);
                    pinList.add(pin);
                }
                pins.setValue(pinList);
            }
        });
        return pins;
    }
}
