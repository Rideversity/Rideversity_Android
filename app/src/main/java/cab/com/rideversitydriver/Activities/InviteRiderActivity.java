package cab.com.rideversitydriver.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import cab.com.rideversitydriver.Adapters.InviteRider_RecyclerAdapter;
import cab.com.rideversitydriver.Models.InviteRiderModel;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.Singleton;
import cab.com.rideversitydriver.Utils.Utilities;

/**
 * Created by KEERTHINI on 8/5/2016.
 */

public class InviteRiderActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout backArrow;
    Context context;
    InviteRider_RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static View.OnClickListener inviteDriverClickListener;
    private boolean selected = true;
    private EditText editTextSearch;
    TextView txtDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_rider);
        context = this;
        //inviteDriverClickListener = new InviteDriverOnClick(context);
        backArrow = (RelativeLayout) findViewById(R.id.textView_backarrow);
        txtDone = (TextView) findViewById(R.id.textView_done);
        txtDone.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        editTextSearch = (EditText) findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.inviteDriver_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (Singleton.getInstance().inviteRiderArray != null) {
            adapter = new InviteRider_RecyclerAdapter(context, Singleton.getInstance().inviteRiderArray);
            String text = editTextSearch.getText().toString().toLowerCase(Locale.getDefault());
            adapter.filter(text);
            recyclerView.setAdapter(adapter);
        }

        editTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSearch.setCursorVisible(true);
                editTextSearch.requestFocus();
                editTextSearch.setActivated(true);
                editTextSearch.setPressed(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                InviteRiderActivity.this.adapter.filters(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_done:

                StringBuilder sbName = new StringBuilder();
                for (InviteRiderModel model : Singleton.getInstance().inviteRiderArray) {
                    if (model.isSelected()) {
                        sbName.append(model.getFirstName());
                        sbName.append(", ");
                    }
                }
                String strInviteDriverNames = sbName.toString().trim();
                if (strInviteDriverNames.length() > 0) {
                    strInviteDriverNames = strInviteDriverNames.substring(0, strInviteDriverNames.length() - 1);
                    CarPoolCreate_Activity.txtInviteRider.setText(strInviteDriverNames);
                }
                finish();

                break;
            case R.id.textView_backarrow:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            Utilities.hideKeyboard(InviteRiderActivity.this);
        }
    }
}