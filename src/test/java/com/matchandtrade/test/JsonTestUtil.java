package com.matchandtrade.test;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.util.JsonUtil;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonTestUtil extends JsonUtil {

	public static <T> SearchResult<T> fromSearchResultString(MockHttpServletResponse response, Class<T> resultClass) throws IOException {
		long total = extractTotal(response);
		int pageSize = 0;
		int pageNumber = 0;
		List<String> linkHeaders = response.getHeaders("Link");
		for(String linkHeader : linkHeaders) {
			pageSize = extractParameterValue(linkHeader, "pageSize=");
			if (linkHeader.contains("nextPage")) {
				pageNumber = extractParameterValue(linkHeader, "_pageNumber=") - 1;
			} else if (linkHeader.contains("previousPage")) {
				pageNumber = extractParameterValue(linkHeader, "_pageNumber=") + 1;
			}
			break;
		}
		Pagination pagination = new Pagination(pageNumber, pageSize, total);
		List<T> resultList = fromArrayString(response.getContentAsString(), resultClass);
		return new SearchResult<>(resultList, pagination);
	}

	private static long extractTotal(MockHttpServletResponse response) {
		String totalAsString = response.getHeader("X-Pagination-Total-Count");
		return Long.parseLong(totalAsString);
	}

	private static int extractParameterValue(String text, String parameter) {
		int pageSizePosition = text.indexOf(parameter) + parameter.length();
		String parameterValue = text.substring(pageSizePosition);
		Pattern numberPattern = Pattern.compile("^([0-9]+)");
		Matcher numberMatcher = numberPattern.matcher(parameterValue);
		if (numberMatcher.find()) {
			return Integer.parseInt(numberMatcher.group(1));
		}
		throw new IllegalArgumentException(String.format("Not able to extract parameter: %s from %s", parameter, text));
	}

}