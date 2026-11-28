package com.example.ltud;

import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.ltud.feature.welcome.WelcomeActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WelcomeActivityTest {
    @Test
    public void skipShowsFinalStepWithoutSelectingGuest() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                WelcomeActivity.class);
        intent.putExtra(WelcomeActivity.EXTRA_FORCE_SHOW, true);
        WelcomeActivity activity = (WelcomeActivity) InstrumentationRegistry.getInstrumentation()
                .startActivitySync(intent);
        try {
            InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
                activity.findViewById(R.id.btnSkip).performClick();
                assertEquals(View.INVISIBLE, activity.findViewById(R.id.btnSkip).getVisibility());
                assertEquals(activity.getString(R.string.welcome_continue_guest),
                        ((TextView) activity.findViewById(R.id.btnNext)).getText().toString());
            });
        } finally {
            InstrumentationRegistry.getInstrumentation().runOnMainSync(activity::finish);
        }
    }
}
