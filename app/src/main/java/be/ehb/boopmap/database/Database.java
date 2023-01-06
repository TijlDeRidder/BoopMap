package be.ehb.boopmap.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import be.ehb.boopmap.data.Pin;

public class Database {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Pin pin;

    public Database() {
    }

    public Task<Void> addPin(Pin pin){
        return db.collection("Pins").document().set(pin);
    }
}
