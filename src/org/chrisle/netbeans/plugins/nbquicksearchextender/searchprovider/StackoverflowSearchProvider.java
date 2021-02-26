package org.chrisle.netbeans.plugins.nbquicksearchextender.searchprovider;

import org.chrisle.netbeans.plugins.nbquicksearchextender.QuickSearchProvider;
import org.netbeans.spi.quicksearch.SearchRequest;
import org.netbeans.spi.quicksearch.SearchResponse;

public class StackoverflowSearchProvider extends QuickSearchProvider {

	public StackoverflowSearchProvider() {
//		super.setSearchUrl("http://stackoverflow.com/search?q=");
		super.setSearchUrl("https://api.stackexchange.com/2.2/search?sort=activity&site=stackoverflow&intitle=");
//		super.setSelector(".summary>.result-link a");
	}

	@Override
	public void evaluate(SearchRequest request, SearchResponse response) {
		super.evaluate(request, response); //To change body of generated methods, choose Tools | Templates.
	}
}
