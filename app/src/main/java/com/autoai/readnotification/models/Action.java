package com.autoai.readnotification.models;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Base64;
import android.util.Log;

import com.autoai.readnotification.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Action implements Parcelable {

    private final String text;
    private final String packageName;
    private final PendingIntent p;
    private final boolean isQuickReply;
    Map<String, Uri> map = new HashMap<>();
    private final ArrayList<RemoteInputParcel> remoteInputs = new ArrayList<>();

    public Action(Parcel in) {
        text = in.readString();
        packageName = in.readString();
        p = in.readParcelable(PendingIntent.class.getClassLoader());
        isQuickReply = in.readByte() != 0;
        in.readTypedList(remoteInputs, RemoteInputParcel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(packageName);
        dest.writeParcelable(p, flags);
        dest.writeByte((byte) (isQuickReply ? 1 : 0));
        dest.writeTypedList(remoteInputs);
    }

    public Action(NotificationCompat.Action action, String packageName, boolean isQuickReply) {
        this.text = action.title.toString();
        this.packageName = packageName;
        this.p = action.actionIntent;
        if (action.getRemoteInputs() != null) {
            int size = action.getRemoteInputs().length;
            for (int i = 0; i < size; i++)
                remoteInputs.add(new RemoteInputParcel(action.getRemoteInputs()[i]));
        }
        this.isQuickReply = isQuickReply;
    }

    public void sendReply(Context context, String msg) throws PendingIntent.CanceledException {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        ArrayList<RemoteInput> actualInputs = new ArrayList<>();
        for (RemoteInputParcel input : remoteInputs) {

            Uri uri = Uri.parse("android.resource://com.autoai.readnotification.models/drawable/dummy_icon");

            //Bitmap image4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.dummy_icon);
            // bundle.putParcelable(input.getResultKey(), image4);
            //intent.putExtra(input.getResultKey(), "massage");
            bundle.putCharSequence(input.getResultKey(), "https://helpx.adobe.com/content/dam/help/en/stock/how-to/visual-reverse-image-search/jcr_content/main-pars/image/visual-reverse-image-search-v2_intro.jpg");
            //bundle.putBundle(input.getResultKey(), bundle);
            map.put(input.getResultKey(), uri);
            RemoteInput.Builder builder = new RemoteInput.Builder(input.getResultKey());
            builder.setLabel(input.getLabel());
            builder.setChoices(input.getChoices());
            builder.setAllowFreeFormInput(input.isAllowFreeFormInput());
            builder.addExtras(input.getExtras());
            actualInputs.add(builder.build());
        }
        RemoteInput[] inputs = actualInputs.toArray(new RemoteInput[actualInputs.size()]);
        RemoteInput.addResultsToIntent(inputs, intent, bundle);
        p.send(context, 0, intent);
    }

    public ArrayList<RemoteInputParcel> getRemoteInputs() {
        return remoteInputs;
    }

    public boolean isQuickReply() {
        return isQuickReply;
    }

    public String getText() {
        return text;
    }

    public PendingIntent getQuickReplyIntent() {
        return isQuickReply ? p : null;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator() {
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        public Action[] newArray(int size) {
            return new Action[size];
        }
    };

}
