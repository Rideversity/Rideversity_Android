package cab.com.rideversitydriver.Models;

import java.util.ArrayList;

/**
 * Created by Kalidoss on 13/10/16.
 */
public class CarpoolModel {

    String message,result;
    public ArrayList<CarpoolListModel> carpoolLists = new ArrayList<CarpoolListModel>();

    public ArrayList<CarpoolListModel> getCarpoolLists() {
        return carpoolLists;
    }

    public void setCarpoolLists(ArrayList<CarpoolListModel> carpoolLists) {
        this.carpoolLists = carpoolLists;
    }

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
}
