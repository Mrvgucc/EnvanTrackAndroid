package com.example.designs;

import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Uygulamanın herhangi bir Activity'sinde
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.c1)); // status bar rengini arka plan rengi ile ayni yapma
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.c1)); // navigation bar rengini arka plan rengi ile ayni yapma


    }
}