package com.kilar.tweet.application;

import com.kilar.tweet.application.data.Tweet;
import com.kilar.tweet.application.request.TweetRequest;
import com.kilar.tweet.application.view.TweetView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTweetControllerTest {

    @InjectMocks
    private UserTweetController userTweetController;

    @Mock
    private TweetService tweetService;

    @Test
    void wallShouldProperlyProcessMessage() {

        //given
        String login = "login";
        Tweet tweet = new Tweet("message", LocalDateTime.now(), login);
        when(tweetService.getUserWall(login)).thenReturn(List.of(tweet));

        //when
        List<TweetView> tweetViewList = userTweetController.wall(login);

        //then
        verify(tweetService).getUserWall(login);
        assertThat(tweetViewList).isNotEmpty();
        TweetView resultTweet = tweetViewList.get(0);

        assertThat(resultTweet.getMessage()).isEqualTo(tweet.getMessage());
        assertThat(resultTweet.getTimestamp()).isEqualTo(tweet.getTimestamp());
        assertThat(resultTweet.getAuthor()).isEqualTo(tweet.getAuthor());

    }

    @Test
    void getFollowersProperlyProcessMessage() {

        //given
        String login = "login";
        String second = "second";
        when(tweetService.getFollowedUsers(login)).thenReturn(Set.of(second));

        //when
        Set<String> followersList = userTweetController.getFollowers(login);

        //then
        verify(tweetService).getFollowedUsers(login);
        assertThat(followersList).contains(second);

    }

    @Test
    void postTweetShouldProperlyProcessMessage() {

        //given
        String login = "login";
        TweetRequest tweetRequest = new TweetRequest("msg");

        //when
        userTweetController.postTweet(login, tweetRequest );

        //then
        verify(tweetService).postTweet(login, tweetRequest);

    }

    @Test
    void putFollowerShouldProperlyProcessMessage() {

        //given
        String login = "login";
        String second = "second";

        //when
        userTweetController.putFollower(login, second);

        //then
        verify(tweetService).followUser(login, second);

    }

}