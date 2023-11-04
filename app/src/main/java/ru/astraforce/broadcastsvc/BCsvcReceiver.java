package ru.astraforce.broadcastsvc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BCsvcReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent parmintent) {
        String barcode;
        String bcName = BCService.bcName;
        Object o = parmintent.getExtras() != null ? parmintent.getExtras().get(bcName) : null;
        if(o == null)
        {
            Toast.makeText(context, "В данных нет ШК! ("+bcName+")", Toast.LENGTH_SHORT).show();
            return;
        }
        if(o.getClass().getName().contains("string"))
            barcode = parmintent.getStringExtra(bcName);
        else if(o.getClass().getName().contains("byte")) {
            byte[] arrayOfByte = parmintent.getByteArrayExtra(bcName);
            int l = parmintent.getIntExtra("length", 0);
            barcode = new String(arrayOfByte, 0, l);
        }
        else barcode = o.toString();

        if (barcode != null && barcode.length() > 0) {
            Intent intent = new Intent(BCService.outMessage);
            intent.putExtra("text", BCService.eventId);
            intent.putExtra("title", "1C");
            intent.putExtra("data", barcode);
            if (BCService.baseId != null && !BCService.baseId.equals(""))
                intent.putExtra("base", BCService.baseId);

            context.sendBroadcast(intent);
        }
    }
}