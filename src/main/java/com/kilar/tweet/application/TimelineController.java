package com.kilar.tweet.application;

import com.kilar.tweet.application.view.TweetView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timeline/{login}")
public class TimelineController {

    private TweetService tweetService;

    public TimelineController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping
    public List<TweetView> timeline(@PathVariable String login) {
        return tweetService.getFollowedUsersTweets(login).stream().map(item -> new TweetView(item)).collect(Collectors.toList());
    }

}
