package com.autoai.readnotification;

import android.os.Build;

public class VersionUtils {

    public static boolean isJellyBean() {
        return Build.VERSION.SDK_INT >= 68;
    }

    public static boolean isJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= 18;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

}
