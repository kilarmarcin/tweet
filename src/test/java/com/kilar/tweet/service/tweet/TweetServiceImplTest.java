package com.kilar.tweet.service.tweet;

import com.kilar.tweet.application.data.Tweet;
import com.kilar.tweet.application.request.TweetRequest;
import com.kilar.tweet.service.exception.UserDoNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TweetServiceImplTest {

    @InjectMocks
    private TweetServiceImpl tweetService;

    private String login = "login";

    @Test
    void getUserWallShouldTrowExceptionWithoutLogin() throws UserDoNotExistException {
        Assertions.assertThrows(UserDoNotExistException.class, () -> tweetService.getUserWall(null));
    }

    @Test
    void getUserWallShouldEmpty() throws UserDoNotExistException {
        //when
        List<Tweet> tweetList = tweetService.getUserWall(login);
        //then
        assertThat(tweetList).isEmpty();
    }

    @Test
    void getUserWallShouldReturnTweets() throws UserDoNotExistException {

        //given
        TweetRequest tweet1 = new TweetRequest("msg1");
        TweetRequest tweet2 = new TweetRequest("msg2");

        tweetService.postTweet(login, tweet1);
        tweetService.postTweet(login, tweet2);

        //when
        List<Tweet> tweetList = tweetService.getUserWall(login);

        //when
        assertThat(tweetList).isNotEmpty();
        Tweet newestTweet = tweetList.get(0);
        assertThat(newestTweet.getMessage()).isEqualTo("msg2");
        assertThat(newestTweet.getAuthor()).isEqualTo(login);
        assertThat(newestTweet.getTimestamp()).isBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
    }

    @Test
    void followingUserWithoutTweetsShouldFail() throws UserDoNotExistException {
        Assertions.assertThrows(UserDoNotExistException.class, () -> tweetService.followUser(login, login));
    }

    @Test
    void followingUserWithTweetsShouldSuccess() throws UserDoNotExistException {

        postTweetBy(login);

        tweetService.followUser("any", login);
    }

    @Test
    void followingUserWithTweetsShouldSuccess2() throws UserDoNotExistException {

        String meLogin = "me";
        postTweetBy(login);

        tweetService.followUser(meLogin, login);

        Set<String> followedUsers = tweetService.getFollowedUsers(meLogin);

        assertThat(followedUsers.contains(login));
    }

    @Test
    void getFollowedUsersWithoutLoginShouldFail() throws UserDoNotExistException {
        Assertions.assertThrows(UserDoNotExistException.class, () -> tweetService.getFollowedUsers(null));
    }

    @Test
    void getUserWallWithoutLoginShouldFail() throws UserDoNotExistException {
        Assertions.assertThrows(UserDoNotExistException.class, () -> tweetService.getUserWall(null));
    }

    @Test
    void followUserWithoutLoginShouldFail() throws UserDoNotExistException {
        Assertions.assertThrows(UserDoNotExistException.class, () -> tweetService.followUser(null, login));
        Assertions.assertThrows(UserDoNotExistException.class, () -> tweetService.followUser(login, null));
    }

    private void postTweetBy(String user) {
        TweetRequest tweet1 = new TweetRequest("msg1");
        tweetService.postTweet(user, tweet1);
    }
}