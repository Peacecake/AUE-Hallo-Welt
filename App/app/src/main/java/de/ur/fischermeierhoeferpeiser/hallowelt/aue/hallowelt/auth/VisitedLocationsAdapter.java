package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Location;

public class VisitedLocationsAdapter extends BaseAdapter {
    private ArrayList<Location> locations = new ArrayList<>();
    private LayoutInflater inflater;

    public VisitedLocationsAdapter(Context context, ArrayList<Location> locations) {
        this.locations = locations;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout locationEntry = (LinearLayout) inflater.inflate(R.layout.visited_locations_list_entry, parent, false);
        TextView tvLocationName = locationEntry.findViewById(R.id.tvVisitedLocationsEntry);

        Location location = locations.get(position);

        tvLocationName.setText(location.getName());
        locationEntry.setTag(location.getId());
        return locationEntry;
    }
}
