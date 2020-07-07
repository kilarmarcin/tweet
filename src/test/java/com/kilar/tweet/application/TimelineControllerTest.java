package com.kilar.tweet.application;

import com.kilar.tweet.application.data.Tweet;
import com.kilar.tweet.application.view.TweetView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jmx.export.annotation.ManagedOperation;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimelineControllerTest {

    @InjectMocks
    private TimelineController timelineController;

    @Mock
    private TweetService tweetService;

    @Test
    void shouldProperlyProcessMessage(){

        //given
        String login = "login";
        Tweet tweet = new Tweet("message", LocalDateTime.now(), login);
        when(tweetService.getFollowedUsersTweets(login)).thenReturn(List.of(tweet));

        //when
        List<TweetView> tweetViewList = timelineController.timeline(login);

        //then
        verify(tweetService).getFollowedUsersTweets(login);
        assertThat(tweetViewList).isNotEmpty();
        TweetView resultTweet = tweetViewList.get(0);

        assertThat(resultTweet.getMessage()).isEqualTo(tweet.getMessage());
        assertThat(resultTweet.getTimestamp()).isEqualTo(tweet.getTimestamp());
        assertThat(resultTweet.getAuthor()).isEqualTo(tweet.getAuthor());

    }

}