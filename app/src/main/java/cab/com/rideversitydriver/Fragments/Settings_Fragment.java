package cab.com.rideversitydriver.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import cab.com.rideversitydriver.Activities.FrequentlyAskedQustions_Activity;
import cab.com.rideversitydriver.Activities.NotificationSetting_Activity;
import cab.com.rideversitydriver.Activities.PrivacyPolicy_Activity;
import cab.com.rideversitydriver.Activities.TermsandCondition_Activity;
import cab.com.rideversitydriver.R;

/**
 * Created by Kalidoss on 03/08/16.
 */
public class Settings_Fragment extends Fragment implements View.OnClickListener {

    RelativeLayout layoutNotification, layoutTermsCondition, layoutPrivacyPolicy, layoutFrequentlyaskedquestions;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);
        context = this.getActivity();
        layoutNotification = (RelativeLayout) rootView.findViewById(R.id.layout_notification_setting);
        layoutTermsCondition = (RelativeLayout) rootView.findViewById(R.id.layout_terms_conditions);
        layoutPrivacyPolicy = (RelativeLayout) rootView.findViewById(R.id.layout_privacy_policy);
        layoutFrequentlyaskedquestions = (RelativeLayout) rootView.findViewById(R.id.layout_frequently_asked_questions);
        layoutNotification.setOnClickListener(this);
        layoutTermsCondition.setOnClickListener(this);
        layoutPrivacyPolicy.setOnClickListener(this);
        layoutFrequentlyaskedquestions.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_notification_setting:
                Intent intent = new Intent(context, NotificationSetting_Activity.class);
                startActivity(intent);
                break;
            case R.id.layout_terms_conditions:
                Intent intentTerms = new Intent(context, TermsandCondition_Activity.class);
                startActivity(intentTerms);
                break;
            case R.id.layout_privacy_policy:
                Intent intentPrivacy = new Intent(context, PrivacyPolicy_Activity.class);
                startActivity(intentPrivacy);
                break;
            case R.id.layout_frequently_asked_questions:
                Intent intentFreq = new Intent(context, FrequentlyAskedQustions_Activity.class);
                startActivity(intentFreq);
                break;
            default:
                break;
        }
    }

}
