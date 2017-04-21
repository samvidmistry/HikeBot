package com.example.samvidmistry.hikebot;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.samvidmistry.hikebot.event.PermissionFlowFinishedEvent;

import org.greenrobot.eventbus.EventBus;

public class SampleActivity extends AppCompatActivity {
    public static final String REQUEST_SYSTEM_DRAW_PERMISSION = "0";
    private static final int PERMISSION_DRAW_OVERLAY = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getBooleanExtra(REQUEST_SYSTEM_DRAW_PERMISSION, false)
                && Build.VERSION.SDK_INT > Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Please grant the permission to HikeBot", Toast.LENGTH_SHORT)
                    .show();
            ActivityCompat.startActivityForResult(this,
                    new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION), PERMISSION_DRAW_OVERLAY,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
            return;
        }

        if (!HikeBotService.isServiceEnabled(this)) {
            new AlertDialog.Builder(this)
                    .setTitle("Accessibility service disabled")
                    .setMessage("Please enable the HikeBot service from accessibility settings.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(android.provider.
                                    Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("HikeBot is running. Happy hiking!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.finishAfterTransition(SampleActivity.this);
                        }
                    }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_DRAW_OVERLAY && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    Settings.canDrawOverlays(this)) {
                EventBus.getDefault().post(new PermissionFlowFinishedEvent());
            } else {
                Toast.makeText(this, "You have to grant the permission in order for @youtube " +
                        "to work", Toast.LENGTH_LONG).show();
            }

            ActivityCompat.finishAfterTransition(this);
        }
    }
}
