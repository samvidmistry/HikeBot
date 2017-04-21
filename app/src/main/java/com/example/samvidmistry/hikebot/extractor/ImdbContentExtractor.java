package com.example.samvidmistry.hikebot.extractor;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

import org.json.JSONObject;

/**
 * Created by samvidmistry on 12/15/16.
 */

public class ImdbContentExtractor implements ContentExtractor {
    @Override
    public CharSequence getContent(String fullContent) throws Exception {
        JSONObject object = new JSONObject(fullContent);
        if (!object.optBoolean("Response", false)) {
            return "No content with such title found";
        }

        String title = object.optString("Title");
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(title).append("\n");
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, title.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append("Year: ").append(object.optString("Year")).append("\n");
        builder.append("Rated: ").append(object.optString("Rated")).append("\n");
        builder.append("Released: ").append(object.optString("Released")).append("\n");
        builder.append("Runtime: ").append(object.optString("Runtime")).append("\n");
        builder.append("Genre: ").append(object.optString("Genre")).append("\n");
        builder.append("Actors: ").append(object.optString("Actors")).append("\n");
        builder.append("Language: ").append(object.optString("Language")).append("\n");
        builder.append("Country: ").append(object.optString("Country")).append("\n");
        builder.append("Type: ").append(object.optString("Type")).append("\n");
        if (object.has("totalSeasons")) {
            builder.append("Total Seasons: ").append(object.optString("totalSeasons")).append("\n");
        }
        builder.append("IMDb rating: ").append(object.optString("imdbRating")).append("\n");
        builder.append("Poster: ").append(object.optString("Poster")).append("\n");
        builder.append("Plot: ").append(object.optString("Plot"));
        return builder.toString();
    }
}
