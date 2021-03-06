package in.zbic.pikaczu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by krzysiek on 28.10.17.
 */

public class NotificationService extends Service {
    static final String LOG_TAG = "NotificationService";

    private HandlerThread handlerThread;
    private BeeperHandler handler;
    private boolean isEnabled = true;
    private NotificationManager notificationManager;
    private PendingIntent pausePendingIntent;
    private PendingIntent playPendingIntent;
    private PendingIntent nextPendingIntent;
    private PendingIntent previousPendingIntent;
    private PendingIntent stopPendingIntent;
    private long timeStarted = 0;

    private PendingIntent createPendingIntent(String action) {
        Intent intent = new Intent(this, NotificationService.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        return pendingIntent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.timeStarted = System.currentTimeMillis();
    }

    public Looper getLooper() {
        return this.handlerThread.getLooper();
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.MAIN_SERVICE_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_name);
        String title = getResources().getString(R.string.app_name);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(this.getFormattedTimeElapsed())
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        if(this.isEnabled) {
            //builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "", this.stopPendingIntent);
                   //.addAction(android.R.drawable.ic_media_pause, "", this.pausePendingIntent)
                   //.addAction(android.R.drawable.ic_media_next, "", this.nextPendingIntent);
        } else {
            //builder.addAction(android.R.drawable.ic_media_previous, "", this.previousPendingIntent)
            //       .addAction(android.R.drawable.ic_media_play, "", this.playPendingIntent)
            //       .addAction(android.R.drawable.ic_media_next, "", this.nextPendingIntent);
        }

        return builder.build();
    }

    private String getFormattedTimeElapsed() {
        long interval = System.currentTimeMillis() - this.timeStarted;
        long hr = TimeUnit.MILLISECONDS.toHours(interval);
        long min = TimeUnit.MILLISECONDS.toMinutes(interval) %60;
        long sec = TimeUnit.MILLISECONDS.toSeconds(interval) %60;
        return String.format("%02d:%02d:%02d", hr, min, sec);
    }

    private void startService(ArrayList<Integer> periods) {
        Log.i(LOG_TAG, "Received Start Service Intent ");
        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        this.nextPendingIntent = this.createPendingIntent(Constants.NEXT_SERVICE_ACTION);
        this.previousPendingIntent = this.createPendingIntent(Constants.PREVIOUS_SERVICE_ACTION);
        this.pausePendingIntent = this.createPendingIntent(Constants.PAUSE_SERVICE_ACTION);
        this.playPendingIntent = this.createPendingIntent(Constants.PLAY_SERVICE_ACTION);
        this.stopPendingIntent = this.createPendingIntent(Constants.STOP_SERVICE_ACTION);

        this.handlerThread = new HandlerThread(getResources().getString(R.string.app_name));
        this.handlerThread.start();

        Notification notification = this.createNotification();
        startForeground(Constants.SERVICE_ID, notification);
    }

    private void stopService() {
        Log.i(LOG_TAG, "Received Stop Foreground Intent");
        this.handlerThread.quit();
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        switch (action) {
            case Constants.START_SERVICE_ACTION:
                Bundle extras = intent.getExtras();
                ArrayList<Integer> periods = extras.getIntegerArrayList(Constants.CHOSEN_PERIODS);
                this.startService(periods);
                break;
            case Constants.PREVIOUS_SERVICE_ACTION:
                Log.i(LOG_TAG, "Clicked Previous");
                this.updateNotification();
                break;
            case Constants.NEXT_SERVICE_ACTION:
                Log.i(LOG_TAG, "Clicked Next");
                this.updateNotification();
                break;
            case Constants.PAUSE_SERVICE_ACTION:
                Log.i(LOG_TAG, "Clicked Pause");
                this.isEnabled = false;
                Message message = this.handler.obtainMessage();
                this.handler.sendMessage(message);
                this.updateNotification();
                break;
            case Constants.PLAY_SERVICE_ACTION:
                Log.i(LOG_TAG, "Clicked Play");
                this.isEnabled = true;
                this.updateNotification();
                break;
            case Constants.STOP_SERVICE_ACTION:
                this.stopService();
                break;
        }
        return START_STICKY;
    }

    public void updateNotification() {
        this.notificationManager.notify(Constants.SERVICE_ID, this.createNotification());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }
}
