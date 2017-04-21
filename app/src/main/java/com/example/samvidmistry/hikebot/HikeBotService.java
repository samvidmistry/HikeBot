package com.example.samvidmistry.hikebot;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityWindowInfoCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.samvidmistry.hikebot.adapter.YouTubeListAdapter;
import com.example.samvidmistry.hikebot.builder.FancyTextRequestBuilder;
import com.example.samvidmistry.hikebot.builder.ImdbRequestBuilder;
import com.example.samvidmistry.hikebot.builder.QuoteRequestBuilder;
import com.example.samvidmistry.hikebot.builder.RequestBuilder;
import com.example.samvidmistry.hikebot.builder.TextUtil;
import com.example.samvidmistry.hikebot.builder.WikipediaRequestBuilder;
import com.example.samvidmistry.hikebot.builder.YouTubeRequestBuilder;
import com.example.samvidmistry.hikebot.extractor.ContentExtractor;
import com.example.samvidmistry.hikebot.extractor.FancyTextContentExtractor;
import com.example.samvidmistry.hikebot.extractor.ImdbContentExtractor;
import com.example.samvidmistry.hikebot.extractor.ListContentExtractor;
import com.example.samvidmistry.hikebot.extractor.QuoteContentExtractor;
import com.example.samvidmistry.hikebot.extractor.WikipediaContentExtractor;
import com.example.samvidmistry.hikebot.extractor.YouTubeListContentExtractor;
import com.example.samvidmistry.hikebot.model.Model;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by samvidmistry on 12/13/16.
 */

public class HikeBotService extends AccessibilityService implements UpdateNodeContentInterface {
    private static final String TAG = "HikeBotService";
    private static final String service = "com.example.samvidmistry.hikebot/.HikeBotService";
    private OkHttpClient mClient;
    private View mMainLayout;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (mClient == null) {
            Log.e(TAG, "Client uninitialized");
            return;
        }

        AccessibilityNodeInfoCompat source = AccessibilityEventCompat.asRecord(
                accessibilityEvent).getSource();
        if (source != null) {
            source.refresh();

            List<AccessibilityNodeInfoCompat> accessibilityNodeInfosByViewId =
                    source.findAccessibilityNodeInfosByViewId("com.bsb.hike:id/msg_compose");

            /*List<AccessibilityNodeInfoCompat> accessibilityNodeInfosByViewId = new ArrayList<>(1);
            accessibilityNodeInfosByViewId.add(source);*/

            if (accessibilityNodeInfosByViewId.size() <= 0 ||
                    accessibilityNodeInfosByViewId.get(0) == null ||
                    !accessibilityNodeInfosByViewId.get(0).getViewIdResourceName()
                            .equals(source.getViewIdResourceName())) {
                return;
            }

            //Log.e(TAG, "onAccessibilityEvent: View id is " + source.getViewIdResourceName());
            /*List<AccessibilityNodeInfoCompat> sendButton =
                    AccessibilityWindowInfoCompat.obtain().getRoot()
                            .findAccessibilityNodeInfosByViewId("com.bsb.hike:id/attachment_view");*/

            String query = accessibilityNodeInfosByViewId.get(0).getText().toString();
            if (!query.endsWith("@wiki") && !query.endsWith("@imdb")
                    && !query.endsWith("@quote") && !query.endsWith("@fancy")
                    && !query.endsWith("@help") && !query.endsWith("@youtube")
                    && !query.endsWith("@format")) {
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
                    AccessibilityNodeInfoCompat.MOVEMENT_GRANULARITY_PARAGRAPH);
            bundle.putBoolean(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN,
                    true);
            accessibilityNodeInfosByViewId.get(0).
                    performAction(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY,
                            bundle);

            String[] split = query.split("\\s+");
            switch (split[split.length - 1]) {
                case "@wiki":
                    new InfoRequestAsyncTask(accessibilityNodeInfosByViewId.get(0).getText()
                            .toString(), accessibilityNodeInfosByViewId.get(0),
                            new WikipediaContentExtractor(), new WikipediaRequestBuilder())
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;

                case "@imdb":
                    new InfoRequestAsyncTask(accessibilityNodeInfosByViewId.get(0).getText()
                            .toString(), accessibilityNodeInfosByViewId.get(0),
                            new ImdbContentExtractor(), new ImdbRequestBuilder())
                            .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    break;

                case "@quote":
                    new InfoRequestAsyncTask(accessibilityNodeInfosByViewId.get(0).getText()
                            .toString(), accessibilityNodeInfosByViewId.get(0),
                            new QuoteContentExtractor(), new QuoteRequestBuilder())
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;

                case "@fancy":
                    new InfoRequestAsyncTask(accessibilityNodeInfosByViewId.get(0).getText()
                            .toString(), accessibilityNodeInfosByViewId.get(0),
                            new FancyTextContentExtractor(), new FancyTextRequestBuilder())
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;

                case "@help":
                    StringBuilder builder = new StringBuilder();
                    builder.append("<query> @wiki: search anything on wikipedia\n");
                    builder.append("<title> @imdb: get info and ratings from IMDb\n");
                    builder.append("@quote: get random famous quotes\n");
                    builder.append("<text> @fancy: convert text to fancy fonts\n");
                    builder.append("<query> @youtube: search anything on YouTube and click will " +
                            "copy the link of video");
                    updateTextContent(builder.toString(), accessibilityNodeInfosByViewId.get(0));
                    break;

                case "@format":
                    SpannableString q = new SpannableString(
                            TextUtil.extractFormatQueryFormat(query)
                    );
                    q.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, q.length(),
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    updateTextContent(q, accessibilityNodeInfosByViewId.get(0));
                    break;

                case "@youtube":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                            !Settings.canDrawOverlays(this)) {
                        Intent intent = new Intent(this, SampleActivity.class);
                        intent.putExtra(SampleActivity.REQUEST_SYSTEM_DRAW_PERMISSION, true);
                        startActivity(intent);
                        return;
                    }

                    final WindowManager windowManager = (WindowManager)
                            getSystemService(WINDOW_SERVICE);
                    final FrameLayout mainLayout = new FrameLayout(this) {
                        @Override
                        public boolean dispatchKeyEventPreIme(KeyEvent event) {
                            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                removeWindow("@youtube");
                                return true;
                            }
                            return super.dispatchKeyEventPreIme(event);
                        }
                    };

                    final LinearLayout verticalLayout = new LinearLayout(this);
                    verticalLayout.setBackgroundColor(Color.BLUE);
                    verticalLayout.setOrientation(LinearLayout.VERTICAL);
                    verticalLayout.setId(R.id.rootLayout);

                    final FrameLayout rvProgressBarContainer = new FrameLayout(this);
                    rvProgressBarContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    rvProgressBarContainer.setBackgroundColor(Color.WHITE);

                    final RecyclerView recyclerView = new RecyclerView(this);
                    recyclerView.setLayoutParams(new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setHasFixedSize(true);

                    final ProgressBar progressBar = new ProgressBar(this, null,
                            android.R.attr.progressBarStyleLarge);
                    progressBar.setLayoutParams(new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER
                    ));
                    progressBar.setVisibility(View.GONE);

                    final Point point = new Point();
                    windowManager.getDefaultDisplay().getSize(point);

                    verticalLayout.getViewTreeObserver().addOnPreDrawListener(
                            new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            verticalLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                            //int height = floating.getHeight();
                            int[] location = new int[2];
                            verticalLayout.getLocationOnScreen(location);
                            ObjectAnimator animator =
                                    ObjectAnimator.ofFloat(verticalLayout, View.Y, point.y,
                                            location[1]);
                            animator.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    progressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            animator.setDuration(getResources()
                                    .getInteger(android.R.integer.config_mediumAnimTime));
                            animator.setInterpolator(new OvershootInterpolator());
                            animator.start();
                            return true;
                        }
                    });

                    Toolbar toolbar = new Toolbar(this);
                    toolbar.setTitle(TextUtil.extractSearchQueryYouTube(
                            accessibilityNodeInfosByViewId.get(0).getText()
                                    .toString()
                    ));
                    toolbar.setId(R.id.toolbar);
                    toolbar.setTitleTextColor(Color.WHITE);
                    toolbar.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64,
                                    getResources().getDisplayMetrics())
                    ));
                    toolbar.setBackgroundColor(Color.RED);

                    toolbar.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            removeWindow("@youtube");
                            return true;
                        }
                    });

                    rvProgressBarContainer.addView(recyclerView);
                    rvProgressBarContainer.addView(progressBar);

                    verticalLayout.addView(toolbar);
                    verticalLayout.addView(rvProgressBarContainer);

                    mainLayout.addView(verticalLayout);

                    WindowManager.LayoutParams layoutParams =
                            new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                                    point.y, 0, 0,
                                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                                    PixelFormat.TRANSPARENT);
                    layoutParams.gravity = Gravity.BOTTOM | GravityCompat.END;
                    windowManager.addView(mainLayout, layoutParams);
                    mMainLayout = mainLayout;

                    new ListInfoRequestAsyncTask(accessibilityNodeInfosByViewId.get(0).getText()
                            .toString(), accessibilityNodeInfosByViewId.get(0),
                            recyclerView, progressBar,
                            ListInfoRequestAsyncTask.CONTENT_TYPE_YOUTUBE,
                            new YouTubeListContentExtractor(),
                            new YouTubeRequestBuilder())
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    break;
            }
        }
    }

    private void removeWindow(String windowType) {
        switch (windowType) {
            case "@youtube":
                final View mainLayout = mMainLayout;
                final Point point = new Point();
                ((WindowManager) getSystemService(WINDOW_SERVICE))
                        .getDefaultDisplay().getSize(point);
                ObjectAnimator animator =
                        ObjectAnimator.ofFloat(mainLayout.findViewById(R.id.rootLayout),
                                View.Y, point.y);
                animator.setDuration(getResources()
                        .getInteger(android.R.integer.config_mediumAnimTime));
                animator.setInterpolator(new AccelerateInterpolator());
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mainLayout.getParent() != null) {
                            ((WindowManager) getSystemService(WINDOW_SERVICE))
                                    .removeView(mainLayout);
                            mMainLayout = null;
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
                break;

        }
    }

    public static boolean isServiceEnabled(Context context) {
        AccessibilityManager manager = (AccessibilityManager) context
                .getSystemService(ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> infoList =
                manager.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo serv :
                infoList) {
            if (serv.getId().equals(service)) {
                return true;
            }
        }

        return false;
    }

    private void updateTextContent(CharSequence content, AccessibilityNodeInfoCompat nodeInfo) {
        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(CLIPBOARD_SERVICE);
        SpannableString string = new SpannableString(content);
        ClipData clipData =
                ClipData.newPlainText("service text", string);
        clipboardManager.setPrimaryClip(clipData);
        nodeInfo.performAction(AccessibilityNodeInfoCompat.ACTION_PASTE);
    }

    private void updateListContent(List<Model> content, AccessibilityNodeInfoCompat nodeInfo,
                                   RecyclerView recyclerView, int contentType) {
        RecyclerView.Adapter adapter = null;
        switch (contentType) {
            case ListInfoRequestAsyncTask.CONTENT_TYPE_YOUTUBE:
                adapter = new YouTubeListAdapter(content, nodeInfo, this);
                recyclerView.addItemDecoration(new DividerItemDecoration(this,
                        DividerItemDecoration.VERTICAL));
                break;
        }

        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        if (mClient == null) {
            mClient = new OkHttpClient();
        }
    }

    private void unregister() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onInterrupt() {
        unregister();
    }

    @Override
    public void onDestroy() {
        unregister();
        super.onDestroy();
    }

    @Override
    public void updateNodeContent(CharSequence content, AccessibilityNodeInfoCompat node) {
        updateTextContent(content, node);

        if (mMainLayout != null) {
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.removeView(mMainLayout);
            mMainLayout = null;
        }
    }

    private class ListInfoRequestAsyncTask extends AsyncTask<Void, Void, List<Model>> {
        private static final int CONTENT_TYPE_YOUTUBE = 398;

        private String mUrl;
        private AccessibilityNodeInfoCompat mNode;
        private RecyclerView mRecyclerView;
        private ProgressBar mProgressBar;
        private int mContentType;
        private ListContentExtractor mListContentExtractor;
        private RequestBuilder mRequestBuilder;

        private ListInfoRequestAsyncTask(String url, AccessibilityNodeInfoCompat node,
                                         RecyclerView recyclerView,
                                         ProgressBar progressBar, int contentType,
                                         ListContentExtractor listContentExtractor,
                                         RequestBuilder requestBuilder) {
            mUrl = url;
            mNode = node;
            mRecyclerView = recyclerView;
            mProgressBar = progressBar;
            this.mContentType = contentType;
            mListContentExtractor = listContentExtractor;
            mRequestBuilder = requestBuilder;
        }

        @Override
        protected List<Model> doInBackground(Void... params) {
            try {
                Request request = mRequestBuilder.getRequest(mUrl);

                Response execute = mClient.newCall(request).execute();
                String response = execute.body().string();
                return mListContentExtractor.getContent(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Model> models) {
            if (models == null) {
                return;
            }

            if (mProgressBar != null) {
                mProgressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }

            updateListContent(models, mNode, mRecyclerView, mContentType);
        }
    }


    private class InfoRequestAsyncTask extends AsyncTask<Void, Void, CharSequence> {
        private String mUrl;
        private AccessibilityNodeInfoCompat mNode;
        private ContentExtractor mContentExtractor;
        private RequestBuilder mRequestBuilder;

        private InfoRequestAsyncTask(String url, AccessibilityNodeInfoCompat node,
                                     ContentExtractor contentExtractor,
                                     RequestBuilder requestBuilder) {
            mUrl = url;
            mNode = node;
            mContentExtractor = contentExtractor;
            mRequestBuilder = requestBuilder;
        }

        @Override
        protected CharSequence doInBackground(Void... params) {
            try {
                Request request = mRequestBuilder.getRequest(mUrl);

                Response execute = mClient.newCall(request).execute();
                String response = execute.body().string();
                return mContentExtractor.getContent(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CharSequence content) {
            if (content == null) {
                return;
            }
            updateTextContent(content, mNode);
        }
    }

}