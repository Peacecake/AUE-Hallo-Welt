package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.posts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Post;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;

public class PostsActivity extends HelloWorldActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

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
        listView.setOnItemLongClickListener(this);
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
            db.getUser(auth.getUser().getUid());
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

    @Override
    protected void onPostDeleted() {
        super.onPostDeleted();
        setLoading(true);
        posts.clear();
        db.getLocation(locationId);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final String locationId = location.getId();
        final String postId = view.getTag().toString();
        new AlertDialog.Builder(this)
                .setTitle("Post löschen")
                .setMessage("Post wirklich löschen?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                        db.deletePost(locationId, postId);
                    }
                })
                .setNegativeButton("Nein", null)
                .show();
        return false;
    }
}
