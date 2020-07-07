package com.kilar.tweet.service.tweet;

import com.kilar.tweet.application.TweetService;
import com.kilar.tweet.application.data.Tweet;
import com.kilar.tweet.application.request.TweetRequest;
import com.kilar.tweet.service.exception.UserDoNotExistException;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class TweetServiceImpl implements TweetService {

    private ConcurrentHashMap<String, List<Tweet>> tweetsMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Set<String>> followersMap = new ConcurrentHashMap<>();

    @Override
    public void postTweet(String login, TweetRequest request) {

        verifyUser(login);
        List<Tweet> userTweets = tweetsMap.getOrDefault(login, new ArrayList<>());

        userTweets.add(new Tweet(request.getMessage(), now(), login));

        tweetsMap.put(login, userTweets);
    }

    @Override
    public List<Tweet> getUserWall(String login) throws UserDoNotExistException {

        verifyUser(login);

        List<Tweet> tweetList = tweetsMap.getOrDefault(login, new ArrayList<>());

        Collections.reverse(tweetList);

        return tweetList;
    }

    @Override
    public List<Tweet> getFollowedUsersTweets(String login) {

        verifyUser(login);

        List<Tweet> tweetList =  followersMap.getOrDefault(login, new TreeSet<>()).stream()
                .map(follower -> getUserWall(follower))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Collections.sort(tweetList);

        return tweetList;
    }

    @Override
    public void followUser(String followingUser, String userToFollow) throws UserDoNotExistException {

        verifyUser(followingUser);
        verifyUser(userToFollow);
        if (!tweetsMap.containsKey(userToFollow)){
            throw new UserDoNotExistException("User " + userToFollow + " doesn't exist");
        }

        Set<String> followedUsersSet = followersMap.getOrDefault(followingUser, new TreeSet<>());

        followedUsersSet.add(userToFollow);

        followersMap.put(followingUser, followedUsersSet);
    }

    @Override
    public Set<String> getFollowedUsers(String followingUser) {
        verifyUser(followingUser);
        return followersMap.getOrDefault(followingUser, Collections.EMPTY_SET);
    }

    private void verifyUser(String login) throws UserDoNotExistException{
        if (login == null) {
            throw new UserDoNotExistException("User login not provided");
        }
    }

}
