package com.debug;

import android.os.StrictMode;

/**
 * Created by bin.teng on 2016/3/15.
 */
public class StrictModeWrapper {
    static {
        try {
            Class.forName("android.os.StrictMode", true, Thread.currentThread().getContextClassLoader());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void checkAvailable() {
    }

    public static void enableDefaults() {
        StrictMode.enableDefaults();
    }
}