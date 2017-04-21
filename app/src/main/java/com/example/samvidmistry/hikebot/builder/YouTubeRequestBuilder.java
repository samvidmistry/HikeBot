package com.example.samvidmistry.hikebot.builder;

import okhttp3.Request;

/**
 * Created by samvidmistry on 12/19/16.
 */

public class YouTubeRequestBuilder implements RequestBuilder {
    @Override
    public Request getRequest(String query) throws Exception {
        return new Request.Builder().url(
                ServiceApiUtil.getYouTubeSearchUrl(TextUtil.extractSearchQueryYouTube(query))
        ).build();
    }
}
