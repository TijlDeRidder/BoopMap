
package be.ehb.boopmap;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        Button btnLogin = findViewById(R.id.btn_Login);
        Button btnRegister = findViewById(R.id.btn_Register);

        btnLogin.setOnClickListener(view -> {
            authUser();
        });

        btnRegister.setOnClickListener(view -> {
            registerUser();
                    });


    }
    public void authUser() {
        EditText txtEmail;
        EditText txtPass;
        txtEmail = findViewById(R.id.txt_email);
        txtPass = findViewById(R.id.txt_pasw);


        String email = txtEmail.getText().toString();
        String pass = txtPass.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        goToMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    public void registerUser(){

        EditText txtEmail;
        EditText txtPass;
        txtEmail = findViewById(R.id.txt_email);
        txtPass = findViewById(R.id.txt_pasw);

        String email = txtEmail.getText().toString();
        String pass = txtPass.getText().toString();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

        if (!(email.isEmpty())) {
            mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(LoginActivity.this, "You are registered you can Log In", Toast.LENGTH_SHORT).show();
                        System.out.println(mAuth.getCurrentUser().getUid());
//                        User newUser = new User(email,mAuth.getCurrentUser().getUid());
//                       addUserToDb(newUser);
                    }
                }
            });
        }
    }

    public void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}