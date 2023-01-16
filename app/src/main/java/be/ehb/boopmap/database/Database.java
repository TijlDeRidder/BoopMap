package be.ehb.boopmap.database;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
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

    public MutableLiveData<Integer> getPinsCount(){
        MutableLiveData<Integer> count = new MutableLiveData<Integer>();
        CollectionReference collectionReference = db.collection("Pins");
        AggregateQuery countQuery = collectionReference.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                AggregateQuerySnapshot snapshot = task.getResult();
                count.setValue((int) snapshot.getCount());
            }
        });
        return count;
    }

    public void removePinById(int id){
        CollectionReference collectionReference = db.collection("Pins");
        collectionReference.whereEqualTo("id", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        documentSnapshot.getReference().delete();
                    }
                    Log.d(TAG, "Document deleted succesfully!");
                }else{
                    Log.w(TAG, "Document deleted failed", task.getException());
                }
            }
        });
    }
}
