package ru.astraforce.broadcastsvc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GetData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();

        try {
            switch (intent.getAction()) {
                case "ru.astraforce.GET_BARCODE":
                    if (BCService.lastBC != null && BCService.lastBC.length() > 0)
                        intent.putExtra("barcode", BCService.lastBC);
                    break;
                case "ru.astraforce.GET_PARAMS":
                    intent.putExtra("inMessage", BCService.inMessage);
                    intent.putExtra("bcName", BCService.bcName);
                    break;
                case "ru.astraforce.START_SERVICE":
                    startService(new Intent(this, BCService.class));
                    intent.putExtra("version", BCService.VERSION);
                    intent.putExtra("inMessage", BCService.inMessage);
                    intent.putExtra("bcName", BCService.bcName);
                    break;
                case "ru.astraforce.STOP_SERVICE":
                    stopService(new Intent(this, BCService.class));
                    break;
            }
            setResult(Activity.RESULT_OK, intent);
        } catch (Exception e) {
            intent.putExtra("error", e.getMessage());
            setResult(0, intent);
        }

        BCService.lastBC = "";
        finish();
    }
}