package cab.com.rideversitydriver.Dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import cab.com.rideversitydriver.Interfaces.DialogDismisser;
import cab.com.rideversitydriver.R;

/**
 * Created by KEERTHINI on 8/4/2016.
 */

public class CustomDialog extends DialogFragment {

    private static String dialogboxTitle;
    private static String dialogDescription, dialogDescriptionTwo;
    private DialogDismisser dialogDismisser;

    public CustomDialog() {
    }

    public void setDialogTitle(String title, DialogDismisser dialogDismisser) {
        dialogboxTitle = title;
        this.dialogDismisser = dialogDismisser;
    }

    public void setDialogDescription(String description) {
        dialogDescription = description;
    }

    public void setDialogDescriptionTwo(String description) {
        dialogDescriptionTwo = description;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_reg_success, container);
        TextView dialogTitle = (TextView) view.findViewById(R.id.textView_dialogTitle);
        dialogTitle.setText(dialogboxTitle);
        TextView dialogText = (TextView) view.findViewById(R.id.textView_dialogDesc);
        dialogText.setText(dialogDescription);
        TextView dialogText2 = (TextView) view.findViewById(R.id.textView_dialogDesc2);
        dialogText2.setText(dialogDescriptionTwo);
        ImageView checkImg = (ImageView) view.findViewById(R.id.imageView_check);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        checkImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                dialogDismisser.dismissDialog(1);

            }
        });

        return view;
    }
}
