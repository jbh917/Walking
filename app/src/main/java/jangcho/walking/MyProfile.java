package jangcho.walking;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 장보현1 on 2017-01-25.
 */

public class MyProfile {

    private static String PREF_NAME = "prefs";

    public MyProfile() {
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static Object getValue(Context context, String key) {
        if(key.equals("SEX")) {
            return getPrefs(context).getInt(key, 0);
        } else if(key.equals("TALL")) {
            return getPrefs(context).getInt(key, 0);
        } else if(key.equals("WEIGHT")) {
            return getPrefs(context).getInt(key, 0);
        } else {
            return getPrefs(context).getString(key, "");
        }

    }

    public static void setSex(Context context, int value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt("SEX", value);
        editor.commit();
    }

    public static void setTall(Context context, int value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt("TALL", value);
        editor.commit();
    }

    public static void setWeight(Context context, int value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt("WEIGHT", value);
        editor.commit();
    }

}
