package org.chrisle.netbeans.plugins.nbquicksearchextender;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.netbeans.spi.quicksearch.SearchProvider;
import org.netbeans.spi.quicksearch.SearchRequest;
import org.netbeans.spi.quicksearch.SearchResponse;
import org.openide.util.Exceptions;

public class QuickSearchProvider implements SearchProvider {

	private String _searchUrl;
	private String _selector;
	private final String _charset;
	private String _baseUrl;

	public QuickSearchProvider() {
		this._charset = "UTF-8";
		Unirest.config().verifySsl(false);
	}

	@Override
	public void evaluate(SearchRequest request, SearchResponse response) {
		try {
			String searchUrl = _searchUrl + URLEncoder.encode(request.getText().toLowerCase(), _charset);
			HttpResponse<String> asString = Unirest
					.get(searchUrl).asString();
			Elements links;
			if (_searchUrl.contains("stackoverflow")) {
				connectStack(searchUrl, response);
				return;
			}
			if (_searchUrl.contains("hub.docker.com")) {
				connectDocker(searchUrl, response);
				return;
			}
			links = Jsoup.parse(asString.getBody(), searchUrl,
					Parser.htmlParser()).select(_selector);
			for (Element link : links) {
				String title = link.text();
				String url = link.absUrl("href");
				if (!url.startsWith("https") && this._searchUrl.contains("google")) {
					continue; // Ads/news/etc.
				}
				if (!response.addResult(new OpenLink(url), title)) {
					return;
				}
			}
		} catch (UnsupportedEncodingException ex) {
			Exceptions.printStackTrace(ex);
		}
	}

	public String getSearchUrl() {
		return _searchUrl;
	}

	public void setSearchUrl(String _searchUrl) {
		this._searchUrl = _searchUrl;
	}

	public String getSelector() {
		return _selector;
	}

	public void setSelector(String _selector) {
		this._selector = _selector;
	}

	public void setBaseUrl(String baseUrl) {
		this._baseUrl = baseUrl;
	}

	public void connectStack(String searchURL, SearchResponse response) {
		HttpResponse<JsonNode> queryResponse = Unirest
				.get(searchURL)
				.asJson();
		if (queryResponse.getStatus() == 200) {
			JSONObject obj = new JSONObject(queryResponse.getBody().toString());
			if (obj.has("items")) {
				JSONArray arr = obj.getJSONArray("items");
				for (Object object : arr) {
					JSONObject name = (JSONObject) object;
					if (!response.addResult(new OpenLink(name.getString("link")),
							name.getString("title"))) {
						return;
					}
				}
			}
		}

	}

	public void connectDocker(String searchURL, SearchResponse response) {
		HttpResponse<JsonNode> queryResponse = Unirest
				.get(searchURL)
				.asJson();
		if (queryResponse.getStatus() == 200) {
			JSONObject obj = new JSONObject(queryResponse.getBody().toString());
			if (obj.has("summaries") && obj.getInt("count") != 0) {
				JSONArray arr = obj.getJSONArray("summaries");
				for (Object object : arr) {
					JSONObject name = (JSONObject) object;
					if (!response.addResult(new OpenLink(_baseUrl + name.getString("name")),
							name.getString("short_description"))) {
						return;
					}
				}
			}
		}

	}

}
