package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;

public class SetupActivity extends HelloWorldActivity implements View.OnClickListener {

    private final ArrayList<Location> locations = new ArrayList<Location> () {{
        add(new Location(49.024540, 12.054830, getString(R.string.westbad), getString(R.string.westbadDescription)));
        add(new Location(49.018618, 12.096435, getString(R.string.neupfarrplatz), getString(R.string.neupfarrplatzDescription)));
        add(new Location(49.021974, 12.097153, getString(R.string.steinerneBrücke), getString(R.string.steinerneBrückeDescription)));
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
