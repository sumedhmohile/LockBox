package com.sumedh.lockbox;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

public class ProgressBarManager {
    private static ProgressBarFragment progressBarFragment;

    public static void showProgressBar(String message, FragmentManager fragmentManager) {
        Bundle progressBundle = new Bundle();
        progressBundle.putString("message", message);
        progressBarFragment = ProgressBarFragment.newInstance(message);

        progressBarFragment.show(fragmentManager, "Progress " + message);
    }

    public static void dismissProgressBar() {
        progressBarFragment.dismiss();
    }
}
