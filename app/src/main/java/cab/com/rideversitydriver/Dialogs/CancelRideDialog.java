package cab.com.rideversitydriver.Dialogs;

/**
 * Created by KEERTHINI on 8/8/2016.
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import cab.com.rideversitydriver.Interfaces.DialogDismisser;
import cab.com.rideversitydriver.R;


/**
 * Created by KEERTHINI on 7/29/2016.
 */

public class CancelRideDialog extends DialogFragment {

    private static String dialogboxTitle;
    private static String dialogDescription;
    private DialogDismisser dialogDismisser;

    public CancelRideDialog() {
    }

    public void setDialogTitle(String title, DialogDismisser dialogDismisser) {
        dialogboxTitle = title;
        this.dialogDismisser = dialogDismisser;
    }

    public void setDialogDescription(String description) {
        dialogDescription = description;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_cancel_ride, container);
        TextView dialogTitle = (TextView) view.findViewById(R.id.textView_dialogTitle);
        TextView dialogText = (TextView) view.findViewById(R.id.textView_dialogDesc);
        Button yes = (Button) view.findViewById(R.id.yes);
        Button no = (Button) view.findViewById(R.id.no);

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        dialogTitle.setText(dialogboxTitle);
        dialogText.setText(dialogDescription);
        yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                dialogDismisser.dismissDialog(1);

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}