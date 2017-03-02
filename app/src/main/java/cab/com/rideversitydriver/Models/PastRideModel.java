package cab.com.rideversitydriver.Models;

import java.util.ArrayList;

/**
 * Created by CIPL0293 on 10/17/2016.
 */

public class PastRideModel  {

    private String mMessage;
    private String mResult;

    private ArrayList<PastRideListModel> mPastRideListModels;

    public ArrayList<PastRideListModel> getPastRideListModels() {
        return mPastRideListModels;
    }

    public void setPastRideListModels(ArrayList<PastRideListModel> pastRideListModels) {
        mPastRideListModels = pastRideListModels;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }
}
