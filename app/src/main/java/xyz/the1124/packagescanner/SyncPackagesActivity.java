package xyz.the1124.packagescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.IOException;

public class SyncPackagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_packages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void sync(View view) {
        String ipAddress = findViewById(R.id.ipAddressField).toString();
        Intent syncServiceIntent = new Intent(this, SyncService.class);
        syncServiceIntent.putExtra("ipAddress", ipAddress);
        startService(syncServiceIntent);

    }

}
