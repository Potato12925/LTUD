package com.example.ltud.core.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/** Stores entry choices without changing keys used by existing installations. */
public final class AppPreferences {
    private static final String PREFS_NAME = "learngraph_entry";
    private static final String KEY_GUEST_SELECTED = "guest_selected";

    private final SharedPreferences preferences;

    public AppPreferences(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isGuestSelected() {
        return preferences.getBoolean(KEY_GUEST_SELECTED, false);
    }

    public void selectGuest() {
        preferences.edit().putBoolean(KEY_GUEST_SELECTED, true).apply();
    }
}
