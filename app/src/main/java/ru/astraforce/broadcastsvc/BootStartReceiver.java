package ru.astraforce.broadcastsvc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED){
            context.startService(new Intent(context, BCService.class));
        }
    }
}