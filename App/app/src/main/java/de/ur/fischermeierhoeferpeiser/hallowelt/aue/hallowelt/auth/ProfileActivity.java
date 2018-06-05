package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.MainActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Authentification;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Database;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Post;
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

        Button btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = new Database();
                Post p = new Post("Meine Überschrift", "Das ist ein Test Post", user.getUsername());
                Location l = new Location(42.0012, 11.4938, "TestLocation");
                //db.addLocation(l);
                db.addPost(l.getId(), p, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(ProfileActivity.this, "AddedPost", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
