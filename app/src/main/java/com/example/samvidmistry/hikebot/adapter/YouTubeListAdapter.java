package com.example.samvidmistry.hikebot.adapter;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.samvidmistry.hikebot.HikeBotService;
import com.example.samvidmistry.hikebot.UpdateNodeContentInterface;
import com.example.samvidmistry.hikebot.databinding.YoutubeListItemBinding;
import com.example.samvidmistry.hikebot.model.Model;
import com.example.samvidmistry.hikebot.model.YouTubeSearchItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by samvidmistry on 12/19/16.
 */

public class YouTubeListAdapter extends RecyclerView.Adapter<YouTubeListAdapter.ViewHolder> {
    private List<Model> mSearchItems;
    private AccessibilityNodeInfoCompat mNode;
    private UpdateNodeContentInterface mUpdateNodeContentInterface;

    public YouTubeListAdapter(List<Model> searchItems, AccessibilityNodeInfoCompat node,
                              UpdateNodeContentInterface updateNodeContentInterface) {
        mNode = node;
        mUpdateNodeContentInterface = updateNodeContentInterface;
        if (!(searchItems.get(0) instanceof YouTubeSearchItem)) {
            throw new IllegalArgumentException("Required: YouTubeSearchItem, provided: " +
                        searchItems.get(0).getClass().getSimpleName());
        }
        mSearchItems = searchItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final YoutubeListItemBinding binding = YoutubeListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final YoutubeListItemBinding binding = holder.mBinding;
        binding.setVideo((YouTubeSearchItem) mSearchItems.get(position));
        binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpdateNodeContentInterface.updateNodeContent("https://www.youtube.com/watch?v=" +
                        binding.getVideo().getVideoId(), mNode);
            }
        });
        Picasso.with(binding.getRoot().getContext()).load(binding.getVideo().getThumbnailUrl())
                .into(binding.thumbImage);
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSearchItems == null ? 0 : mSearchItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        YoutubeListItemBinding mBinding;

        ViewHolder(YoutubeListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
