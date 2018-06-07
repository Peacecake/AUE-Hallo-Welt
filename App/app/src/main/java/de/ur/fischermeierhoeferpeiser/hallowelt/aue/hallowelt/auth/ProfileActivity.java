package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.AuthentificationResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.DatabaseResult;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.User;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;

import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_ALL_LOCATIONS;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_LOCATION;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_GET_USER;
import static de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.FirebaseResult.DB_USER_CHECK_IN;

public class ProfileActivity extends HelloWorldActivity {
    private Button btnLogout;
    private TextView tvUsername;
    private TextView tvEmail;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initLoader(R.id.profileLoader);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLoginProtected();
        setLoading(true);
        db.getUser(auth.getUser().getUid());
    }

    private void updateUserProfile() {
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
    }

    private void initUi() {
        setContentView(R.layout.activity_profile);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvUsername = findViewById(R.id.tvProfileUsername);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });
    }


    @Override
    protected void onUserRetrieved(User user) {
        super.onUserRetrieved(user);
        setLoading(false);
        this.user = user;
        updateUserProfile();
    }

    @Override
    protected void onDatabaseError(String errorMessage) {
        super.onDatabaseError(errorMessage);
        setLoading(false);
    }

    @Override
    public void onAuthEvent(AuthentificationResult authentificationResult) {
        super.onAuthEvent(authentificationResult);
    }
}
