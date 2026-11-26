package com.example.ltud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class WelcomeActivity extends AppCompatActivity {
    public static final String EXTRA_FORCE_SHOW = "com.example.ltud.FORCE_SHOW_WELCOME";
    private static final String PREFS_NAME = "learngraph_entry";
    private static final String KEY_GUEST_SELECTED = "guest_selected";
    private static final String STATE_STEP = "step";
    private static final int STEP_COUNT = 3;

    private LinearLayout rootWelcome;
    private ScrollView welcomeContent;
    private TextView tvTitle;
    private TextView tvDescription;
    private LinearLayout layoutFeatures;
    private LinearLayout layoutDots;
    private MaterialButton btnNext;
    private MaterialButton btnBack;
    private MaterialButton btnSkip;
    private String[] titles;
    private String[] descriptions;
    private String[] featureTitles;
    private String[] featureDescriptions;
    private String[] featureSymbols;
    private int currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (!getIntent().getBooleanExtra(EXTRA_FORCE_SHOW, false)
                && preferences.getBoolean(KEY_GUEST_SELECTED, false)) {
            openGuestHome();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        initViews();
        loadData();
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
        layoutDots = findViewById(R.id.layoutDots);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnSkip);
        ViewCompat.setOnApplyWindowInsetsListener(rootWelcome, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    private void loadData() {
        Resources resources = getResources();
        titles = resources.getStringArray(R.array.welcome_titles);
        descriptions = resources.getStringArray(R.array.welcome_descriptions);
        featureTitles = resources.getStringArray(R.array.welcome_feature_titles);
        featureDescriptions = resources.getStringArray(R.array.welcome_feature_descriptions);
        featureSymbols = resources.getStringArray(R.array.welcome_feature_symbols);
    }

    private void setupListeners() {
        btnNext.setOnClickListener(view -> {
            if (currentStep == STEP_COUNT - 1) {
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit().putBoolean(KEY_GUEST_SELECTED, true).apply();
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
                ? getString(R.string.welcome_start_guest)
                : getString(R.string.welcome_next, currentStep + 1));
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
        layoutDots.removeAllViews();
        int small = getResources().getDimensionPixelSize(R.dimen.welcome_gap_small);
        int wide = getResources().getDimensionPixelSize(R.dimen.welcome_gap_large);
        for (int step = 0; step < STEP_COUNT; step++) {
            final int selectedStep = step;
            boolean selected = step == currentStep;
            FrameLayout target = new FrameLayout(this);
            int touchSize = (int) (48 * getResources().getDisplayMetrics().density);
            target.setLayoutParams(new LinearLayout.LayoutParams(touchSize, touchSize));
            View dot = new View(this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    selected ? wide : small, small);
            params.gravity = android.view.Gravity.CENTER;
            dot.setLayoutParams(params);
            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);
            background.setCornerRadius(small);
            background.setColor(ContextCompat.getColor(this,
                    selected ? R.color.welcome_primary : R.color.welcome_card));
            dot.setBackground(background);
            target.setContentDescription(getString(R.string.welcome_step_description, step + 1));
            target.setOnClickListener(view -> {
                currentStep = selectedStep;
                showStep();
            });
            target.addView(dot);
            layoutDots.addView(target);
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
