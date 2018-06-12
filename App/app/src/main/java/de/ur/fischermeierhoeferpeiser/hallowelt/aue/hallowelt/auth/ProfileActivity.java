package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.User;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.posts.PostsActivity;

public class ProfileActivity extends HelloWorldActivity implements AdapterView.OnItemClickListener {
    private TextView tvUsername;
    private TextView tvEmail;
    private ListView lvVisitedLocations;


    private VisitedLocationsAdapter adapter;
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
            adapter = new VisitedLocationsAdapter(this, user.getVisitedLocations());
            lvVisitedLocations.setAdapter(adapter);
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
        lvVisitedLocations = findViewById(R.id.lvVisitedLocationsList);
        lvVisitedLocations.setOnItemClickListener(this);
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
    protected void onUserCheckedIn(User user) {
        super.onUserCheckedIn(user);
        setLoading(false);
        Intent postIntent = new Intent(this, PostsActivity.class);
        postIntent.putExtra("locationId", user.getCurrentLocation().getId());
        startActivity(postIntent);
    }

    @Override
    protected void onDatabaseError(String errorMessage) {
        super.onDatabaseError(errorMessage);
        setLoading(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setLoading(true);
        db.checkInUser(user, user.getVisitedLocations().get(position));
    }
}
