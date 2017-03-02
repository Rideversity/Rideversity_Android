package cab.com.rideversitydriver.Models;

import java.util.ArrayList;

/**
 * Created by Kalidoss on 08/09/16.
 */
public class ActiveRideListModel {

    /*
    *
    * "rideId": "853",
      "riderId": "198",
      "driverId": "163",
      "rideTime": "00:02",
      "rideType": "Real Time",
      "rideTypeId": "1",
      "rideDate": "27/09/2016",
      "pickupLocation": "2-6, Grubanali Street, 600031, Egmore Nungambakkam, Tamil Nadu, India",
      "destination": "Tambaram, Chennai, Tamil Nadu 600045, India",
      "seatNo": "2",
      "tripType": "One-way",
      "donation": "17.5",
      "ladiesOnly": "0",
      "discountId": "0",
      "driverName": "Bruce v",
      "driverImage": "http://colanapps.in/rideversity/admin/uploads/profile/vijay.png",
      "driverRating": "5",
      "carImage": [],
      "carName": "",
      "comments": "",
      "pickupLat": "13.062909",
      "pickupLong": "80.265005",
      "destinationLat": "12.922915",
      "destinationLong": "80.127456",
      "carpoolId": "0"
    * */
    private String mRideId;
    private String mRiderId;
    private String mDriverId;
    private String mRideTime;
    private String mRideType;
    private String mRideTypeId;
    private String mRideDate;
    private String mPickupLocation;
    private String mDestination;
    private String mSeatNo;
    private String mTripType;
    private String mDonation;
    private String mLadiesOnly;
    private String mDiscountId;
    private String mRiderName;
    private String mRiderImage;
    private String mComments;
    private String mPickUpLat;
    private String mPickUpLong;
    private String mDestinationLat;
    private String mDestinationLong;
    private String mCarpoolId;

    private String riderName;
    private String riderfName;
    private String riderlName;


    private ArrayList<ActiveRideCoverModel> activeridecoverphotoLists = new ArrayList<ActiveRideCoverModel>();

    public ArrayList<ActiveRideCoverModel> getActiveridecoverphotoLists() {
        return activeridecoverphotoLists;
    }

    public void setActiveridecoverphotoLists(ArrayList<ActiveRideCoverModel> activeridecoverphotoLists) {
        this.activeridecoverphotoLists = activeridecoverphotoLists;
    }

    public String getPickUpLat() {
        return mPickUpLat;
    }

    public void setPickUpLat(String pickUpLat) {
        mPickUpLat = pickUpLat;
    }

    public String getRideId() {
        return mRideId;
    }

    public void setRideId(String rideId) {
        mRideId = rideId;
    }

    public String getRiderId() {
        return mRiderId;
    }

    public void setRiderId(String riderId) {
        mRiderId = riderId;
    }

    public String getDriverId() {
        return mDriverId;
    }

    public void setDriverId(String driverId) {
        mDriverId = driverId;
    }

    public String getRideTime() {
        return mRideTime;
    }

    public void setRideTime(String rideTime) {
        mRideTime = rideTime;
    }

    public String getRideType() {
        return mRideType;
    }

    public void setRideType(String rideType) {
        mRideType = rideType;
    }

    public String getRideTypeId() {
        return mRideTypeId;
    }

    public void setRideTypeId(String rideTypeId) {
        mRideTypeId = rideTypeId;
    }

    public String getRideDate() {
        return mRideDate;
    }

    public void setRideDate(String rideDate) {
        mRideDate = rideDate;
    }

    public String getPickupLocation() {
        return mPickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        mPickupLocation = pickupLocation;
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public String getSeatNo() {
        return mSeatNo;
    }

    public void setSeatNo(String seatNo) {
        mSeatNo = seatNo;
    }

    public String getTripType() {
        return mTripType;
    }

    public void setTripType(String tripType) {
        mTripType = tripType;
    }

    public String getDonation() {
        return mDonation;
    }

    public void setDonation(String donation) {
        mDonation = donation;
    }

    public String getLadiesOnly() {
        return mLadiesOnly;
    }

    public void setLadiesOnly(String ladiesOnly) {
        mLadiesOnly = ladiesOnly;
    }

    public String getDiscountId() {
        return mDiscountId;
    }

    public void setDiscountId(String discountId) {
        mDiscountId = discountId;
    }

    public String getRiderName() {
        return mRiderName;
    }

    public void setRiderName(String riderName) {
        mRiderName = riderName;
    }

    public String getRiderImage() {
        return mRiderImage;
    }

    public void setRiderImage(String riderImage) {
        mRiderImage = riderImage;
    }

    public String getComments() {
        return mComments;
    }

    public void setComments(String comments) {
        mComments = comments;
    }

    public String getPickUpLong() {
        return mPickUpLong;
    }

    public void setPickUpLong(String pickUpLong) {
        mPickUpLong = pickUpLong;
    }

    public String getDestinationLat() {
        return mDestinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        mDestinationLat = destinationLat;
    }

    public String getDestinationLong() {
        return mDestinationLong;
    }

    public void setDestinationLong(String destinationLong) {
        mDestinationLong = destinationLong;
    }

    public String getCarpoolId() {
        return mCarpoolId;
    }

    public void setCarpoolId(String carpoolId) {
        mCarpoolId = carpoolId;
    }

    public String getriderfName() {
        return riderfName;
    }

    public void setriderfName(String mriderfName) {
        this.riderfName = mriderfName;
    }

    public String getriderlName() {
        return riderlName;
    }

    public void setriderlName(String mriderlName) {
        this.riderlName = mriderlName;
    }

    public String getriderName() {
        return riderName;
    }

    public void setriderName(String mriderName) {
        this.riderName = mriderName;
    }
}
