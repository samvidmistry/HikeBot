package com.example.samvidmistry.hikebot.extractor;

/**
 * Created by samvidmistry on 12/15/16.
 */

public interface ContentExtractor {
    CharSequence getContent(String fullContent) throws Exception;
}
