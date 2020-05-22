package ie.dbs.moviesapi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import java.util.List;

import ie.dbs.moviesapi.R;
import ie.dbs.moviesapi.database.AppDatabase;
import ie.dbs.moviesapi.model.User;

public class Splash extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private AppDatabase database;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                database = AppDatabase.getDatabase(getApplicationContext());
                List<User> users = database.userDAO().getAllUsers();
                if (users.size()==0) {
                    Intent mainIntent = new Intent(Splash.this,LoginActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
                else {
                    Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
