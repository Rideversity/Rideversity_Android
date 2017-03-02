package cab.com.rideversitydriver.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cab.com.rideversitydriver.Fragments.BankAccount;
import cab.com.rideversitydriver.Fragments.CreditCard_Fragment;

/**
 * Created by Kalidoss on 04/08/16.
 */
public class Payment_PageAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public Payment_PageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CreditCard_Fragment tab1 = new CreditCard_Fragment();

                return tab1;
            case 1:
                BankAccount tab2 = new BankAccount();

                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}