package com.volmar.tweetflock.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Trend;
import org.springframework.social.twitter.api.Trends;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import com.volmar.tweetflock.controller.NotValidCountryException;
import com.volmar.tweetflock.util.CountryWOEID;

@Service
@CacheConfig(cacheNames = {"country"})
public class TwitterTrendService {
	
	@Autowired
	private Twitter twitter;
	
	private static final Logger log = LoggerFactory.getLogger(TwitterTrendService.class);
	
	@Cacheable(value = "country", key = "#id")
	public HashMap<String, List<Tweet>> getTrendTweetsByCountry(String id){
		
		if (!EnumUtils.isValidEnum(CountryWOEID.class, id.toUpperCase())) {
			throw new NotValidCountryException();
		}
		
		Trends trends = twitter.searchOperations().getLocalTrends(CountryWOEID.valueOf(id.toUpperCase()).getWoeid());
		
		log.debug("returned all trends:"+trends.getTrends().size());
		
		HashMap<String, List<Tweet>> trendMap = new HashMap<String, List<Tweet>>();
		for (Trend trend : trends.getTrends()) {
			log.debug("search trend:"+trend.getName());
			SearchResults results = twitter.searchOperations().search(
				    new SearchParameters(trend.getName())
				    .count(10));
			log.debug("search result:"+results.getTweets().size());
			trendMap.put(trend.getName(), results.getTweets());
		}
		
		return trendMap;
	}

}
