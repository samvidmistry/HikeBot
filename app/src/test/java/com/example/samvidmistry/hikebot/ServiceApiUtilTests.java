package com.example.samvidmistry.hikebot;

import com.example.samvidmistry.hikebot.builder.ServiceApiUtil;
import com.example.samvidmistry.hikebot.builder.TextUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by samvidmistry on 12/14/16.
 */

public class ServiceApiUtilTests {
    @Test
    public void testWikipediaUrlGeneration() throws Exception {
        String assertUrl = "https://en.wikipedia.org/w/api.php?" +
                "format=json&action=query&prop=extracts&exintro=&explaintext=" +
                "&titles=samvid+mistry";
        assertEquals(assertUrl, ServiceApiUtil.getWikipediaSearchUrl("samvid mistry"));
    }

    @Test
    public void testExtractionWikipedia() throws Exception {
        String op = "Samvid Mistry";
        assertEquals(op, TextUtil.extractSearchQueryWiki("Samvid Mistry @wiki"));
    }

    @Test
    public void testImdbUrlGeneration() throws Exception {
        String assertUrl = "http://www.omdbapi.com/?t=samvid+mistry&y=&plot=short&r=json";
        assertEquals(assertUrl, ServiceApiUtil.getImdbSearchUrl("samvid mistry"));
    }

    @Test
    public void testExtractionImdb() throws Exception {
        String op = "Samvid Mistry";
        assertEquals(op, TextUtil.extractSearchQueryImdb("Samvid Mistry @imdb"));
    }
}
