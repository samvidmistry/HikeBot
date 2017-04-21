package com.example.samvidmistry.hikebot.builder;

import okhttp3.Request;

/**
 * Created by samvidmistry on 12/15/16.
 */

public class QuoteRequestBuilder implements RequestBuilder {
    @Override
    public Request getRequest(String query) throws Exception {
        return new Request.Builder().url("https://andruxnet-random-famous-quotes.p.mashape.com/" +
                "?cat=famous")
                .addHeader("X-Mashape-Key", "iEYj2Fnawomsh3aW6oFLIjrCkdyYp19luyJjsnf7GIWATTch9Z")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
    }
}
