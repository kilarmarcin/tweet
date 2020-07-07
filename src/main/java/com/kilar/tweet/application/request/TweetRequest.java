package com.kilar.tweet.application.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TweetRequest {

    @Size(max = 140)
    @NotEmpty
    private String message;

    public TweetRequest(@Size(max = 140, min = 1) String message) {
        this.message = message;
    }

    public TweetRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
