package com.matchandtrade.test.random;

import java.util.Random;

public class StringRandom {
	
	String[] adjectives = {
			"able",
			"abnormal",
			"absent",
			"absolute",
			"abstract",
			"abundant",
			"academic",
			"acceptable"
	};
	
	String[] noums = {
			"call",
			"calorie",
			"camera",
			"camp",
			"campaign",
			"cancer",
			"candidate",
			"candle",
			"cane",
	};
	
	String[] prepositions = {
		"with",
		"plus",
		"and"
	};
	
	private Random random = new Random();
	
	public String nextName() {
		int aPosition = random.nextInt(adjectives.length);
		int nPosition = random.nextInt(noums.length);
		return adjectives[aPosition] + " " + noums[nPosition] + "-" + random.nextInt(500);
	}

	public String nextDescription() {
		int pPosition = random.nextInt(prepositions.length);
		return nextName() + " " + prepositions[pPosition] + " " + nextName() + "-" + random.nextInt(500);
	}
	
	public String nextEmail() {
		int aPosition = random.nextInt(adjectives.length);
		int nPosition = random.nextInt(noums.length);
		return adjectives[aPosition] + noums[nPosition] + "-" + random.nextInt(500) + "@random.com";
	}

}