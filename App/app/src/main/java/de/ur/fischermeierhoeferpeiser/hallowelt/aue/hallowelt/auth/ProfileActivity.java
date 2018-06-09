package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.User;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;

public class ProfileActivity extends HelloWorldActivity {
    private TextView tvVisitedLocations;
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
        if (setLoginProtected()) {
            setLoading(true);
            db.getUser(auth.getUser().getUid());
        }
    }

    private void updateUserProfile() {
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        if (user.getVisitedLocations().size() > 0) {
            tvVisitedLocations.setText(getLocationsAsString(user.getVisitedLocations()));
        }
    }

    private String getLocationsAsString(ArrayList<Location> visitedLocations) {
        StringBuilder builder = new StringBuilder();
        for(Location l : visitedLocations) {
            builder.append(l.getName());
            builder.append("\n");
        }
        return builder.toString();
    }

    private void initUi() {
        setContentView(R.layout.activity_profile);
        setTitle(getString(R.string.profile));
        tvVisitedLocations = findViewById(R.id.tvVisitedLocations);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvUsername = findViewById(R.id.tvProfileUsername);
    }


    @Override
    protected void onUserRetrieved(User user) {
        super.onUserRetrieved(user);
        this.user = user;
        updateUserProfile();
        setLoading(false);
    }

    @Override
    protected void onDatabaseError(String errorMessage) {
        super.onDatabaseError(errorMessage);
        setLoading(false);
    }
}
