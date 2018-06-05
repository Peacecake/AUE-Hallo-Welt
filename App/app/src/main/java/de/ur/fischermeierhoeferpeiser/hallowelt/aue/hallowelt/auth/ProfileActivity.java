package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.MainActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Authentification;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Database;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.DatabaseResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseListener;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Post;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.User;

import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_LOCATION;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_USER;

public class ProfileActivity extends AppCompatActivity implements FirebaseListener {
    private Button btnLogout;

    private Authentification auth;
    private Database db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        db = new Database();
        db.setOnFirebaseListener(this);
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
                Post p = new Post("Meine Überschrift", "Das ist ein Test Post", user.getUsername());
                Location l = new Location(42.0012, 11.4938, "TestLocation");
                //db.addLocation(l);
                /*db.addPost(l.getId(), p, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(ProfileActivity.this, "AddedPost", Toast.LENGTH_SHORT).show();
                    }
                });*/

                //db.getLocation(l.getId());
                db.getUser(user.getId());
            }
        });
    }

    @Override
    public void onAuthEvent(AuthentificationResult authentificationResult) {

    }

    @Override
    public void onDatabaseEvent(DatabaseResult databaseResult) {
        switch (databaseResult.getType()) {
            case DB_GET_LOCATION:
                if (databaseResult.wasSuccessful()) {
                    Location location = (Location) databaseResult.getDatabaseObject();
                    Log.e("SUCCESS", "SUCCESS");
                } else {
                    Log.e("ERROR", databaseResult.getErrorMessage());
                }
                break;
            case DB_GET_USER:
                if (databaseResult.wasSuccessful()) {
                    User u = (User) databaseResult.getDatabaseObject();
                    Log.e("GET_USER", "SUCCESS");
                } else {
                    Log.e("ERROR", databaseResult.getErrorMessage());
                }
        }
    }
}
