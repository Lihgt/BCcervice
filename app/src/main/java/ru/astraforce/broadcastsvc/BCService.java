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
    public static final int NOTIFY_ID = 1;
    public static final String NOTIFY_CHANNEL_ID = "AstraBroadcast_ChanId";
    public static final String NOTIFY_CHANNEL_Name = "Astraforce Broadcast";
    public static final String NOTIFY_CHANNEL_Desc = "Broadcast service notification";

    public static final String VERSION = "1.2.3";

    public static String lastBC;
    public static String eventId;
    public static String bcName;
    public static String baseId;
    public static boolean ready;
    public static String inMessage;
    public static String outMessage;
    public static String outCategory;
    private BCsvcReceiver bcReceiver;
    private BCsvcReceiver bcReceiverATOL;
    private BCsvcReceiver bcReceiverASTRA;
    public BCService() {
        ready = false;
        outMessage = "com.google.android.c2dm.intent.RECEIVE";
        bcName = "DATA";
        lastBC = "";
    }

    public IBinder onBind(Intent intent){
        throw new UnsupportedOperationException("Bind не реализован");
    }

    @Override
    public void onDestroy() {
        ready = false;
        lastBC = "";
        Toast.makeText(this, "Сервис остановлен", Toast.LENGTH_SHORT).show();
        if(bcReceiver != null)
            unregisterReceiver(bcReceiver);
        if(bcReceiverATOL != null)
            unregisterReceiver(bcReceiverATOL);
        if(bcReceiverASTRA != null)
            unregisterReceiver(bcReceiverASTRA);

        Intent intent = new Intent(outMessage);
        if(outCategory != null && outCategory.length() > 0)
            intent.addCategory(outCategory);
        intent.putExtra("text", "Сервис остановлен");
        this.sendBroadcast(intent);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        SharedPreferences sharedPreferences = getSharedPreferences("bcsvc_settings", 0);
        if (!sharedPreferences.contains("edInMessage")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("edInMessage", "ActionName");
            editor.apply();
            editor.putString("edInField", "BarcodeData");
            editor.apply();
            editor.putString("edOutMessage", "com.google.android.c2dm.intent.RECEIVE");
            editor.apply();
            editor.putString("edOutCategory", "com.google.android.gms");
            editor.apply();
            editor.putString("edEventId", "1");
            editor.apply();
            editor.putString("edBaseId", "");
            editor.apply();
        }
        inMessage = sharedPreferences.getString("edInMessage", "");
        bcName = sharedPreferences.getString("edInField", "barcode");
        outMessage = sharedPreferences.getString("edOutMessage", "");
        outCategory = sharedPreferences.getString("edOutCategory", "");
        eventId = sharedPreferences.getString("edEventID", "1");
        baseId = sharedPreferences.getString("edBaseId", null);
        if(inMessage.length() > 0) {
            bcReceiver = new BCsvcReceiver();
            registerReceiver(bcReceiver, new IntentFilter(inMessage));
            bcReceiverATOL = new BCsvcReceiver();
            registerReceiver(bcReceiverATOL, new IntentFilter("BARCODE_DECODING_BROADCAST"));
            bcReceiverASTRA = new BCsvcReceiver();
            registerReceiver(bcReceiverASTRA, new IntentFilter("ActionName"));

            Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            startForeground(NOTIFY_ID, new Notification.Builder(this,
                    NOTIFY_CHANNEL_ID) // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_astrastat_name)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Сервис работает")
                    .setContentIntent(pendingIntent)
                    .build());
            ready = true;
            Toast.makeText(this, "Ожидание оповещений от сканера", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Оповещение НЕ настроено", Toast.LENGTH_SHORT).show();

        lastBC = "";
        return START_STICKY;
    }
}