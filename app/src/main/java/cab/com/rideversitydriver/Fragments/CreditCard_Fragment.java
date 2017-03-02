package cab.com.rideversitydriver.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cab.com.rideversitydriver.Activities.AddNewCreditcard_Activity;
import cab.com.rideversitydriver.Adapters.PaymentCardList_Adapter;
import cab.com.rideversitydriver.Interfaces.AsyncTaskInterface;
import cab.com.rideversitydriver.Models.CardModel;
import cab.com.rideversitydriver.Models.PaymentModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.CommonAsynTask;
import cab.com.rideversitydriver.Utils.Constants;
import cab.com.rideversitydriver.Utils.SharedPref;
import cab.com.rideversitydriver.Utils.Singleton;

/**
 * Created by Kalidoss on 04/08/16.
 */
public class CreditCard_Fragment extends Fragment implements View.OnClickListener {

    Button btnAddcreditCard;
    Context context;
    public static boolean isCardAdded = false;
    RelativeLayout layoutFull;
    ImageView imgEmptyCard;
    static Activity activity;
    private static SharedPref sharedPref;
    public static View.OnClickListener activeCardClickListener;
    private static RecyclerView recyclerViewCard;
    private RecyclerView.LayoutManager layoutManager;
    PaymentCardList_Adapter adapter;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.credit_card, container, false);
        context = this.getActivity().getApplicationContext();
        activity = this.getActivity();
        sharedPref = new SharedPref(context);
        layoutFull = (RelativeLayout) rootView.findViewById(R.id.layout_payment_full);
        imgEmptyCard = (ImageView) rootView.findViewById(R.id.imageView_credit_card);
        btnAddcreditCard = (Button) rootView.findViewById(R.id.button_add_credit_card);
        btnAddcreditCard.setOnClickListener(this);

        imgEmptyCard.setVisibility(View.GONE);

        activeCardClickListener = new CardOnClick(getActivity());
        recyclerViewCard = (RecyclerView) rootView.findViewById(R.id.recyclerView_payment_card);
        recyclerViewCard.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCard.setLayoutManager(layoutManager);
        recyclerViewCard.setItemAnimator(new DefaultItemAnimator());

        GetCards();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_credit_card:
                isCardAdded = false;
                Intent intent = new Intent(context, AddNewCreditcard_Activity.class);
                intent.putExtra("TYPE", "Add");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private class CardOnClick implements View.OnClickListener {

        private final Context context;

        private CardOnClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int position = recyclerViewCard.getChildLayoutPosition(v);
            Intent intent = new Intent(getActivity(), AddNewCreditcard_Activity.class);
            intent.putExtra("TYPE", "Update");
            intent.putExtra("CARD_ID", Singleton.getInstance().cardslistArray.get(0).getCardLists().get(position).getCardId());
            intent.putExtra("HOLDER_NAME", Singleton.getInstance().cardslistArray.get(0).getCardLists().get(position).getHolderName());
            intent.putExtra("CARD_NUMBER", Singleton.getInstance().cardslistArray.get(0).getCardLists().get(position).getCardNumber());
            intent.putExtra("CARD_CVV", Singleton.getInstance().cardslistArray.get(0).getCardLists().get(position).getCardCVV());
            intent.putExtra("EXPIRATION_DATE", Singleton.getInstance().cardslistArray.get(0).getCardLists().get(position).getExpirationDate());
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isCardAdded == true) {
            GetCards();
            if (!AddNewCreditcard_Activity.strCardmessage.equals("")) {
                Snackbar snackbar = Snackbar
                        .make(layoutFull, "" + AddNewCreditcard_Activity.strCardmessage, Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                snackbar.show();
            }
        }

        //SoftkeyBoard Handles
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                if (heightDiff > 100) {
                    if (Constants.BACK_PRESS == true) {
                        getActivity().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        InputMethodManager inputManager = (InputMethodManager) rootView
                                .getContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);

                        IBinder binder = rootView.getWindowToken();
                        inputManager.hideSoftInputFromWindow(binder,
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                if (heightDiff < 100) {
                }
            }
        });
    }

    public void GetCards() {
        CommonAsynTask addcardsyncTask = new CommonAsynTask(activity, Constants.CARD_LIST_URL, getDetails(),
                Constants.REQUEST_TYPE_POST, new AsyncTaskInterface() {
            @Override
            public void onTaskCompleted(JSONObject jsonObject) {
                if (jsonObject != null) {
                    try {
                        boolean first = false;
                        Singleton.getInstance().cardslistArray.clear();
                        PaymentModel model = new PaymentModel();
                        model.setMessage(jsonObject.getString(Constants.MESSAGE));
                        model.setResult(jsonObject.getString(Constants.RESULT));
                        if (jsonObject.getString(Constants.RESULT).equals(Constants.SUCCESS)) {
                            JSONArray dataArray = jsonObject.getJSONArray(Constants.DATA);
                            ArrayList<CardModel> cardlistArray = new ArrayList<CardModel>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObj = dataArray.getJSONObject(i);
                                CardModel cardmodel = new CardModel();
                                cardmodel.setCardId(dataObj.getString(Constants.CARD_ID));
                                cardmodel.setHolderName(dataObj.getString(Constants.HOLDER_NAME));
                                cardmodel.setCardNumber(dataObj.getString(Constants.CARD_NUMBER));
                                cardmodel.setCardCVV(dataObj.getString(Constants.CARD_CVV));
                                cardmodel.setExpirationDate(dataObj.getString(Constants.EXPIRATION_DATE));
                                cardlistArray.add(cardmodel);
                            }
                            model.setCardLists(cardlistArray);
                            Singleton.getInstance().cardslistArray.add(model);
                        } else if (jsonObject.getString(Constants.RESULT).equals(Constants.ERROR)) {
                            Snackbar snackbar = Snackbar
                                    .make(layoutFull, "" + jsonObject.getString(Constants.MESSAGE), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_background));
                            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
                            snackbar.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (Singleton.getInstance().cardslistArray.size() > 0) {
                        if (Singleton.getInstance().cardslistArray.get(0).getCardLists().size() > 0) {
                            adapter = new PaymentCardList_Adapter();
                            recyclerViewCard.setAdapter(adapter);
                            imgEmptyCard.setVisibility(View.GONE);
                        } else {
                            imgEmptyCard.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(activity, "Backend server problem", Toast.LENGTH_LONG).show();
                }
            }
        });
        addcardsyncTask.execute();
    }

    private static String getDetails() {
        JSONObject objData = new JSONObject();
        try {
            objData.putOpt(Constants.DRIVER_ID, sharedPref.getString("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objData.toString();
    }
}