package com.autoai.readnotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

import com.autoai.readnotification.models.Action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressLint("OverrideAbstract")
public class MyNotifiService extends NotificationListenerService {
    private BufferedWriter bw;
    private String nMessage;
    private String data;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String msgString = (String) msg.obj;
            Toast.makeText(getApplicationContext(), msgString, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        data = intent.getStringExtra("data");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        MyNotifiService.this.cancelNotification(sbn.getKey());
        Bundle extrass = NotificationCompat.getExtras(sbn.getNotification());
        //find el number hena tmama
        //if you want to send it to backend
        String title2 = NotificationUtils.getTitle(extrass);
        String msg = NotificationUtils.getMessage(extrass);
        Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(), getPackageName());
        if (action != null) {
            try {
                action.sendReply(getApplicationContext(), "hi ncnsdnfsn");
            } catch (PendingIntent.CanceledException e) {
            }
        } else {
        }
    }
}