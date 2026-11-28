package com.example.ltud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.ltud.core.preferences.AppPreferences;
import com.example.ltud.core.ui.SystemBarInsets;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SharedInfrastructureTest {
    @Test
    public void guestChoicePersistsAcrossInstancesWithoutChangingOtherPreferences() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences preferences = context.getSharedPreferences("learngraph_entry", Context.MODE_PRIVATE);
        boolean hadValue = preferences.contains("guest_selected");
        boolean oldValue = preferences.getBoolean("guest_selected", false);
        try {
            preferences.edit().remove("guest_selected").commit();
            assertFalse(new AppPreferences(context).isGuestSelected());
            new AppPreferences(context).selectGuest();
            assertTrue(new AppPreferences(context).isGuestSelected());
        } finally {
            SharedPreferences.Editor restore = preferences.edit();
            if (hadValue) restore.putBoolean("guest_selected", oldValue);
            else restore.remove("guest_selected");
            restore.commit();
        }
    }

    @Test
    public void systemBarPaddingIsBasedOnOriginalPaddingOnRepeatedDispatch() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            View view = new View(context);
            view.setPadding(3, 5, 7, 11);
            SystemBarInsets.applyToPadding(view);
            WindowInsetsCompat insets = new WindowInsetsCompat.Builder()
                    .setInsets(WindowInsetsCompat.Type.systemBars(), Insets.of(2, 4, 6, 8))
                    .build();
            ViewCompat.dispatchApplyWindowInsets(view, insets);
            ViewCompat.dispatchApplyWindowInsets(view, insets);
            assertEquals(5, view.getPaddingLeft());
            assertEquals(9, view.getPaddingTop());
            assertEquals(13, view.getPaddingRight());
            assertEquals(19, view.getPaddingBottom());
        });
    }
}
