package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;

public class AchievementDialog extends DialogFragment implements View.OnClickListener {
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
        builder.setTitle(R.string.achievementUnlocked);
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
