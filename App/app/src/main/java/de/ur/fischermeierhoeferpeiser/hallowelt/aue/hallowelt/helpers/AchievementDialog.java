package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Achievement;

public class AchievementDialog extends DialogFragment implements View.OnClickListener {

    private Activity activity;
    private Achievement achievement;
    private TextView tvHeader;
    private TextView tvDescription;
    private TextView tvClose;

    /*public AchievementDialog(Activity activity, Achievement achievement) {
        super(activity);
        this.activity = activity;
        this.achievement = achievement;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.achievement_dialog);
        tvHeader = findViewById(R.id.tvAchievementTitle);
        tvDescription = findViewById(R.id.tvAchievementDescription);
        tvClose = findViewById(R.id.tvCloseAchievement);
        tvClose.setOnClickListener(this);
    }*/

    public static AchievementDialog newInstance(String title, String description) {
        AchievementDialog f = new AchievementDialog();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        f.setArguments(args);

        return f;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String description = getArguments().getString("description");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Erfolg freigeschaltet");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.achievement_dialog, null);

        TextView tvHeader = content.findViewById(R.id.tvAchievementTitle);
        TextView tvDesc = content.findViewById(R.id.tvAchievementDescription);
        TextView tvClose = content.findViewById(R.id.tvCloseAchievement);
        tvClose.setOnClickListener(this);
        tvHeader.setText(title);
        tvDesc.setText(description);
        builder.setView(content);

        return builder.create();
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }
}
