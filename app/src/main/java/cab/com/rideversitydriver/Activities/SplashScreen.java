package cab.com.rideversitydriver.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.SharedPref;

/**
 * Created by Kalidoss on 01/08/16.
 */
public class SplashScreen extends Activity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);
        final SharedPref sharedPref = new SharedPref(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPref != null) {
                    if (sharedPref.getString("userId") != null) {
                        if (!sharedPref.getString("userId").isEmpty()) {

                            Intent mainIntent = new Intent(SplashScreen.this, HomeMenu.class);
                            startActivity(mainIntent);
                            finish();
                        } else {

                            Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                    } else {
                        Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}