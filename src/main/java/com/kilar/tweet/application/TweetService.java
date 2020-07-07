package com.kilar.tweet.application;

import com.kilar.tweet.application.data.Tweet;
import com.kilar.tweet.application.request.TweetRequest;
import com.kilar.tweet.service.exception.UserDoNotExistException;

import java.util.List;
import java.util.Set;

public interface TweetService {

    List<Tweet> getUserWall(String login) throws UserDoNotExistException;

    List<Tweet> getFollowedUsersTweets(String login);

    void postTweet(String login, TweetRequest request);

    void followUser(String followingUser, String userToFollow) throws UserDoNotExistException;

    Set<String> getFollowedUsers(String followingUser);
}
