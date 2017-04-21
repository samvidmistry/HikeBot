package com.example.samvidmistry.hikebot.extractor;

import com.example.samvidmistry.hikebot.model.Model;

import java.util.List;

/**
 * Created by samvidmistry on 12/19/16.
 */

public interface ListContentExtractor {
    List<Model> getContent(String fullContent) throws Exception;
}
