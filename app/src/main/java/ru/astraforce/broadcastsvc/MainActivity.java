package ru.astraforce.broadcastsvc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStart;
    Button btnStop;
    EditText etInMessage;
    EditText etInField;
    EditText etOutMessage;
    EditText etEventId;
    EditText etBaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        etInMessage = findViewById(R.id.teInMessage);
        etInField = findViewById(R.id.teInField);
        etOutMessage = findViewById(R.id.teOutMessage);
        etEventId = findViewById(R.id.teEventId);
        etBaseId = findViewById(R.id.teBaseId);

        this.setControls(false);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = BCService.NOTIF_CHANNEL_Name;
            String description = BCService.NOTIF_CHANNEL_Desc;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(BCService.NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void setPref(SharedPreferences shPref, EditText et, String prefName, String defValue){
        if (shPref.contains(prefName)) {
            et.setText(shPref.getString(prefName, defValue));
        } else {
            et.setText(defValue);
        }
    }

    private void setControls(boolean inWork){
        SharedPreferences sharedPreferences = getSharedPreferences("bcsvc_settings", 0);
        setPref(sharedPreferences, etInMessage, "edInMessage", "scan.rcv.message");
        setPref(sharedPreferences, etInField, "edInField", "barcode");
        setPref(sharedPreferences, etOutMessage, "edOutMessage", "com.google.android.c2dm.intent.RECEIVE");
        setPref(sharedPreferences, etEventId, "edEventId", "1");
        setPref(sharedPreferences, etBaseId, "edBaseId", "");

        btnStart.setEnabled(!inWork);// && BCService.ready);
        btnStop.setEnabled(inWork);// && BCService.ready);
        etInMessage.setEnabled(!inWork);
        etInField.setEnabled(!inWork);
        etOutMessage.setEnabled(!inWork);
        etEventId.setEnabled(!inWork);
        etBaseId.setEnabled(!inWork);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("bcsvc_settings", 0).edit();
        editor.putString("edInMessage", etInMessage.getText().toString());
        editor.apply();
        editor.putString("edInField", etInField.getText().toString());
        editor.apply();
        editor.putString("edOutMessage", etOutMessage.getText().toString());
        editor.apply();
        editor.putString("edEventId", etEventId.getText().toString());
        editor.apply();
        editor.putString("edBaseId", etBaseId.getText().toString());
        editor.apply();
    }

    @Override
    public void onStart() {
        this.setControls(BCService.ready);
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnStart){
            this.savePreferences();
            startService(new Intent((Context)this, BCService.class));
            this.setControls(true);
        }
        else if(v.getId() == R.id.btnStop){
            stopService(new Intent((Context)this, BCService.class));
            this.setControls(false);
        }
    }
}