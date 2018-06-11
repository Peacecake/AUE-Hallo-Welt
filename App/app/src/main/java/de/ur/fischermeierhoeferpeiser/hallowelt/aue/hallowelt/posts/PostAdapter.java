package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.posts;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;
import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper.Post;

public class PostAdapter extends BaseAdapter {
    private ArrayList<Post> posts;
    private LayoutInflater inflater;


    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.posts = posts;
        inflater = LayoutInflater.from((context));
    }

    @Override
    public int getCount() {
        return posts.size();
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
        ConstraintLayout postEntry = (ConstraintLayout) inflater.inflate(R.layout.post_list_entry, parent, false);
        TextView tvAuthorName = postEntry.findViewById(R.id.tvPostAuthor);
        TextView tvPostDate = postEntry.findViewById(R.id.tvPostDate);
        TextView tvPostContent = postEntry.findViewById(R.id.tvPostContent);
        TextView tvPostHeader = postEntry.findViewById(R.id.tvPostHeader);

        Post post = posts.get(position);

        tvAuthorName.setText(post.getAuthorUsername());
        Date d = post.getDate();
        String date = new SimpleDateFormat("dd.MM.yyyy").format(d);
        String time = new SimpleDateFormat("HH:mm").format(d);
        tvPostDate.setText("Schrieb am " + date + " um " + time + " Uhr");
        tvPostContent.setText(post.getContent());
        tvPostHeader.setText(post.getHeader());
        postEntry.setTag(post.getId());
        return postEntry;
    }
}
