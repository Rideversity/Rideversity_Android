package cab.com.rideversitydriver.Utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import cab.com.rideversitydriver.R;

/**
 * Created by Kalidoss on 27/09/16.
 */
@SuppressLint("ValidFragment")
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    TextView mTextView;

    public DatePicker(View view) {
        mTextView = (TextView) view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
        dialog.getDatePicker().setMinDate(c.getTimeInMillis() - 1000);

        return dialog;
    }

    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        //mTextView.setText((month + 1)+"/"+day + "/" + year);
        Log.i("Date", day + "/" + (month + 1) + "/" + year);
        mTextView.setText((month + 1) + "/" + day + "/" + year);

    }

}
