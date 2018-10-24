package com.volmar.tweetflock.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.social.twitter.api.SearchMetadata;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Trend;
import org.springframework.social.twitter.api.Trends;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import com.volmar.tweetflock.controller.NotValidCountryException;
import com.volmar.tweetflock.util.CountryWOEID;

@RunWith(MockitoJUnitRunner.class)
public class TwitterTrendServiceTest {

	private Twitter twitter = mock(Twitter.class, Mockito.RETURNS_DEEP_STUBS);;

	@InjectMocks
	TwitterTrendService twitterTrendService;

	@Test
	public void getTrendTweetsByCountryTest() {
		Trends trends = new Trends(new Date(), Arrays.asList(new Trend("name", "query")));
		Tweet tweet = new Tweet(111L, "text", new Date(), "user", "url", 222L, 333L, "en", "source");
		SearchResults srs = new SearchResults(Arrays.asList(tweet), new SearchMetadata(111L, 222L));

		when(twitter.searchOperations().getLocalTrends(CountryWOEID.UK.getWoeid())).thenReturn(trends);
		when(twitter.searchOperations().search(new SearchParameters("name").count(10))).thenReturn(srs);

		HashMap<String, List<Tweet>> result = twitterTrendService.getTrendTweetsByCountry("uk");
		assertTrue(result.containsKey("name"));
		assertEquals(1, result.get("name").size());
		assertEquals(111L, result.get("name").get(0).getId());
	}

	@Test(expected = NotValidCountryException.class)
	public void getTrendTweetsByNotValidCountryTest() {
		twitterTrendService.getTrendTweetsByCountry("br");
	}

}
