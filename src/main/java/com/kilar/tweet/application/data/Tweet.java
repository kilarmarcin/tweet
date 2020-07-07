package com.kilar.tweet.application.data;

import java.time.LocalDateTime;

public class Tweet implements Comparable {
    private final String message;
    private final LocalDateTime timestamp;
    private final String author;

    public Tweet(String message, LocalDateTime timestamp, String author) {
        this.message = message;
        this.timestamp = timestamp;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int compareTo(Object object) {

        if (!(object instanceof Tweet)) {
            return -1;
        }

        Tweet other = (Tweet) object;

        return other.getTimestamp().compareTo(timestamp);
    }
}
