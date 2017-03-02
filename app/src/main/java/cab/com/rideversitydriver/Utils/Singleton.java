package cab.com.rideversitydriver.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import cab.com.rideversitydriver.Models.ActiveRideModel;
import cab.com.rideversitydriver.Models.ActiveRiderModel;
import cab.com.rideversitydriver.Models.CarpoolModel;
import cab.com.rideversitydriver.Models.Data;
import cab.com.rideversitydriver.Models.DriverInfoModel;
import cab.com.rideversitydriver.Models.ForgetModel;
import cab.com.rideversitydriver.Models.InviteRiderModel;
import cab.com.rideversitydriver.Models.LoginModel;
import cab.com.rideversitydriver.Models.NotificationModel;
import cab.com.rideversitydriver.Models.PastRideModel;
import cab.com.rideversitydriver.Models.PaymentModel;
import cab.com.rideversitydriver.Models.ReportModel;
import cab.com.rideversitydriver.Models.RewardsModel;
import cab.com.rideversitydriver.Models.RideListModel;
import cab.com.rideversitydriver.Models.SchoolListModel;


/**
 * Created by Kalidoss on 26/07/16.
 */
public class Singleton {

    private Singleton() {
    }

    private static Singleton instance = null;

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public ArrayList<RideListModel> RideListsArray = new ArrayList<RideListModel>();
    public ArrayList<LoginModel> loginArrayList = new ArrayList<LoginModel>();
    public ArrayList<ActiveRiderModel> mActiveRiderArray = new ArrayList<>();
    public ArrayList<ActiveRiderModel> mAdvancedRiderArray = new ArrayList<>();
    public ArrayList<RewardsModel> rewardsArray = new ArrayList<RewardsModel>();
    public ArrayList<CarpoolModel> carpoolArray = new ArrayList<CarpoolModel>();
    public ArrayList<ActiveRideModel> activeRideArray = new ArrayList<ActiveRideModel>();
    public ArrayList<PastRideModel> pastRideArray = new ArrayList<>();
    public ArrayList<NotificationModel> notificationArrayList = new ArrayList<NotificationModel>();
    public ArrayList<ForgetModel> forgetPasswordArray = new ArrayList<ForgetModel>();
    public ArrayList<InviteRiderModel> inviteRiderArray = new ArrayList<InviteRiderModel>();
    public ArrayList<ForgetModel> forgetUsernameArray = new ArrayList<ForgetModel>();
    public ArrayList<LoginModel> profileArrayList = new ArrayList<>();
    public static ArrayList<SchoolListModel> schoolArray = new ArrayList<SchoolListModel>();
    public static ArrayList<SchoolListModel> studentArray = new ArrayList<SchoolListModel>();
    public ArrayList<DriverInfoModel> mDriverInfoModels = new ArrayList<>();
    public ArrayList<PaymentModel> cardslistArray = new ArrayList<PaymentModel>();
    public static ArrayList<SchoolListModel> genderArray = new ArrayList<SchoolListModel>();
    public ArrayList<ReportModel> reportReasonArray = new ArrayList<ReportModel>();
    public ArrayList<Data> onGoingArraySingleton = new ArrayList<Data>();

}
