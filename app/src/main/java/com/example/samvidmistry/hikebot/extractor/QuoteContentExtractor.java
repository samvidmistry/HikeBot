package com.example.samvidmistry.hikebot.extractor;

import android.text.SpannableStringBuilder;

import org.json.JSONObject;

/**
 * Created by samvidmistry on 12/15/16.
 */

public class QuoteContentExtractor implements ContentExtractor {
    @Override
    public CharSequence getContent(String fullContent) throws Exception {
        JSONObject jsonObject = new JSONObject(fullContent);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(jsonObject.optString("quote")).append(" - ")
                .append(jsonObject.optString("author"));
        return builder.toString();
    }
}
