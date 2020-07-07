package com.kilar.tweet.application;

import com.kilar.tweet.application.exception.BadRequestException;
import com.kilar.tweet.application.request.TweetRequest;
import com.kilar.tweet.application.view.TweetView;
import com.kilar.tweet.service.exception.UserDoNotExistException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tweets/{login}")
public class UserTweetController {

    private TweetService tweetService;

    public UserTweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public void postTweet(@PathVariable String login, @RequestBody @Valid TweetRequest tweetRequest) {
        tweetService.postTweet(login, tweetRequest);
    }

    @GetMapping
    public List<TweetView> wall(@PathVariable String login) {
        return tweetService.getUserWall(login).stream().map(item -> new TweetView(item)).collect(Collectors.toList());
    }

    @PutMapping("/followers")
    public void putFollower(@PathVariable String login, @RequestBody @Valid @NotEmpty String userToFollow) {
        try {
            tweetService.followUser(login, userToFollow);
        } catch (UserDoNotExistException userDoNotExist) {
            throw new BadRequestException(userDoNotExist);
        }
    }

    @GetMapping("/followers")
    public Set<String> getFollowers(@PathVariable String login) {
        return tweetService.getFollowedUsers(login);
    }


}
