package com.example.samvidmistry.hikebot.builder;

import okhttp3.Request;

/**
 * Created by samvidmistry on 12/15/16.
 */

public class FancyTextRequestBuilder implements RequestBuilder {
    @Override
    public Request getRequest(String query) throws Exception {
        return new Request.Builder().url(
                ServiceApiUtil.getFancyTextUrl(TextUtil.extractTextQueryFancyText(query)))
                .addHeader("X-Mashape-Key", "iEYj2Fnawomsh3aW6oFLIjrCkdyYp19luyJjsnf7GIWATTch9Z")
                .build();
    }
}
