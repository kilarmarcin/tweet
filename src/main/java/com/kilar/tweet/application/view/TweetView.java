package com.kilar.tweet.application.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kilar.tweet.application.data.Tweet;

import java.time.LocalDateTime;

public class TweetView {

    private String message;
    private String author;
    private LocalDateTime timestamp;

    @JsonCreator
    public TweetView(String message, String author, LocalDateTime timestamp) {
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    public TweetView(Tweet tweet) {
        this.message = tweet.getMessage();
        this.author = tweet.getAuthor();
        this.timestamp = tweet.getTimestamp();
    }

    public TweetView() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
