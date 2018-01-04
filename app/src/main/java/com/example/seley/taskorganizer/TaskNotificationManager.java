package com.example.seley.taskorganizer;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class TaskNotificationManager {

    private static AlarmManager mAlarmManager;
    private Context mContext;
    private static HashMap<UUID, PendingIntent> hashMap;

    public TaskNotificationManager(Context context)
    {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        hashMap = new HashMap<>();
    }

    public void addNotification(Task task)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(task.getDueDate());
        long remainingTime = calendar.getTimeInMillis() - System.currentTimeMillis();
        Log.d("TIME", "Due date: " + task.getDueDate() + ", time remaining: " + String.valueOf(remainingTime/1000));

        Intent resultIntent = Receiver.newIntent(mContext, task.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        Log.d("INTENT", "INTENT adding: " + pendingIntent.toString());
        hashMap.put(task.getId(), pendingIntent);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + remainingTime, pendingIntent);
    }

    public static void removeNotification(Task task)
    {
        PendingIntent pendingIntent = hashMap.remove(task.getId());
        if (pendingIntent != null)
        {
            Log.d("INTENT", "INTENT removing: " + pendingIntent.toString());
            mAlarmManager.cancel(pendingIntent);
        }
    }

    public void updateNotification(Task task)
    {
        if (hashMap.get(task.getId()) != null) {
            removeNotification(task);
            addNotification(task);
        }
    }

public static class Receiver extends BroadcastReceiver {

    public static String TASK_TAG = "Receiver.Task";

    public static Intent newIntent(Context packageContext, UUID uuid) {
        Intent intent = new Intent(packageContext, Receiver.class);
        intent.putExtra(TASK_TAG, uuid);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        UUID taskID = (UUID) intent.getSerializableExtra(TASK_TAG);
        Task task = TaskStorage.get(context).getTask(taskID);
        if (task != null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel");
            builder.setSmallIcon(R.drawable.notification_icon);
            builder.setContentTitle("Task Notification");
            builder.setContentText(task.getTitle() + " for " +
                    task.getSubject() + " is due.");

            Log.d("TIME", "Notification being made");

            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

            //LED
            builder.setLights(Color.RED, 3000, 3000);

            Intent resultIntent = DetailsActivity.newIntent(context, task.getId());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel",
                        "readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);

                mNotificationManager.createNotificationChannel(channel);
            }

            mNotificationManager.notify(0, builder.build());
            removeNotification(task);
        }
    }
}
}
