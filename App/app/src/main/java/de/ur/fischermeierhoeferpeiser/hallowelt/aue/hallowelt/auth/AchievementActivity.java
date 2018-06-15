package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.auth;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Achievement;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.User;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers.HelloWorldActivity;

public class AchievementActivity extends HelloWorldActivity {

    private ArrayList<Achievement> achievements;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.achievements);
        setContentView(R.layout.activity_achievement);
        listView = findViewById(R.id.lvAchievementsList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(setLoginProtected()) {
            initLoader(R.id.achievementLoader);
            setLoading(true);
            db.getUser(auth.getUser().getUid());
        }
    }

    @Override
    protected void onDatabaseError(String errorMessage) {
        super.onDatabaseError(errorMessage);
        setLoading(false);
    }

    @Override
    protected void onUserRetrieved(User user) {
        super.onUserRetrieved(user);
        achievements = user.getAchievements();
        AchievementListAdapter adapter = new AchievementListAdapter(this, achievements);
        listView.setAdapter(adapter);
        setLoading(false);
    }
}
