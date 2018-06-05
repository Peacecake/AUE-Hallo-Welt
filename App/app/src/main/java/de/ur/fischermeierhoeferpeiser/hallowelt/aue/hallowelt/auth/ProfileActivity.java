package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.MainActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Authentification;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.User;

public class ProfileActivity extends AppCompatActivity {
    private Button btnLogout;

    private Authentification auth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = new Authentification(this);
        user = auth.getUser();
        if (user != null) {
            Toast.makeText(this, "Username: " + user.getUsername() + "\nEmail: " + user.getEmail(), Toast.LENGTH_SHORT).show();
        } else {
            goToLogin();
        }
    }

    private void goToLogin() {
        Toast.makeText(this, "Go to login", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void initUi() {
        setContentView(R.layout.activity_profile);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                goToLogin();
            }
        });
    }
}
