package com.volmar.tweetflock.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.volmar.tweetflock.service.TwitterTrendService;

@RestController
@RequestMapping(value = "/api/tweetflock/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class TweetController {
	
	@Autowired
	private TwitterTrendService twitterTrendService;


	private static final Logger log = LoggerFactory.getLogger(TweetController.class);

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public HashMap<String, List<Tweet>> getTrendsByCountry(@PathVariable String id) throws ExecutionException {
		
		log.debug("Received request for: "+id);
		
		HashMap<String, List<Tweet>> trends = twitterTrendService.getTrendTweetsByCountry(id);
	
		return trends;
	}
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "It's a not (yet) supported Country") // 400
	@ExceptionHandler(NotValidCountryException.class)
	public void isNotIP() {
		// Nothing to do
	}
}
