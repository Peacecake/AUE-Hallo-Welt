package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.posts;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Post;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;

public class PostsActivity extends HelloWorldActivity implements View.OnClickListener {

    private Location location;
    private ArrayList<Post> posts = new ArrayList<>();
    private ListView listView;
    private TextView tvLocationName;
    private TextView tvLocationDescription;
    private FloatingActionButton fab;
    private String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_new);
        listView = findViewById(R.id.lvPostList);
        tvLocationDescription = findViewById(R.id.tvLocationDescription);
        tvLocationName = findViewById(R.id.tvLocationName);
        fab = findViewById(R.id.fabAddPost);
        fab.setOnClickListener(this);
        setTitle("Beiträge");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (setLoginProtected()) {
            initLoader(R.id.postsLoader);
            setLoading(true);
            Intent i = getIntent();
            locationId = i.getStringExtra("locationId");
            db.getLocation(locationId);
            db.getUser(auth.getUser().getUid());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            setLoading(true);
            db.getLocation(locationId);
        }
    }

    @Override
    protected void onDatabaseError(String errorMessage) {
        super.onDatabaseError(errorMessage);
        setLoading(false);
    }

    @Override
    protected void onLocationRetrieved(Location location) {
        super.onLocationRetrieved(location);
        this.location = location;
        posts = location.getPosts();
        update();
        setLoading(false);
    }

    private void update() {
        setTitle("Sie haben eingecheckt in:");
        tvLocationName.setText(location.getName());
        tvLocationDescription.setText(location.getDescription());
        displayPosts();
    }

    private void displayPosts() {
        Collections.reverse(posts);
        PostAdapter adapter = new PostAdapter(this, posts);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent newPost = new Intent(this, NewPostActivity.class);
        newPost.putExtra("locationId", locationId);
        startActivity(newPost);
    }
}
