package cab.com.rideversitydriver.Utils;

import android.app.Application;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Kalidoss on 26/07/16.
 */
public class MyApplication extends Application {

    private Typeface normalFont;
    private Typeface boldFont;
    private static MyApplication singleton;

    // Returns the application instance
    public static MyApplication getInstance() {
        return singleton;
    }

    public final void onCreate() {
        super.onCreate();
        singleton = this;
    }


    //Normal font
    public Typeface getNormalFont() {
        if (normalFont == null) {
            normalFont = Typeface.createFromAsset(getAssets(), "Fonts/TrajanPro-Regular.ttf");
            //normalFont = Typeface.createFromAsset(getAssets(),"Fonts/Times New Roman.ttf");
        }
        return this.normalFont;
    }

    //Bold font
    public Typeface getBoldFont() {
        if (boldFont == null) {
            boldFont = Typeface.createFromAsset(getAssets(), "Fonts/TrajanPro-Regular.ttf");
            //normalFont = Typeface.createFromAsset(getAssets(),"Fonts/Times New Roman.ttf");
        }
        return this.boldFont;
    }

    // -- Fonts For TextView -- //
    public void setTypeface(TextView textView) {
        if (textView != null) {
            if (textView.getTypeface() != null && textView.getTypeface().isBold()) {
                textView.setTypeface(getBoldFont());
            } else {
                textView.setTypeface(getNormalFont());
            }
        }
    }

    // -- Fonts For Button -- //
    public void setTypeface(Button button) {
        if (button != null) {
            if (button.getTypeface() != null && button.getTypeface().isBold()) {
                button.setTypeface(getBoldFont());
            } else {
                button.setTypeface(getNormalFont());
            }
        }
    }
}
