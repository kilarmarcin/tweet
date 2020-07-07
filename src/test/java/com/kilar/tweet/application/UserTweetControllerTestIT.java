package com.kilar.tweet.application;

import com.kilar.tweet.application.request.TweetRequest;
import com.kilar.tweet.application.view.TweetView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTweetControllerTestIT {

    private static final String followers = "/followers";
    @LocalServerPort
    private int port = 8080;
    @Autowired
    private TestRestTemplate restTemplate;
    private String login = "mkilar";
    private String president1 = "Barack Obama";
    private String president2 = "George Bush";

    @Test
    void shouldGenerateErrorWhenLoginIsNotProvided() {

        //when
        ResponseEntity tweetViewResponse = restTemplate.getForEntity(getUrl(), String.class);

        //then
        assertThat(tweetViewResponse.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void showGetTweetsList() {

        //when
        ResponseEntity<TweetView[]> tweetViewResponse = restTemplate.getForEntity(
                getUrl() + login, TweetView[].class);

        //then
        assertThat(tweetViewResponse.getStatusCode()).isEqualTo(OK);
    }


    @Test
    void shouldValidateTweetMaxLength() throws Exception {

        //given
        String message = "veeeeeeeeeryyyyyyyy___looooong_tweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeettttttttveeeeeeeeeryyyyyyyy" +
                "veeeeeeeeeryyyyyyyy___looooong_tweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeettttttttveeeeeeeeeryyyyyyyy";
        TweetRequest tweetRequest = new TweetRequest();
        tweetRequest.setMessage(message);

        //when
        ResponseEntity response = restTemplate.postForEntity(getUrl() + login, tweetRequest, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);

    }

    @Test
    void shouldValidateTweetMinLength() throws Exception {

        //given
        String message = "";
        TweetRequest tweetRequest = new TweetRequest();
        tweetRequest.setMessage(message);

        //when
        ResponseEntity response = restTemplate.postForEntity(getUrl() + login, tweetRequest, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldValidateTweetWithNullMessage() throws Exception {

        //given
        TweetRequest tweetRequest = new TweetRequest();

        //when
        ResponseEntity response = restTemplate.postForEntity(getUrl() + login, tweetRequest, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void shouldValidateTweetWithNullRequest() throws Exception {

        //when
        ResponseEntity response = restTemplate.postForEntity(getUrl() + login, null, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(UNSUPPORTED_MEDIA_TYPE);
    }


    @Test
    void shouldPostTweet() throws Exception {

        //given
        String message = "tweetRequest";
        TweetRequest tweetRequest = new TweetRequest();
        tweetRequest.setMessage(message);

        List<TweetView> beforeList = getUserWall(login);

        //when
        ResponseEntity response = restTemplate.postForEntity(getUrl() + login, tweetRequest, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);

        List<TweetView> afterList = getUserWall(login);

        assertThat(afterList.size()).isGreaterThan(beforeList.size());
        assertThat(afterList.get(0).getMessage()).isEqualTo(message);

    }

    @Test
    void followNotExistingUserShouldFail() throws Exception {

        //when
        String unknownUser = "unknownUser";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

       ResponseEntity responseEntity =  restTemplate.exchange(getUrl() + login + followers, HttpMethod.PUT,
               new HttpEntity<>(unknownUser, headers), String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
        List<String> afterList = getUserFollowers(unknownUser);
        assertThat(afterList.isEmpty()).isTrue();

    }

    @Test
    void shouldPutFollower() throws Exception {

        //given
        postTweet(president2, "hello");
        postTweet(president1, "hello");

        //when
        restTemplate.put(getUrl() + login + followers, president1, ResponseEntity.class);

        restTemplate.put(getUrl() + login + followers, president2, ResponseEntity.class);

        //then

        List<String> afterList = getUserFollowers(login);
        assertThat(afterList.containsAll(Arrays.asList(president1, president2))).isTrue();

    }

    private List<TweetView> getUserWall(String login) throws MalformedURLException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<TweetView[]> tweetViewResponse = restTemplate.getForEntity(
                getUrl() + login, TweetView[].class);

        return Arrays.asList(tweetViewResponse.getBody());
    }

    private List<String> getUserFollowers(String login) throws MalformedURLException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String[]> followersResponse = restTemplate.getForEntity(getUrl() + login + followers, String[].class);

        return Arrays.asList(followersResponse.getBody());
    }

    private ResponseEntity postTweet(String author, String tweetMessage) {

        TweetRequest tweetRequest = new TweetRequest();
        tweetRequest.setMessage(tweetMessage);

        //when
        ResponseEntity response = restTemplate.postForEntity(getUrl() + author, tweetRequest, String.class);

        return response;
    }

    private String getUrl() {
        return "http://localhost:" + port + "/api/tweets/";
    }

}