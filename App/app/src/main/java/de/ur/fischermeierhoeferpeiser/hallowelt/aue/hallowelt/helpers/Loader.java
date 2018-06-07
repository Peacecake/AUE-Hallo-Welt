package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import java.net.ConnectException;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;

public class Loader extends FrameLayout {
    private FrameLayout l;

    public Loader(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Loader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Loader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Loader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.loader, this);
        l = findViewById(R.id.loader);
    }

    public void setLoading(boolean isLoading) {
        AlphaAnimation anim;
        if (isLoading) {
            anim = new AlphaAnimation(0f, 1f);
            anim.setDuration(200);
            l.setAnimation(anim);
            l.setVisibility(View.VISIBLE);
        } else {
            anim = new AlphaAnimation(1f, 0f);
            anim.setDuration(200);
            l.setAnimation(anim);
            l.setVisibility(View.GONE);
        }
    }
}
