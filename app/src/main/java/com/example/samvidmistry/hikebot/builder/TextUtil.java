package com.example.samvidmistry.hikebot.builder;

import android.support.annotation.VisibleForTesting;

/**
 * Created by samvidmistry on 12/14/16.
 */

public class TextUtil {

    //No instances please
    private TextUtil(){}

    public static String extractSearchQueryWiki(String query) {
        return query.replaceAll(" @wiki", "");
    }

    public static String extractSearchQueryImdb(String query) {
        return query.replaceAll(" @imdb", "");
    }

    public static String extractTextQueryFancyText(String query) {
        return query.replaceAll(" @fancy", "");
    }

    public static String extractSearchQueryYouTube(String query) {
        return query.replaceAll(" @youtube", "");
    }

    public static String extractFormatQueryFormat(String query) {
        return query.replaceAll(" @format", "");
    }
}
