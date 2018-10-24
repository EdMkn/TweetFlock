package com.volmar.tweetflock.util;

public enum CountryWOEID {
	
	US(23424977L), UK(23424975L);
	
	CountryWOEID(Long woeid) {
		this.woeid = woeid;
	}

	private final long woeid;

	public long getWoeid() {
		return woeid;
	}
	
}
