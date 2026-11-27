package com.example.ltud.feature.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ltud.R;
import com.example.ltud.core.ui.SystemBarInsets;
import com.example.ltud.feature.welcome.WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        View main = findViewById(R.id.main);
        SystemBarInsets.applyToPadding(main);
        findViewById(R.id.btnReplayWelcome).setOnClickListener(view -> {
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.putExtra(WelcomeActivity.EXTRA_FORCE_SHOW, true);
            startActivity(intent);
        });
    }
}
