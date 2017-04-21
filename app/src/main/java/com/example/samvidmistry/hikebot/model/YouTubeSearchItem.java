package com.example.samvidmistry.hikebot.model;

/**
 * Created by samvidmistry on 12/19/16.
 */

public class YouTubeSearchItem extends Model {
    private String mVideoId;
    private String mTitle;
    private String mThumbnailUrl;
    private String mChannelTitle;

    public YouTubeSearchItem(String videoId, String title, String thumbnailUrl, String channelTitle) {
        mVideoId = videoId;
        mTitle = title;
        mThumbnailUrl = thumbnailUrl;
        mChannelTitle = channelTitle;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public String getChannelTitle() {
        return mChannelTitle;
    }
}
