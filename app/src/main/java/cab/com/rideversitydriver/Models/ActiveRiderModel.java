package cab.com.rideversitydriver.Models;

/**
 * Created by CIPL0293 on 10/7/2016.
 */

public class ActiveRiderModel {

     /*
      "message": "Get active ride information successfully",
  "result": "success",
   "rideId": "35",
      "riderId": "309",
      "driverId": "0",
      "rideTime": "22:41",
      "rideType": "Real Time",
      "riderImage": "http://colanapps.in/rideversity/admin/uploads/profile/karthik.png",
      "riderName": "Ariel",
      "riderPhoneNo": "123456777",
      "rideTypeId": "1",
      "rideDate": "10/26/2016",
      "pickupLocation": "3-2, Grubanali Street, 600031, Egmore Nungambakkam, Tamil Nadu, India",
      "destination": "Teh. Nurpur, Dist. Kangra, Himachal Pradesh, India, Talara, Himachal Pradesh 176051, India",
      "seatNo": "2",
      "tripType": "One-way",
      "donation": "1990",
      "ladiesOnly": "0",
      "discountId": "0",
      "comments": "",
      "carpoolId": "0",
      "postedTime": "1 Hrs",
      "estimatedMI": "1,353.37"

    */
    private String mMessage;
    private String mResult;
    private String mRideId;
    private String mRiderId;
    private String mDriverId;
    private String mRideTime;
    private String mRideType;
    private String mRideTypeId;
    private String mRideDate;
    private String mPickUpLocation;
    private String mDestination;
    private String mSeatNo;
    private String mTripType;
    private String mDonation;
    private String mLadiesOnly;
    private String mDiscountId;
    private String mDriverReqCount;
    private String mComments;
    private String mDriverRequest;
    private String mCarpoolId;
    private String mRiderImage;
    private String mPostedTime;
    private String mEstimatedMI;
    private String mRiderName;
    private String destinationLat;
    private String destinationLong;
    private String pickupLat;
    private String pickupLong;
    private String riderfirstName ;
    private String riderlastName ;

    public String getRiderlastName() {
        return riderlastName;
    }

    public void setRiderlastName(String riderlastName) {
        this.riderlastName = riderlastName;
    }

    public String getRiderfirstName() {
        return riderfirstName;
    }

    public void setRiderfirstName(String riderfirstName) {
        this.riderfirstName = riderfirstName;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    private String pickupTime ;

    public String getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(String pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getDestinationLong() {
        return destinationLong;
    }

    public void setDestinationLong(String destinationLong) {
        this.destinationLong = destinationLong;
    }

    public String getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        this.destinationLat = destinationLat;
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

    public String getPostedTime() {
        return mPostedTime;
    }

    public void setPostedTime(String postedTime) {
        mPostedTime = postedTime;
    }

    public String getEstimatedMI() {
        return mEstimatedMI;
    }

    public void setEstimatedMI(String estimatedMI) {
        mEstimatedMI = estimatedMI;
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

    public String getPickUpLocation() {
        return mPickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        mPickUpLocation = pickUpLocation;
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

    public String getDriverReqCount() {
        return mDriverReqCount;
    }

    public void setDriverReqCount(String driverReqCount) {
        mDriverReqCount = driverReqCount;
    }

    public String getComments() {
        return mComments;
    }

    public void setComments(String comments) {
        mComments = comments;
    }

    public String getDriverRequest() {
        return mDriverRequest;
    }

    public void setDriverRequest(String driverRequest) {
        mDriverRequest = driverRequest;
    }

    public String getCarpoolId() {
        return mCarpoolId;
    }

    public void setCarpoolId(String carpoolId) {
        mCarpoolId = carpoolId;
    }
}
