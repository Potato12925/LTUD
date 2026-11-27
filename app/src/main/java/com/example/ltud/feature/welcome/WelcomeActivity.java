package com.example.ltud.feature.welcome;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.example.ltud.R;
import com.example.ltud.core.preferences.AppPreferences;
import com.example.ltud.core.ui.SystemBarInsets;
import com.example.ltud.feature.home.MainActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class WelcomeActivity extends AppCompatActivity {
    public static final String EXTRA_FORCE_SHOW = "com.example.ltud.FORCE_SHOW_WELCOME";
    private static final String STATE_STEP = "step";
    private static final int STEP_COUNT = 3;
    private static final int[] DOT_TARGET_IDS = {
            R.id.dotTargetOne, R.id.dotTargetTwo, R.id.dotTargetThree
    };

    private LinearLayout rootWelcome;
    private ScrollView welcomeContent;
    private TextView tvTitle;
    private TextView tvDescription;
    private LinearLayout layoutFeatures;
    private MaterialButton btnNext;
    private MaterialButton btnBack;
    private MaterialButton btnSkip;
    private String[] titles;
    private String[] descriptions;
    private String[] featureTitles;
    private String[] featureDescriptions;
    private String[] featureSymbols;
    private int currentStep;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences = new AppPreferences(getApplicationContext());
        if (!getIntent().getBooleanExtra(EXTRA_FORCE_SHOW, false)
                && appPreferences.isGuestSelected()) {
            openGuestHome();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        initViews();
        loadTextResources();
        setupListeners();
        currentStep = savedInstanceState == null ? 0 : savedInstanceState.getInt(STATE_STEP, 0);
        showStep();
    }

    private void initViews() {
        rootWelcome = findViewById(R.id.rootWelcome);
        welcomeContent = findViewById(R.id.welcomeContent);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        layoutFeatures = findViewById(R.id.layoutFeatures);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnSkip);
        SystemBarInsets.applyToPadding(rootWelcome);
    }

    private void loadTextResources() {
        titles = getResources().getStringArray(R.array.welcome_titles);
        descriptions = getResources().getStringArray(R.array.welcome_descriptions);
        featureTitles = getResources().getStringArray(R.array.welcome_feature_titles);
        featureDescriptions = getResources().getStringArray(R.array.welcome_feature_descriptions);
        featureSymbols = getResources().getStringArray(R.array.welcome_feature_symbols);
    }

    private void setupListeners() {
        btnNext.setOnClickListener(view -> {
            if (currentStep == STEP_COUNT - 1) {
                appPreferences.selectGuest();
                openGuestHome();
            } else {
                currentStep++;
                showStep();
            }
        });
        btnBack.setOnClickListener(view -> {
            if (currentStep > 0) {
                currentStep--;
                showStep();
            }
        });
        btnSkip.setOnClickListener(view -> {
            currentStep = STEP_COUNT - 1;
            showStep();
        });
        findViewById(R.id.btnLogin).setOnClickListener(view ->
                Snackbar.make(rootWelcome, R.string.welcome_login_unavailable, Snackbar.LENGTH_LONG).show());
    }

    private void showStep() {
        tvTitle.setText(titles[currentStep]);
        tvDescription.setText(descriptions[currentStep]);
        btnNext.setText(currentStep == STEP_COUNT - 1
                ? getString(R.string.welcome_continue_guest)
                : getString(R.string.welcome_continue_step, currentStep + 1, STEP_COUNT));
        btnBack.setVisibility(currentStep == 0 ? View.INVISIBLE : View.VISIBLE);
        btnSkip.setVisibility(currentStep == STEP_COUNT - 1 ? View.INVISIBLE : View.VISIBLE);
        renderFeatures();
        renderDots();
        welcomeContent.smoothScrollTo(0, 0);
    }

    private void renderFeatures() {
        layoutFeatures.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        int first = currentStep * STEP_COUNT;
        for (int index = first; index < first + STEP_COUNT; index++) {
            View card = inflater.inflate(R.layout.item_welcome_feature, layoutFeatures, false);
            ((TextView) card.findViewById(R.id.tvFeatureIcon)).setText(featureSymbols[index]);
            ((TextView) card.findViewById(R.id.tvFeatureTitle)).setText(featureTitles[index]);
            ((TextView) card.findViewById(R.id.tvFeatureDescription))
                    .setText(featureDescriptions[index]);
            layoutFeatures.addView(card);
        }
    }

    private void renderDots() {
        for (int step = 0; step < STEP_COUNT; step++) {
            final int selectedStep = step;
            boolean selected = step == currentStep;
            FrameLayout target = findViewById(DOT_TARGET_IDS[step]);
            View dot = target.getChildAt(0);
            ViewCompat.setBackgroundTintList(dot, ColorStateList.valueOf(ContextCompat.getColor(this,
                    selected ? R.color.welcome_primary : R.color.welcome_card)));
            target.setContentDescription(getString(R.string.welcome_step_description,
                    step + 1, STEP_COUNT));
            target.setOnClickListener(view -> {
                currentStep = selectedStep;
                showStep();
            });
        }
    }

    private void openGuestHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_STEP, currentStep);
        super.onSaveInstanceState(outState);
    }
}
