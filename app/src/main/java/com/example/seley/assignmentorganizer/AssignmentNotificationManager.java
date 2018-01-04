package com.example.seley.assignmentorganizer;


import android.app.AlarmManager;
import android.app.Notification;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.UUID;

public class AssignmentNotificationManager {

    private static AlarmManager mAlarmManager;
    private Context mContext;
    private static HashMap<UUID, PendingIntent> hashMap;

    public AssignmentNotificationManager (Context context)
    {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        hashMap = new HashMap<>();
    }

    public void addNotification(Assignment assignment)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(assignment.getDueDate());
        long remainingTime = calendar.getTimeInMillis() - System.currentTimeMillis();
        Log.d("TIME", "Due date: " + assignment.getDueDate() + ", time remaining: " + String.valueOf(remainingTime/1000));

        Intent resultIntent = Receiver.newIntent(mContext, assignment.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        Log.d("INTENT", "INTENT adding: " + pendingIntent.toString());
        hashMap.put(assignment.getId(), pendingIntent);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + remainingTime, pendingIntent);
    }

    public static void removeNotification(Assignment assignment)
    {
        PendingIntent pendingIntent = hashMap.remove(assignment.getId());
        if (pendingIntent != null)
        {
            Log.d("INTENT", "INTENT removing: " + pendingIntent.toString());
            mAlarmManager.cancel(pendingIntent);
        }
    }

    public void updateNotification(Assignment assignment)
    {
        if (hashMap.get(assignment.getId()) != null) {
            removeNotification(assignment);
            addNotification(assignment);
        }
    }

public static class Receiver extends BroadcastReceiver {

    public static String ASSIGNMENT_TAG = "Receiver.Assignment";

    public static Intent newIntent(Context packageContext, UUID uuid) {
        Intent intent = new Intent(packageContext, Receiver.class);
        intent.putExtra(ASSIGNMENT_TAG, uuid);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        UUID assignmentID = (UUID) intent.getSerializableExtra(ASSIGNMENT_TAG);
        Assignment assignment = AssignmentStorage.get(context).getAssignment(assignmentID);
        if (assignment != null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel");
            builder.setSmallIcon(R.drawable.notification_icon);
            builder.setContentTitle("Assignment Notification");
            builder.setContentText(assignment.getTitle() + " for " +
                    assignment.getSubject() + " is due.");

            Log.d("TIME", "Notification being made");

            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

            //LED
            builder.setLights(Color.RED, 3000, 3000);

            Intent resultIntent = DetailsActivity.newIntent(context, assignment.getId());
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
            removeNotification(assignment);
        }
    }
}
}
