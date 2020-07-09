package com.sumedh.lockbox;

import android.text.format.DateUtils;

import java.util.concurrent.TimeUnit;

public enum CheckInFrequency {
    DAILY,
    WEEKLY,
    MONTHLY;

    public int getTime() {
        return 1;
    }
}
