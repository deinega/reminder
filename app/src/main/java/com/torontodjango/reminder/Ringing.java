package com.torontodjango.reminder;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.provider.Settings;


public class Ringing extends Activity {

    private final String TAG = "Ringing";

    private class PlayTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            Log.d(TAG, "PalyTimerTask.run()");
            //addNotification(task);
            finish();
        }
    }

    private Ringtone ringtone;
    private long playTime;
    private Timer timer = null;
    private PlayTimerTask playTimerTask;

    Task task;
    private TextView textView;

    @Override
    protected void onCreate(Bundle bundle)
    {
        Log.i(TAG, "onCreate");

        super.onCreate(bundle);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.ringing);

        textView = (TextView)findViewById(R.id.name);

        playTime = (long)30000;
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);

        start(getIntent());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        stop();
    }

    private void start(Intent intent)
    {
        Log.d(TAG, "Start ringing...");

        task = new Task();
        task.fromIntent(intent);

        textView.setText(task.getName());

        playTimerTask = new PlayTimerTask();
        timer = new Timer();
        timer.schedule(playTimerTask, playTime);
        ringtone.play();
    }

    private void stop()
    {
        Log.d(TAG, "Stop ringing...");
        timer.cancel();
        ringtone.stop();
    }

    public void onDismissClick(View view)
    {
        finish();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }


    private void addNotification(Task task)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        PendingIntent activity;
        Intent intent;

        Log.i(TAG, "adding notification...");

        intent = new Intent(this.getApplicationContext(), DashBoardActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        activity = PendingIntent.getActivity(this, (int)task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel channel = new NotificationChannel("alarmme_01", "AlarmMe Notifications",
                NotificationManager.IMPORTANCE_DEFAULT);

        notification = new Notification.Builder(this)
                .setContentIntent(activity)
                .setSmallIcon(R.drawable.ringing)
                .setAutoCancel(true)
                .setContentTitle("Missed alarm: " + task.getName())
                .setContentText(DAO.formatDate(task))
                .setChannelId("alarmme_01")
                .build();

        notificationManager.createNotificationChannel(channel);

        notificationManager.notify((int)task.getId(), notification);

        Log.d(TAG, "notification added");
    }

}
