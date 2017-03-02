package cab.com.rideversitydriver.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cab.com.rideversitydriver.R;


/**
 * Created by Kalidoss on 01/08/16.
 */
public class EmergencyContacts_Activity extends AppCompatActivity implements View.OnClickListener {

    //MyApplication application= null;
    TextView txtTermofService;
    RelativeLayout imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emerency_contacts);
        imgBack = (RelativeLayout) findViewById(R.id.imageView_backarrow);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_backarrow:
                finish();
                break;
            default:
                break;
        }
    }
}