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
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Achievement;

public class AchievementListAdapter extends BaseAdapter {
    private ArrayList<Achievement> achievements;
    private LayoutInflater inflater;

    public AchievementListAdapter(Context context, ArrayList<Achievement> achievements) {
        this.achievements = achievements;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return achievements.size();
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
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.achievement_list_entry, parent, false);
        TextView tv = layout.findViewById(R.id.tvAchievementListTitle);

        Achievement achievement = achievements.get(position);

        tv.setText(achievement.getTitle());
        layout.setTag(achievement.getId());
        return layout;
    }
}
