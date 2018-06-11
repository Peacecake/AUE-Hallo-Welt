package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Post {
    private String id;
    private String header;
    private String content;
    private String authorUsername;
    private Date date;

    public Post() {}

    public Post(String header, String content, String authorUsername) {
        this.header = header;
        this.content = content;
        this.authorUsername = authorUsername;
        this.id = Long.toString(System.currentTimeMillis());
        this.date = new Date();
    }

    public Post(String id, String header, String content, String authorUsername, Date date) {
        this.id = id;
        this.header = header;
        this.content = content;
        this.authorUsername = authorUsername;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public String getContent() {
        return content;
    }

    public String getHeader() {
        return header;
    }
}
