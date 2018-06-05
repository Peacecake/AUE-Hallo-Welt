package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Post {
    private String id;
    private String header;
    private String content;
    private String authorUsername;

    public Post() {}

    public Post(String header, String content, String authorUsername) {
        this.header = header;
        this.content = content;
        this.authorUsername = authorUsername;
        this.id = Long.toString(System.currentTimeMillis());
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
