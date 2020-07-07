package com.kilar.tweet.application.view;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class TweetListView {

    private List<TweetView> responseList;

    @JsonCreator
    public TweetListView(List<TweetView> responseList) {
        this.responseList = responseList;
    }

    public TweetListView() {
    }

    public List<TweetView> getResponseList() {
        return responseList;
    }
}
