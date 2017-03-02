package cab.com.rideversitydriver.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cab.com.rideversitydriver.Fragments.CreditCard_Fragment;
import cab.com.rideversitydriver.Fragments.Payment_Fragment;
import cab.com.rideversitydriver.R;
import cab.com.rideversitydriver.Utils.Singleton;


/**
 * Created by Kalidoss on 30/08/16.
 */
public class PaymentCardList_Adapter extends RecyclerView.Adapter<PaymentCardList_Adapter.PaymentViewHolder> {

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCardNumber;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            this.textViewCardNumber = (TextView) itemView.findViewById(R.id.textView_cardNo);

        }
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_row, parent, false);

        view.setOnClickListener(CreditCard_Fragment.activeCardClickListener);
        PaymentViewHolder rideViewHolder = new PaymentViewHolder(view);
        return rideViewHolder;
    }

    @Override
    public void onBindViewHolder(final PaymentViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewCardNumber;
        textViewName.setText("Credit Card ****-***-" + getLastFour(Singleton.getInstance().cardslistArray.get(0).getCardLists().get(listPosition).getCardNumber()));
    }

    @Override
    public int getItemCount() {
        return Singleton.getInstance().cardslistArray.get(0).getCardLists().size();
    }

    public String getLastFour(String myString) {
        if (myString.length() > 3)
            return myString.substring(myString.length() - 4);
        else
            return myString;
    }
}