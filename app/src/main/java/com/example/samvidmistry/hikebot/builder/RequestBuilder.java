package com.example.samvidmistry.hikebot.builder;

import okhttp3.Request;

/**
 * Created by samvidmistry on 12/15/16.
 */

public interface RequestBuilder {
    Request getRequest(String query) throws Exception;
}
