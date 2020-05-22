package ie.dbs.moviesapi.ui.activity;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ie.dbs.moviesapi.R;
import ie.dbs.moviesapi.database.AppDatabase;
import ie.dbs.moviesapi.receiver.NetworkChangeReceiver;

public class BaseActivity extends AppCompatActivity {

    private static boolean isOnline;

    private static View v;
    private static boolean justCreated = true;

    public void setV() {
        this.v = getView();
    }

    public static Context applicationContext;


    public static NetworkChangeReceiver mNetworkReceiver;

    public static AppDatabase database;

    private static  Snackbar snackbar;

    public static void setIsOnline(boolean isOnline) {
        BaseActivity.isOnline = isOnline;
    }

    public static boolean getIsOnline(){
        isOnline = mNetworkReceiver.isOnline(applicationContext);
        return isOnline;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        setV();

        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();


        applicationContext = getApplicationContext();

        database = AppDatabase.getDatabase(getApplicationContext());


    }

    @Override
    protected void onResume() {
        super.onResume();
        setV();
        dialog(getIsOnline());
    }

    public static void dialog(boolean value){

        setIsOnline(value);

        if(value){
            if (snackbar != null) {
                snackbar.setText("Internet connected");
            Handler handler = new Handler();
            Runnable delayRunnable = new Runnable() {
                @Override
                public void run() {
                    snackbar.dismiss();
                }
            };
            handler.postDelayed(delayRunnable, 3000); }
            
        }else {
            snackbar =  Snackbar.make(v, "Internet connection is lost", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    public View getView() {
        return findViewById(android.R.id.content);
    }

    private void registerNetworkBroadcastForNougat() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
}
