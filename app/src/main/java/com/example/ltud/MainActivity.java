package com.example.ltud;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int spacing = getResources().getDimensionPixelSize(R.dimen.welcome_screen_margin);
            v.setPadding(systemBars.left + spacing, systemBars.top + spacing,
                    systemBars.right + spacing, systemBars.bottom + spacing);
            return insets;
        });
        findViewById(R.id.btnReplayWelcome).setOnClickListener(view -> {
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.putExtra(WelcomeActivity.EXTRA_FORCE_SHOW, true);
            startActivity(intent);
        });
    }
}
