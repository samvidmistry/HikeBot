package com.example.samvidmistry.hikebot;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by samvidmistry on 12/19/16.
 */

public interface UpdateNodeContentInterface {
    void updateNodeContent(CharSequence content, AccessibilityNodeInfoCompat node);
}
