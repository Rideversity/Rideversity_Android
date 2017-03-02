package cab.com.rideversitydriver.Models;

import java.util.ArrayList;

/**
 * Created by Kalidoss on 08/09/16.
 */
public class RewardsModel {

    String message,result;
    public ArrayList<RewardsListModel> rewardsLists = new ArrayList<RewardsListModel>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<RewardsListModel> getRewardsLists() {
        return rewardsLists;
    }

    public void setRewardsLists(ArrayList<RewardsListModel> rewardsLists) {
        this.rewardsLists = rewardsLists;
    }
}
