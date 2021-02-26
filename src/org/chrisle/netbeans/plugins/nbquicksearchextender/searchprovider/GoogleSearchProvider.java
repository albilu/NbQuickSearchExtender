package org.chrisle.netbeans.plugins.nbquicksearchextender.searchprovider;

import org.chrisle.netbeans.plugins.nbquicksearchextender.QuickSearchProvider;
import org.netbeans.spi.quicksearch.SearchRequest;
import org.netbeans.spi.quicksearch.SearchResponse;

public class GoogleSearchProvider extends QuickSearchProvider {

	public GoogleSearchProvider() {
		super.setSearchUrl("https://www.google.com/search?q=");
		super.setSelector("#main .ZINbbc .kCrYT>a");
	}

	@Override
	public void evaluate(SearchRequest request, SearchResponse response) {
		super.evaluate(request, response);
	}
}
