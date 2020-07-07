package com.kilar.tweet.application;

import com.kilar.tweet.application.request.TweetRequest;
import com.kilar.tweet.application.view.TweetView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimelineControllerIT {

    private static final String MESSAGES = "lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh " +
            "augue, suscipit a, scelerisque sed, lacinia in, mi.";
    private static final String followers = "/followers";
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;
    private String login = "mkilar";
    private String president1 = "Barack Obama";
    private String president2 = "George Bush";
    private String president3 = "Donald Trump";

    @Test
    public void shouldProperlyBuildUserTimeline() {

        //given
        List<String> message = Arrays.asList(MESSAGES.split(" "));

        prepareTweets(message);
        setupFollowers();

        //when
        List<TweetView> tweetViewList = getUserTimeline(login);

        //then
        Collections.reverse(message);
        for(int i =0; i< tweetViewList.size();i++){
            assertThat(tweetViewList.get(i).getMessage()).isEqualTo(message.get(i));
        }

    }

    private void prepareTweets(List<String> message) {
        List<String> presidents = List.of(president1, president2, president3);
        message.forEach(msg -> postTweet(presidents.get(randomNumber()), msg));
    }

    private void setupFollowers() {

        restTemplate.put(
                getUrl() + login + followers, president1, ResponseEntity.class);

        restTemplate.put(
                getUrl() + login + followers, president2, ResponseEntity.class);

        restTemplate.put(
                getUrl() + login + followers, president3, ResponseEntity.class);
    }


    private List<TweetView> getUserTimeline(String login) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<TweetView[]> tweetViewResponse = restTemplate.getForEntity(
                getUrl() + login, TweetView[].class);

        return Arrays.asList(tweetViewResponse.getBody());
    }

    private ResponseEntity postTweet(String author, String tweetMessage) {

        TweetRequest tweetRequest = new TweetRequest();
        tweetRequest.setMessage(tweetMessage);

        //when
        ResponseEntity response = restTemplate.postForEntity(getUrl() + author, tweetRequest, String.class);

        return response;
    }

    private int randomNumber() {
        return ThreadLocalRandom.current().nextInt(0, 2 + 1);
    }

    private String getUrl() {
        return "http://localhost:" + port + "/api/timeline/";
    }

}