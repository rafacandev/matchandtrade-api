package com.trademaximazer;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;

import com.matchandtrade.rest.v1.controller.ItemController;

public class TradeMaximizerTransformer {

	// Utility classes should not have public constructors
	private TradeMaximizerTransformer() { }
	
	public static List<Link> transform(String tradeMaximizerResult) {

		String[] allLines = tradeMaximizerResult.split("\n");
		List<String> tradeLines = new ArrayList<>();
		
		// Get all the tradeLines which ignores the header and the ITEM SUMARRY portion.
		for (int i = 4; i< allLines.length; i++) {
			if (allLines[i].length() <= 1) {
				continue;
			}
			if (allLines[i].startsWith("ITEM SUMMARY")) {
				break;
			}
			tradeLines.add(allLines[i]);
		}
		
		List<List<Link>> links = tradeLines
			.stream()
			.map(s -> transformLine(s))
			.collect(Collectors.toList());
		
		System.out.println(links.toString());
		
		List<Link> result = new ArrayList<>();
		return result;
	}
	
	private static List<Link> transformLine(String line) {
		List<Link> result = new ArrayList<>();
		result.add(linkTo(methodOn(ItemController.class).get(1, 2)).withSelfRel());
		return result;
	}
	
}
