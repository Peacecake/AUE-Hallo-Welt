package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_ALL_LOCATIONS;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_LOCATION;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_USER;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_USER_CHECK_IN;

public class ProfileActivity extends AppCompatActivity implements FirebaseListener {
    private Button btnLogout;
    private TextView tvUsername;
    private TextView tvEmail;

    private Authentification auth;
    private Database db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        db = Database.getInstance();
        db.setOnFirebaseListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Authentification.getInstance();
        auth.setContext(this);
        user = auth.getUser();
        if (user != null) {
            updateUserProfile();
        } else {
            goToLogin();
        }
    }

    private void updateUserProfile() {
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
    }

    private void goToLogin() {
        Toast.makeText(this, "Go to login", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void initUi() {
        setContentView(R.layout.activity_profile);
        btnLogout = findViewById(R.id.btnLogout);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvUsername = findViewById(R.id.tvProfileUsername);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                goToLogin();
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
                break;
            case DB_USER_CHECK_IN:
                if (databaseResult.wasSuccessful()) {
                    user.checkIn((Location) databaseResult.getDatabaseObject());
                } else {
                    Log.e("ERROR", databaseResult.getErrorMessage());
                }
                break;
            case DB_GET_ALL_LOCATIONS:
                if (databaseResult.wasSuccessful()) {
                    ArrayList<Location> locations = (ArrayList<Location>) databaseResult.getDatabaseObject();
                    Log.e("Success", "Got all locations");
                } else {
                    Log.e("ERROR", databaseResult.getErrorMessage());
                }
        }
    }
}
