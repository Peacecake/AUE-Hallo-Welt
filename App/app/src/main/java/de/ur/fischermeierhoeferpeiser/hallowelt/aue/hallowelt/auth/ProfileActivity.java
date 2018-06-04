package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Authentification;

public class ProfileActivity extends AppCompatActivity {

    private Authentification auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = new Authentification(this);
        user = auth.getUser();
        Toast.makeText(this, "Username: " + user.getDisplayName() + "\nEmail: " + user.getEmail(), Toast.LENGTH_SHORT).show();
    }
}
