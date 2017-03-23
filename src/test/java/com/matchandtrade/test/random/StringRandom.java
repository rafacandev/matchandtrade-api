package com.matchandtrade.test.random;

import java.util.Random;

public class StringRandom {
	
	private static String[] adjectives = {
			"able",
			"abnormal",
			"absent",
			"absolute",
			"abstract",
			"abundant",
			"academic",
			"acceptable"
	};
	
	private static String[] noums = {
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
	
	private static String[] prepositions = {
		"with",
		"plus",
		"and"
	};
	
	private static Random random = new Random();
	
	public static String nextName() {
		int aPosition = random.nextInt(adjectives.length);
		int nPosition = random.nextInt(noums.length);
		return adjectives[aPosition] + " " + noums[nPosition] + "-" + random.nextInt(9999);
	}

	public static String nextDescription() {
		int pPosition = random.nextInt(prepositions.length);
		return nextName() + " " + prepositions[pPosition] + " " + nextName() + "-" + random.nextInt(9999);
	}
	
	public static String nextEmail() {
		int aPosition = random.nextInt(adjectives.length);
		int nPosition = random.nextInt(noums.length);
		return adjectives[aPosition] + noums[nPosition] + "-" + random.nextInt(9999) + "@random.com";
	}
	
	public static String nextString() {
		return random.nextInt(9999) + "-" + random.nextInt(9999) + "-" + random.nextInt(9999);		
	}

}