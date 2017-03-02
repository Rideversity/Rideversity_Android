package cab.com.rideversitydriver.Models;

import java.util.ArrayList;

/**
 * Created by Kalidoss on 13/10/16.
 */
public class CarpoolListModel {

    String carpoolId,driverId,rideTime,rideDate,pickupLocation,destination,seatNo,seatAvailable,tripType,donation,ladiesOnly,discountId,driverFirstName,driverImage,driverRating,carName,comments,pickupLat,pickupLong,destinationLat,destinationLong;
    public ArrayList<CoverPhotoModel> coverPhotoLists = new ArrayList<CoverPhotoModel>();
    public ArrayList<InvitedRiderModel> invitedriderLists = new ArrayList<InvitedRiderModel>();
    public ArrayList<JoinedRiderModel> joinedriderLists = new ArrayList<JoinedRiderModel>();
    int position;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarpoolId() {
        return carpoolId;
    }

    public void setCarpoolId(String carpoolId) {
        this.carpoolId = carpoolId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList<CoverPhotoModel> getCoverPhotoLists() {
        return coverPhotoLists;
    }

    public void setCoverPhotoLists(ArrayList<CoverPhotoModel> coverPhotoLists) {
        this.coverPhotoLists = coverPhotoLists;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        this.destinationLat = destinationLat;
    }

    public String getDestinationLong() {
        return destinationLong;
    }

    public void setDestinationLong(String destinationLong) {
        this.destinationLong = destinationLong;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getDonation() {
        return donation;
    }

    public void setDonation(String donation) {
        this.donation = donation;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(String driverRating) {
        this.driverRating = driverRating;
    }

    public ArrayList<InvitedRiderModel> getInvitedriderLists() {
        return invitedriderLists;
    }

    public void setInvitedriderLists(ArrayList<InvitedRiderModel> invitedriderLists) {
        this.invitedriderLists = invitedriderLists;
    }

    public ArrayList<JoinedRiderModel> getJoinedriderLists() {
        return joinedriderLists;
    }

    public void setJoinedriderLists(ArrayList<JoinedRiderModel> joinedriderLists) {
        this.joinedriderLists = joinedriderLists;
    }

    public String getLadiesOnly() {
        return ladiesOnly;
    }

    public void setLadiesOnly(String ladiesOnly) {
        this.ladiesOnly = ladiesOnly;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(String pickupLong) {
        this.pickupLong = pickupLong;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public String getRideTime() {
        return rideTime;
    }

    public void setRideTime(String rideTime) {
        this.rideTime = rideTime;
    }

    public String getSeatAvailable() {
        return seatAvailable;
    }

    public void setSeatAvailable(String seatAvailable) {
        this.seatAvailable = seatAvailable;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }
}
