package com.autoai.readnotification;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.autoai.readnotification.models.Action;
import com.autoai.readnotification.services.ApiCallPresenter;

import java.util.ArrayList;

@SuppressLint("OverrideAbstract")
public class MyNotifiService extends NotificationListenerService implements MyServiceLisener {
    private String data;
    private ArrayList<String> number_List = new ArrayList<>();

    public static final int WHATSAPP_CODE = 2;
    public static final int OTHER_NOTIFICATIONS_CODE = 4;
    public static final String WHATSAPP_PACK_NAME = "com.whatsapp";
    private ApiCallPresenter apiCallPresenter;
    private boolean isLastNotificatino;


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
        isLastNotificatino = true;
        int notificationCode = matchNotificationCode(sbn);
        if (notificationCode != OTHER_NOTIFICATIONS_CODE) {
            MyNotifiService.this.cancelNotification(sbn.getKey());
            Bundle extrass = NotificationCompat.getExtras(sbn.getNotification());
            String number = NotificationUtils.getTitle(extrass);
            String msg = NotificationUtils.getMessage(extrass);
            // if (!number.equals("WhatsApp")) {
            setList(number);
            // }
            Log.i("hhhh", "number" + number_List.get(number_List.size() - 1));
            Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(), getPackageName());
            if (action != null) {
                try {
                    action.sendReply(getApplicationContext(), number_List.get(number_List.size() - 1));
                } catch (PendingIntent.CanceledException e) {
                }
            }
        }
    }

    private void setList(String number) {
        isLastNotificatino = false;
        number_List.clear();
        number_List.add(number);
    }

    public ArrayList<String> getList() {
        return number_List;

    }

    private int matchNotificationCode(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        if (packageName.equals(WHATSAPP_PACK_NAME)) {
            return (WHATSAPP_CODE);
        } else {
            return (OTHER_NOTIFICATIONS_CODE);
        }

    }
}