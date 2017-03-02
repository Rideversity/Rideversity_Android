package cab.com.rideversitydriver.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cab.com.rideversitydriver.R;


/**
 * Created by kalidoss on 26/07/16.
 */
public class ActivityIndicator extends Dialog {

    TextView tv;
    LinearLayout lay;
    String loadingText = "Loading...";
    ProgressBar progressBar;

    public ActivityIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.lay = new LinearLayout(context);
        this.lay.setOrientation(LinearLayout.VERTICAL);
        this.progressBar = new ProgressBar(context);
        //this.progressBar.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        //this.progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.MULTIPLY);
        this.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        this.lay.addView(progressBar);
        /*this.lay.addView(new ProgressBar(context), new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));*/
        this.tv = new TextView(context);
        this.setLoadingText(loadingText);
        //this.tv.setTextColor(Color.WHITE);
        this.tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        this.lay.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        this.lay.addView(tv);
        this.addContentView(lay, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        this.setCancelable(false);
    }

    public void setLoadingText(String loadingText) {
        this.tv.setText(loadingText);
    }
}
