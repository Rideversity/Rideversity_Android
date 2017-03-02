package cab.com.rideversitydriver.Models;

/**
 * Created by CIPL0293 on 10/27/2016.
 */

public class DriverInfoModel {

    /*
     {
  "message": "get driver vehicle details successfully",
  "result": "success",
  "data": {
    "driverId": "166",
    "driverLicProof": "http://colanapps.in/rideversity/admin/uploads/driver/license/1477390502_license_166.png",
    "driverLicNumber": "strw42545456",
    "driverImg": "",
    "driverLicExpirayDate": "22/10/2021",
    "carInsProof": "http://colanapps.in/rideversity/admin/uploads/driver/insurance/1477390502_insurance_166.png",
    "carInsExpirayDate": "22/10/2021",
    "carInsNumber": "32423DSF",
    "vehicleTagProof": "http://colanapps.in/rideversity/admin/uploads/driver/vehicle_tag/1477390502_vehicle_tag_166.png",
    "vehicleTagNumber": "565477",
    "registerProof": "http://colanapps.in/rideversity/admin/uploads/driver/vehicle_register/1477390502_vehicle_register_166.png",
    "vehicleFrontPicture": "http://colanapps.in/rideversity/admin/uploads/driver/vehicle_front/1477390502_vehicle_front_166.png",
    "vehicleSidePicture": "http://colanapps.in/rideversity/admin/uploads/driver/vehicle_side/1477390502_vehicle_side_166.png",
    "vehicleRearPicture": "http://colanapps.in/rideversity/admin/uploads/driver/vehicle_rear/1477390502_vehicle_rear_166.png",
    "personalEmail": "",
    "mailingAddress": "",
    "carYear": "",
    "carModel": "A4",
    "carName": "Audi",
    "seatAvailable": "4"
  }
}
     */

    private String mDriverId;
    private String mDriverLicProof;
    private String mDriverLicNumber;
    private String mDriverImg;
    private String mDriverLicExpirayDate;
    private String mCarInsProof;
    private String mCarInsExpirayDate;
    private String mCarInsNumber;
    private String mVehicleTagProof;
    private String mVehicleTagNumber;
    private String mRegisterProof;
    private String mVehicleFrontPicture;
    private String mVehicleRearPicture;
    private String mVehicleSidePicture;
    private String mPersonalEmail;
    private String mMailingAddress;
    private String mCarYear;
    private String mCarModel;
    private String mCarName;
    private String mSeatAvailable;

    public String getVehicleSidePicture() {
        return mVehicleSidePicture;
    }

    public void setVehicleSidePicture(String vehicleSidePicture) {
        mVehicleSidePicture = vehicleSidePicture;
    }

    public String getDriverId() {
        return mDriverId;
    }

    public void setDriverId(String driverId) {
        mDriverId = driverId;
    }

    public String getDriverLicProof() {
        return mDriverLicProof;
    }

    public void setDriverLicProof(String driverLicProof) {
        mDriverLicProof = driverLicProof;
    }

    public String getDriverLicNumber() {
        return mDriverLicNumber;
    }

    public void setDriverLicNumber(String driverLicNumber) {
        mDriverLicNumber = driverLicNumber;
    }

    public String getDriverImg() {
        return mDriverImg;
    }

    public void setDriverImg(String driverImg) {
        mDriverImg = driverImg;
    }

    public String getDriverLicExpirayDate() {
        return mDriverLicExpirayDate;
    }

    public void setDriverLicExpirayDate(String driverLicExpirayDate) {
        mDriverLicExpirayDate = driverLicExpirayDate;
    }

    public String getCarInsProof() {
        return mCarInsProof;
    }

    public void setCarInsProof(String carInsProof) {
        mCarInsProof = carInsProof;
    }

    public String getCarInsExpirayDate() {
        return mCarInsExpirayDate;
    }

    public void setCarInsExpirayDate(String carInsExpirayDate) {
        mCarInsExpirayDate = carInsExpirayDate;
    }

    public String getCarInsNumber() {
        return mCarInsNumber;
    }

    public void setCarInsNumber(String carInsNumber) {
        mCarInsNumber = carInsNumber;
    }

    public String getVehicleTagProof() {
        return mVehicleTagProof;
    }

    public void setVehicleTagProof(String vehicleTagProof) {
        mVehicleTagProof = vehicleTagProof;
    }

    public String getVehicleTagNumber() {
        return mVehicleTagNumber;
    }

    public void setVehicleTagNumber(String vehicleTagNumber) {
        mVehicleTagNumber = vehicleTagNumber;
    }

    public String getRegisterProof() {
        return mRegisterProof;
    }

    public void setRegisterProof(String registerProof) {
        mRegisterProof = registerProof;
    }

    public String getVehicleFrontPicture() {
        return mVehicleFrontPicture;
    }

    public void setVehicleFrontPicture(String vehicleFrontPicture) {
        mVehicleFrontPicture = vehicleFrontPicture;
    }

    public String getVehicleRearPicture() {
        return mVehicleRearPicture;
    }

    public void setVehicleRearPicture(String vehicleRearPicture) {
        mVehicleRearPicture = vehicleRearPicture;
    }

    public String getPersonalEmail() {
        return mPersonalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        mPersonalEmail = personalEmail;
    }

    public String getMailingAddress() {
        return mMailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        mMailingAddress = mailingAddress;
    }

    public String getCarYear() {
        return mCarYear;
    }

    public void setCarYear(String carYear) {
        mCarYear = carYear;
    }

    public String getCarModel() {
        return mCarModel;
    }

    public void setCarModel(String carModel) {
        mCarModel = carModel;
    }

    public String getCarName() {
        return mCarName;
    }

    public void setCarName(String carName) {
        mCarName = carName;
    }

    public String getSeatAvailable() {
        return mSeatAvailable;
    }

    public void setSeatAvailable(String seatAvailable) {
        mSeatAvailable = seatAvailable;
    }
}
