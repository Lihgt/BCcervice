package ru.astraforce.broadcastsvc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class GetData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();

        try {
            switch (i.getAction()){
                case "ru.astraforce.GET_BARCODE":
                    if(BCService.lastBC != null && BCService.lastBC.length() > 0)
                        i.putExtra("barcode", BCService.lastBC);
                    break;
                case "ru.astraforce.GET_PARAMS":
                    i.putExtra("inMessage", BCService.inMessage);
                    i.putExtra("bcName", BCService.bcName);
                    break;
                case "ru.astraforce.SET_PARAMS":
                    i.putExtra("barcode", "234234234");//BCService.lastBC);
                    break;
                case "ru.astraforce.START_SERVICE":
                    startService(new Intent((Context)this, BCService.class));
                    break;
                case "ru.astraforce.STOP_SERVICE":
                    stopService(new Intent((Context)this, BCService.class));
                    break;
            }
            setResult(Activity.RESULT_OK, i);
        } catch (Exception e) {
            i.putExtra("error", e.getMessage());
            setResult(0, i);
        }

        BCService.lastBC = "";
        finish();
    }
}