package ru.astraforce.broadcastsvc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            context.startService(new Intent(context, BCService.class));
        }
        Toast.makeText(context, "Boot event " + intent.getAction(), Toast.LENGTH_LONG).show();
    }
}