package com.example.samvidmistry.hikebot.extractor;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import org.json.JSONObject;

/**
 * Created by samvidmistry on 12/15/16.
 */

public class WikipediaContentExtractor implements ContentExtractor {
    @Override
    public CharSequence getContent(String fullContent) throws Exception {
        JSONObject jsonObject = new JSONObject(fullContent);
        JSONObject pages = jsonObject.getJSONObject("query").getJSONObject("pages");
        String title = pages.getJSONObject(pages.keys().next()).optString("title");
        SpannableString extractedContent = new SpannableString(title + "\n\n" +
                pages.getJSONObject(pages.keys().next()).optString("extract"));
        extractedContent.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return extractedContent;
    }
}
