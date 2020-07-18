package com.sumedh.lockbox;

import android.content.Context;

import java.util.List;

import androidx.fragment.app.FragmentManager;

public class ImmutableBoxListAdapter extends BoxListAdapter {
    public ImmutableBoxListAdapter(Context context, List<Box> boxes, FragmentManager fragmentManager) {
        super(context, boxes, fragmentManager);
    }
}
