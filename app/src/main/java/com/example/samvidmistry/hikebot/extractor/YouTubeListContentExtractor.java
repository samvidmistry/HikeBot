package com.example.samvidmistry.hikebot.extractor;

import com.example.samvidmistry.hikebot.model.Model;
import com.example.samvidmistry.hikebot.model.YouTubeSearchItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samvidmistry on 12/19/16.
 */

public class YouTubeListContentExtractor implements ListContentExtractor {
    @Override
    public List<Model> getContent(String fullContent) throws Exception {
        JSONObject jsonObject = new JSONObject(fullContent);
        JSONArray items = jsonObject.getJSONArray("items");

        if (items.length() <= 0) {
            return null;
        }

        List<Model> models = new ArrayList<>();
        for (int i = 0, size = items.length(); i < size; i++) {
            JSONObject item = items.getJSONObject(i);
            YouTubeSearchItem youTubeSearchItem = new YouTubeSearchItem(
                    item.getJSONObject("id").optString("videoId"),
                    item.getJSONObject("snippet").optString("title"),
                    item.getJSONObject("snippet").optJSONObject("thumbnails")
                            .optJSONObject("default").optString("url"),
                    item.getJSONObject("snippet").optString("channelTitle")
            );
            models.add(youTubeSearchItem);
        }

        return models;
    }
}
