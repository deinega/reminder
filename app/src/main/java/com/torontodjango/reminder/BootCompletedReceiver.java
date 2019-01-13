package com.torontodjango.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver
{
    private final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(TAG, "onReceive");
        //new TaskListAdapter(context);
    }
}
