package com.example.samvidmistry.hikebot.builder;

import android.support.annotation.VisibleForTesting;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by samvidmistry on 12/14/16.
 */

public class ServiceApiUtil {

    //No instances please
    private ServiceApiUtil(){}

    public static String getWikipediaSearchUrl(String title) throws UnsupportedEncodingException {
        title = URLEncoder.encode(title, "UTF-8");
        String baseUrl = "https://en.wikipedia.org/w/api.php?" +
                "format=json&action=query&prop=extracts&exintro=&explaintext=" +
                "&titles=%s";
        return String.format(baseUrl, title);
    }

    public static String getImdbSearchUrl(String title) throws UnsupportedEncodingException {
        title = URLEncoder.encode(title, "UTF-8");
        String baseUrl = "http://www.omdbapi.com/?t=%s&y=&plot=short&r=json";
        return String.format(baseUrl, title);
    }

    public static String getFancyTextUrl(String text) throws UnsupportedEncodingException {
        text = URLEncoder.encode(text, "UTF-8");
        String baseUrl = "https://ajith-Fancy-text-v1.p.mashape.com/text?text=%s";
        return String.format(baseUrl, text);
    }

    public static String getYouTubeSearchUrl(String text) throws UnsupportedEncodingException {
        text = URLEncoder.encode(text, "UTF-8");
        String baseUrl = "https://www.googleapis.com/youtube/v3/search?q=%s&part=id,snippet" +
                "&maxResults=15&typ=video&key=AIzaSyAP_cjUp68O9yhkJACtEXRW1-mm8JNBUJA";
        return String.format(baseUrl, text);
    }
}
