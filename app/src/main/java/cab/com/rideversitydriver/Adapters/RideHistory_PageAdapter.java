package cab.com.rideversitydriver.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cab.com.rideversitydriver.Fragments.ActiveRide_Fragment;
import cab.com.rideversitydriver.Fragments.PastRide_Fragment;


/**
 * Created by KEERTHINI on 7/28/2016.
 */

public class RideHistory_PageAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public RideHistory_PageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ActiveRide_Fragment tab1 = new ActiveRide_Fragment();
                return tab1;
            case 1:
                PastRide_Fragment tab2 = new PastRide_Fragment();
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
