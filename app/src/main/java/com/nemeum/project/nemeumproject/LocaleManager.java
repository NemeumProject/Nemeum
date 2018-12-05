package com.nemeum.project.nemeumproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import java.util.Locale;

public class LocaleManager {

    public static void setLocale(Context c) {

        setNewLocale(c, getLanguage(c));

    }

    public static void setNewLocale(Context c, String language) {

        persistLanguage(c, language);
        updateResources(c, language);

    }

    public static String getLanguage(Context c) {

        SharedPreferences SP = c.getSharedPreferences("appLanguage", c.MODE_PRIVATE);
        return SP.getString("appLanguage", "");

    }

    private static void persistLanguage(Context c, String language) {

        SharedPreferences SP = c.getSharedPreferences("appLanguage", c.MODE_PRIVATE);
        SharedPreferences.Editor languageEditor = SP.edit();
        languageEditor.putString("appLanguage", language);
        languageEditor.apply();

    }

    private static void updateResources(Context context, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());

        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        res.updateConfiguration(config, res.getDisplayMetrics());

    }
}