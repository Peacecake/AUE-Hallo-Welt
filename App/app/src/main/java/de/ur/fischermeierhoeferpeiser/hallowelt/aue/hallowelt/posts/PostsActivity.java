package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.posts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Post;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;

public class PostsActivity extends HelloWorldActivity {

    private Location location;
    private ArrayList<Post> posts = new ArrayList<>();
    private ListView listView;
    private TextView tvLocationName;
    private TextView tvLocationDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_new);
        listView = findViewById(R.id.lvPostList);
        tvLocationDescription = findViewById(R.id.tvLocationDescription);
        tvLocationName = findViewById(R.id.tvLocationName);
        setTitle("Beiträge");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (setLoginProtected()) {
            initLoader(R.id.postsLoader);
            setLoading(true);
            Intent i = getIntent();
            String locationId = i.getStringExtra("locationId");
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
        PostAdapter adapter = new PostAdapter(this, posts);
        listView.setAdapter(adapter);
    }
}
