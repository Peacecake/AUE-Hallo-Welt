package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;

public class SetupActivity extends HelloWorldActivity implements View.OnClickListener {

    private final ArrayList<Location> locations = new ArrayList<Location> () {{
        add(new Location(49.024540, 12.054830, "Westbad", "Ein tolles Bad im Westen Regensburgs"));
        add(new Location(49.018618, 12.096435, "Neupfarrplatz", "Hier ist die Neupfarrkirche und man kann teueres Eis kaufen"));
        add(new Location(49.021974, 12.097153, "Steinerne Brücke", "Die Steinerne Brücke ist neben dem Regensburger Dom das bedeutendste Wahrzeichen der Stadt Regensburg. Mit dem Baubeginn 1135 ist sie die älteste erhaltene Brücke Deutschlands und gilt als ein Meisterwerk mittelalterlicher Baukunst."));
    }};

    private int count;
    private Button btnSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        btnSetup = findViewById(R.id.btnSetup);
        btnSetup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        count = 0;
        btnSetup.setEnabled(false);
        for(Location location : locations) {
            db.addLocation(location);
        }
    }

    @Override
    protected void onDatabaseError(String errorMessage) {
        super.onDatabaseError(errorMessage);
        btnSetup.setEnabled(true);
    }

    @Override
    protected void onLocationAdded(Location location) {
        super.onLocationAdded(location);
        Toast.makeText(this, "Location " + location.getName() + " hinzugefügt", Toast.LENGTH_SHORT).show();
        count++;
        if (count == locations.size()) {
            Toast.makeText(this, "Fertig!", Toast.LENGTH_SHORT).show();
            btnSetup.setEnabled(true);
        }
    }
}
