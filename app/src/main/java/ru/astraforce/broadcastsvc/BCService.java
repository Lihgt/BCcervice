package ru.astraforce.broadcastsvc;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

public class BCService extends Service {
    public static final int NOTIF_ID = 1;
    public static final String NOTIF_CHANNEL_ID = "Channel_Id";
    public static final String NOTIF_CHANNEL_Name = "Channel_Id";
    public static final String NOTIF_CHANNEL_Desc = "Channel_Id";

    public static String eventId;
    public static String bcName;
    public static String baseId;
    public static boolean ready;
    public static String inMessage;
    public static String outMessage;
    private BCsvcReceiver bcReciever;
    public BCService() {
        ready = false;
        outMessage = "com.google.android.c2dm.intent.RECEIVE";
        bcName = "barcode";
    }

    @Override
    public void onDestroy() {
        ready = false;
        Toast.makeText(this, "Сервис остановлен", Toast.LENGTH_SHORT).show();
        if(bcReciever != null)
            unregisterReceiver(bcReciever);

        Intent intent = new Intent(outMessage);
        intent.putExtra("text", "Сервис остановлен");
        this.sendBroadcast(intent);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Нет внешнего подключения ... еще");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        SharedPreferences sharedPreferences = getSharedPreferences("bcsvc_settings", 0);
        if (sharedPreferences.contains("edInMessage")) {
            inMessage = sharedPreferences.getString("edInMessage", "");
            bcName = sharedPreferences.getString("edInField", "barcode");
            outMessage = sharedPreferences.getString("edOutMessage", "");
            eventId = sharedPreferences.getString("edEventID", "1");
            baseId = sharedPreferences.getString("edBaseId", null);
            bcReciever = new BCsvcReceiver();
            registerReceiver(bcReciever, new IntentFilter(inMessage));

            Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            startForeground(NOTIF_ID, new Notification.Builder(this,
                    NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_astrastat_name)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Сервис работает")
                    .setContentIntent(pendingIntent)
                    .build());
            ready = true;
            Toast.makeText(this, "Ожидание оповещения", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Оповещения НЕ принимаются!", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }
}