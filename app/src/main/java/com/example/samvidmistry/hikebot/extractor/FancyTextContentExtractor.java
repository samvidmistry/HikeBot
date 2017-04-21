package com.example.samvidmistry.hikebot.extractor;

import org.json.JSONObject;

/**
 * Created by samvidmistry on 12/15/16.
 */

public class FancyTextContentExtractor implements ContentExtractor {
    @Override
    public CharSequence getContent(String fullContent) throws Exception {
        JSONObject jsonObject = new JSONObject(fullContent);
        return jsonObject.optString("fancytext");
    }
}
