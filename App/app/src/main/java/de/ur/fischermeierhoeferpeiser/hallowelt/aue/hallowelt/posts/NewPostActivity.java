package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.posts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Post;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.FormValidator;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;

public class NewPostActivity extends HelloWorldActivity implements View.OnClickListener {

    private String locationId;
    private Location location;
    private EditText etHeader;
    private EditText etContent;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        locationId = i.getStringExtra("locationId");

        setContentView(R.layout.activity_new_post);
        etHeader = findViewById(R.id.etNewHeader);
        etContent = findViewById(R.id.etNewContent);
        btnSave = findViewById(R.id.btnSavePost);

        btnSave.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (setLoginProtected()) {
            initLoader(R.id.newPostLoader);
            setLoading(true, btnSave);
            db.getLocation(locationId);
        }
    }

    @Override
    protected void onDatabaseError(String errorMessage) {
        super.onDatabaseError(errorMessage);
        setLoading(false, btnSave);
    }

    @Override
    protected void onLocationRetrieved(Location location) {
        super.onLocationRetrieved(location);
        this.location = location;
        setLoading(false, btnSave);
    }

    @Override
    protected void onPostAdded(Post post) {
        super.onPostAdded(post);
        setLoading(false, btnSave);
        finish();
    }

    @Override
    public void onClick(View v) {
        FormValidator validator = new FormValidator(this);
        validator.requiredFieldsAreEmpty(etHeader, etContent);
        if (validator.isValid()) {
            setLoading(true, btnSave);
            Post p = new Post(etHeader.getText().toString(), etContent.getText().toString(), auth.getUser().getDisplayName());
            etHeader.setText("");
            etContent.setText("");
            db.addPost(locationId, p);
        } else {
            Toast.makeText(this, validator.getErrorsAsString(), Toast.LENGTH_SHORT).show();
        }
    }
}
