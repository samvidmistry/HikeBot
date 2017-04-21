package com.example.samvidmistry.hikebot.builder;

import okhttp3.Request;

/**
 * Created by samvidmistry on 12/15/16.
 */

public class WikipediaRequestBuilder implements RequestBuilder {
    @Override
    public Request getRequest(String query) throws Exception {
        return new Request.Builder().url(
                ServiceApiUtil.getWikipediaSearchUrl(TextUtil.extractSearchQueryWiki(query))
        ).build();
    }
}
