package com.example.contactstatus;

import static android.Manifest.permission.READ_PHONE_STATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private EditText statusMessageEditText;
    private Switch statusSwitch;
    private Button setStatusButton;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state != null && state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String statusMessage = statusMessageEditText.getText().toString();
                if (statusSwitch.isChecked() && !statusMessage.isEmpty()) {
                    Toast.makeText(context, "Incoming call! Status: " + statusMessage, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessageEditText = findViewById(R.id.statusMessageEditText);
        statusSwitch = findViewById(R.id.statusSwitch);
        setStatusButton = findViewById(R.id.setStatusButton);

        setStatusButton.setOnClickListener(view -> {
            String statusMessage = statusMessageEditText.getText().toString();
            if (!statusMessage.isEmpty()) {
                Toast.makeText(MainActivity.this, "Status set: " + statusMessage, Toast.LENGTH_SHORT).show();
            }
        });

        statusSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Toast.makeText(MainActivity.this, "Status message feature activated", Toast.LENGTH_SHORT).show();
                registerReceiver(broadcastReceiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));
            } else {
                Toast.makeText(MainActivity.this, "Status message feature deactivated", Toast.LENGTH_SHORT).show();
                unregisterReceiver(broadcastReceiver);
            }
        });

        ActivityCompat.requestPermissions(
                this,
                new String[]{READ_PHONE_STATE},
                1
        );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}