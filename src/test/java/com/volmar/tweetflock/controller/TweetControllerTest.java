package com.volmar.tweetflock.controller;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.volmar.tweetflock.controller.NotValidCountryException;
import com.volmar.tweetflock.controller.TweetController;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(TweetController.class)
public class TweetControllerTest {
	@Autowired
	private MockMvc mvc;
	@MockBean
	private TweetController tweetController;

	@Test
	public void getTrendsByCountry() throws Exception {
		Tweet t1 = new Tweet(12345L, "text", new Date(), "user", "image url", 111L, 222L, "en", "source");
		Tweet t2 = new Tweet(6789L, "text", new Date(), "user", "image url", 111L, 222L, "en", "source");

		HashMap<String, List<Tweet>> result = new HashMap<String, List<Tweet>>();
		result.put("#hash1", Arrays.asList(t1, t2));
		result.put("#hash2", Arrays.asList(t1, t2));

		given(tweetController.getTrendsByCountry("uk")).willReturn(result);
		mvc.perform(get("/api/tweetflock/uk").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.#hash1", hasSize(2)))
				.andExpect(jsonPath("$.#hash1.[0]", hasEntry("text", "text")))
				.andExpect(jsonPath("$.#hash2", hasSize(2)))
				.andExpect(jsonPath("$.#hash2.[0]", hasEntry("text", "text")));
	}

	@Test
	public void getTrendsByNotSupportedCountry() throws Exception {
		given(tweetController.getTrendsByCountry("br")).willThrow(new NotValidCountryException());
		mvc.perform(get("/api/tweetflock/br").contentType(APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}
}
