package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.firebaseWrapper;

public class Post {
    private String header;
    private String content;
    private String authorUsername;

    public Post(String header, String content, String authorUsername) {
        this.header = header;
        this.content = content;
        this.authorUsername = authorUsername;
    }
}
