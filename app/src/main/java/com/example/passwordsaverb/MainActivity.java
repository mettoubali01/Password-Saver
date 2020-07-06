package com.example.passwordsaverb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import android.view.View;
import com.example.passwordsaverb.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    public final int DURATION_SPLASH = 1000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.main_activity);

        splashStart();

    }

    public void splashStart(){
        new Handler().postDelayed((Runnable) new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.container).setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.master, new LoginFragment()).commitAllowingStateLoss();
                //Toast.makeText(MainActivity.this, "EEE", Toast.LENGTH_SHORT).show();
            }
        },DURATION_SPLASH);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
