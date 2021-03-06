package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;

public class SetupActivity extends HelloWorldActivity implements View.OnClickListener {

    private ArrayList<Location> locations;
    private int count;
    private Button btnSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        btnSetup = findViewById(R.id.btnSetup);
        btnSetup.setOnClickListener(this);
        setupLocations();
    }

    private void setupLocations() {
        locations = new ArrayList<Location> () {{
            add(new Location(49.024540, 12.054830, getString(R.string.westbad), getString(R.string.westbadDescription)));
            add(new Location(49.018618, 12.096435, getString(R.string.neupfarrplatz), getString(R.string.neupfarrplatzDescription)));
            add(new Location(49.021974, 12.097153, getString(R.string.steinerneBrücke), getString(R.string.steinerneBrückeDescription)));
            add(new Location(49.019488, 12.098254, getString(R.string.stpeter), getString(R.string.stpeterDescription)));
            add(new Location(49.022921, 12.098124, getString(R.string.jahninsel), getString(R.string.jahninselDescription)));
            add(new Location(49.026383, 12.088610, getString(R.string.dultplatz), getString(R.string.dultplatzDescription)));
            add(new Location(49.023495, 12.048264, getString(R.string.donaupark), getString(R.string.donauparkDescription)));
            add(new Location(49.017866, 12.093099, getString(R.string.sax), getString(R.string.saxDescription)));

            add(new Location(49.024217, 12.096606, getString(R.string.spital), getString(R.string.spitalDescription)));
            add(new Location(49.025113, 12.085993, getString(R.string.rtbad), getString(R.string.rtbadDescription)));
            add(new Location(49.018564, 12.089553, getString(R.string.bismarckplatz), getString(R.string.bismarckplatzDescription)));
            add(new Location(49.018601, 12.090173, getString(R.string.stenz), getString(R.string.stenzDescription)));
            add(new Location(49.021262, 12.092645, getString(R.string.bar13), getString(R.string.bar13Description)));
            add(new Location(49.021129, 12.093623, getString(R.string.orangeBar), getString(R.string.orangeBarDescription)));
            add(new Location(49.019696, 12.089012, getString(R.string.kneitinger), getString(R.string.kneitingerDescription)));
        }};
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
