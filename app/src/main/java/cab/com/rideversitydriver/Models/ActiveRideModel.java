package cab.com.rideversitydriver.Models;

import java.util.ArrayList;

/**
 * Created by KEERTHINI on 7/28/2016.
 */

public class ActiveRideModel
{

        /*static String[] nameArray = {"Cupcake", "Donut", "Eclair"};

        static Integer[] id = {0, 1, 2};*/

        String message,result;
        public ArrayList<ActiveRideListModel> activerideLists = new ArrayList<ActiveRideListModel>();

        public ArrayList<ActiveRideListModel> getActiverideLists() {
                return activerideLists;
        }

        public void setActiverideLists(ArrayList<ActiveRideListModel> activerideLists) {
                this.activerideLists = activerideLists;
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
