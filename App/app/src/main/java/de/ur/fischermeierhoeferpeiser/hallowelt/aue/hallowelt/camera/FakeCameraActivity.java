package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.camera;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.map.MapsActivity;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.posts.PostsActivity;

public class FakeCameraActivity extends HelloWorldActivity {

    private Button takePictureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_camera);
        setTitle("QR Code Scanner");

        takePictureButton = findViewById(R.id.button_scan_code);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postsIntent = new Intent(FakeCameraActivity.this, PostsActivity.class);
                startActivity(postsIntent);
            }
        });
    }

}
