package cab.com.rideversitydriver.Utils;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Kalidoss on 03/08/16.
 */
public class Constants {

    public static final String LAST_NAME = "lastName";
    public static final String PERSONAL_EMAIL = "personalEmail";
    public static final String CAR_YEAR = "carYear";
    public static final String CATEGORY = "category";
    public static final String ALL = "All";
    public static final String ETA = "eta";
    public static final String PICKUP_MILES = "pickup_miles";

    public static final String BANK_NAME = "bank_name";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String ACCOUNT_HOLDER = "account_holder";
    public static final String ROUTING_NUMBER = "routing_number";
    public static final String BANK_ID = "bank_id";

    public static boolean RIDE_ACCPET = false;
    public static boolean BECOME_DRIVER = false;

    //Base Url
    public static String BASE_URL = "https://rideversity.co/admin/webservice/driver/";
    public static String BASE_URL_GEN = "https://rideversity.co/admin/webservice/";
    public static String LOGIN_URL = BASE_URL + "login";
    public static final String ACTIVE_RIDER_API = BASE_URL + "realtimebookinglist";
    public static final String ACCEPT_RIDE_REQUEST = BASE_URL + "ridedriverrequst";
    public static final String CARPOOL_URL = BASE_URL + "carpoollist";
    public static final String REWARDS_URL = BASE_URL + "rewardslist";
    public static final String ACTIVE_RIDE_URL = BASE_URL + "activeride";
    public static final String LOGOUT = BASE_URL + "logout";
    public static final String INVITE_RIDER_URL = BASE_URL + "inviterider";
    public static final String CREATE_CARPOOL_URL = BASE_URL + "createcarpool";
    public static final String DRIVER_MODE_URL = BASE_URL + "drivermode";
    public static final String LADIES_MODE_URL = BASE_URL + "ladiesonly";
    public static final String ADVANCED_BOOKINGS_URL = BASE_URL + "advancedbookinglist";
    public static final String PAST_RIDE_URL = BASE_URL + "pastride";
    public static final String NOTIFICATION_LIST_URL = BASE_URL + "notificationlist";
    public static final String NOTIFICATION_ENABLE_URL = BASE_URL + "usernotification";
    public static final String TERMS_URL = BASE_URL_GEN + "generalapi/terms";
    public static final String PRIVACY_POLICY_URL = BASE_URL_GEN + "generalapi/privacy";
    public static final String FAQ_URL = BASE_URL_GEN + "generalapi/faq";
    public static String FORGET_PASSWORD_URL = BASE_URL + "forgotpassword";
    public static String FORGET_USERNAME_URL = BASE_URL + "forgotusername";
    public static final String CHANGE_PASSWORD_URL = BASE_URL + "changepassword";
    public static final String CHANGE_EMAIL_URL = BASE_URL + "changeemail";
    public static final String DRIVER_INFO = BASE_URL + "drivervehicleinfo";
    public static final String EDIT_USER_INFO_URL = BASE_URL + "editdriverprofile";
    public static final String DRIVER_INFO_URL = BASE_URL + "driverinfo";
    public static final String SCHOOLLIST_URL = BASE_URL + "schoollist";
    public static final String FACULTY_POSITION_URL = BASE_URL + "facultypositionlist";
    public static final String BECOME_DRIVER_URL = BASE_URL + "updatedriverdocumentation";
    public static final String REGISTER_URL = BASE_URL + "register";
    public static final String SET_USER_LOCATION = BASE_URL + "setuserlocation";
    public static final String SPONSOR_CATEGORY = BASE_URL + "sponsorcategory";
    public static final String CARD_LIST_URL = BASE_URL + "cardlist";
    public static final String ADD_CARD_URL = BASE_URL + "addcard";
    public static final String UPDATE_CARD_URL = BASE_URL + "updatecard";
    public static final String BANK_INFO = BASE_URL + "bankinfo";
    public static final String UPDATE_BANK_INFO = BASE_URL + "updatebank";
    public static final String COMPLETE_RIDE_URL = BASE_URL + "completeride";
    public static final String ONGOING_RIDE_URL = BASE_URL + "ongoingride";


    //NEW API's'
    public static final String REAL_TIME_RIDE_STATUS = BASE_URL + "realtimeridestatus"; // pending, approved, rejected
    public static final String ADVANCE_BOOKING_RIDE_START = BASE_URL + "advancedbookingridestart"; // START RIDE CLICK
    public static final String RIDE_REQUEST_CANCEL = BASE_URL + "riderequestcancel"; //
    public static final String DRIVER_ARRIVED = BASE_URL + "driverarrived"; // NOTIFY ARRIVAL
    public static final String SET_PICKUP_LIMIT = BASE_URL + "setpickuplimit"; // {"driverId": "11", "pickup_miles": "9"}
    public static final String DRIVER_CANCEL_RIDE = BASE_URL + "drivercancelride"; // {"rideId":"6" , "driverId":"11"}
    public static final String REPORT_REASON_LIST = BASE_URL + "reportreasonlist"; //
    public static final String REPORT_RIDER = BASE_URL + "reportrider"; // {"rideId":"116","reportCat":"qwqerty","rideReport":"test comment"}
    public static final String UPDATE_DEVICE_TOKEN = BASE_URL + "updatedevicetoken"; // {"userId":"162","deviceId":"Apple","deviceType":"","deviceToken":""}


    public static final String API_KEY = "api_key";
    public static final String API_SECRET_KEY = "api_secret_key";
    public static String IS_CASH = "isCash";

    public static String API_KIY = "ride_11mTdRSy4m7y7mg3LJ0bn8y3z3ToHsRx_123";
    public static String API_SECRET_KIY = "ride_LQVk1urVmVz4NT0bRVXl3f9yL6O1q8A0_colan";

    public static final String KEY_ARRIVAL_STATUS = "ArrivalStatus";
    public static final String KEY_APPROVAL_STATUS = "ApprovalStatus";

    public static final String PENDING_STATUS = "pending";
    public static final String APPROVED_STATUS = "approved";
    public static final String REJECTED_STATUS = "rejected";

    public static final String ARRIVAL_STATUS = "arrived";
    public static final String COMPLETE_STATUS = "completed";

    // Empty String
    public static String EMPTY_STRING = "";

    // cancel boolean
    public static String CANCEL_BOOLEAN = "";

    // Request Type
    public static String REQUEST_TYPE_POST = "POST";
    public static String REQUEST_TYPE_GET = "GET";

    // Key
    public static String KEY_CONTENT_TYPE = "Content-Type";
    public static String KEY_ACCEPT = "Accept";
    public static String KEY_AUTHENTICATION = "Authorization";
    public static String KEY_ERROR = "error";


    // Value
    public static String VALUE_CONTENT_TYPE = "application/json";
    public static final String NO_CONNECTION = "Please check your internet connection";
    public static final String GPS_NO_CONNECTION = "Please check your GPS connection";
    public static final String NO_GPS_INTERNET_CONNECTION = "Please check your internet and GPS connection";
    public static final String FACULTY_POSITION = "FACULTY POSITION";
    public static final String TYPE_OF_SERVICE = "TYPE OF SERVICE";
    public static final String REQ_SUBMITTED = "Request Submitted";
    public static final String REQ_SUB_DESCRIPTION = "Thank you! We will approve you once the information has been verified";
    public static String RIDE_STATUS = "ride_status";
    public static String RIDE = "";
    public static boolean RIDE_UPDATES = false;
    public static String FROM = "";
    public static String HISTORY_FROM = "";
    public static String FRAGMENT_LOADED = "";
    public static String INVITE_DRIVER_FROM = "";


    public static String PENDING = "pending";
    public static String APPROVED = "approved";
    public static String REJECTED = "rejected";


    //Login
    public static String MESSAGE = "message";
    public static String RESULT = "result";
    public static String DATA = "data";
    public static String USER_ID = "userId";
    public static String FIRST_NAME = "firstName";
    public static String USERNAME = "userName";
    public static String EMAIL = "email";
    public static String PHONENO = "phoneNo";
    public static String RIDERPHONENO = "riderPhoneNo";
    public static String GENDER = "gender";
    public static String PROFILE_IMAGE = "profileImage";
    public static String USER_ROLE = "userRole";
    public static String ACCOUNT_TYPE = "accountType";
    public static String TYPE_OFSERVICE = "typeOfService";
    public static String DEVICE_ID = "deviceId";
    public static String DEVICE_TYPE = "deviceType";
    public static String DEVICE_TOKEN = "deviceToken";
    public static final String ANDROID_DEVICE = "android";
    public static final String AVG_PICKUP_TIME = "avg_pickup_time";


    public static String PASSWORD = "password";
    public static String CONFIRM_PASSWORD = "confirmPassword";
    public static final String SCHOOLID = "schoolId";
    public static final String SCHOOLNAME = "schoolName";
    public static final String MAILING_ADDRESS = "mailingAddress";
    public static final String DATE_OF_BIRTH = "dob";
    public static final String SOCIAL_SECURITY_NO = "social_security_no";
    public static final String STUDENT_ID = "studentId";
    public static final String DRIVER_IMG = "driverImg";
    public static final String STREET_ADDRESS = "streetAddress";
    public static final String LOCALITY = "locality";
    public static final String REGION = "region";
    public static final String POSTAL_CODE = "postalCode"; //streetAddress":"","locality":"","region":"","postalCode"

    public static final String STUDENTCLASSID = "studentClassId";
    public static final String SCHOOLCLASSID = "schoolClassId";
    public static final String CLASSNAME = "class";
    public static final String SCHOOL_FACULTY_POSITION_ID = "schoolFacultyPositionId";
    public static final String FACULTY_POSITION_ID = "facultyPositionId";
    public static final String POSITION = "position";
    public static final String TYPEOFSERVICE = "typeOfService";
    public static final String ERROR_WARNING = "error_warning";
    public static String SUCCESS = "success";
    public static String ERROR = "error";
    public static final String SERVICE_ID = "serviceId";

    public static String TRIP_ID = "tripId";
    public static String TRIP_NAME = "tripName";

    //Ride
    public static String RIDER_ID = "riderId";
    public static String RIDE_TYPE = "rideType";
    public static String PICKUP_LOCATION = "pickupLocation";
    public static String DESTINATION = "destination";
    public static String DONATION = "donation";
    public static String SEAT_NO = "seatNo";
    public static String TRIP_TYPE = "tripType";
    public static String COMMENTS = "comments";
    public static String LADIES_ONLY = "ladiesOnly";
    public static String DISCOUNT_ID = "discountId";
    public static String RIDER_NAME = "riderName";
    public static String RIDE_DATE = "rideDate";
    public static String RIDE_TIME = "rideTime";
    public static String RIDE_TYPE_ID = "rideTypeId";
    public static String DRIVER_REQ_COUNT = "driverReqCount";
    public static final String RIDER_IMAGE = "riderImage";
    public static final String PICK_UP_LAT = "pickupLat";
    public static final String PICK_UP_LONG = "pickupLong";
    public static final String PICK_UP_TIME = "pickupTime";


    public static final String POSTED_TIME = "postedTime";
    public static final String ESTIMTATED_MI = "estimatedMI";
    public static final String IS_RIDE_START = "isRideStart";
    public static final String IS_RIDER_NOTIFY = "isRiderNotify";


    public static String RIDER_FULL_NAME = "riderName";
    public static String RIDER_FIRST_NAME = "riderfName";
    public static String RIDER_LAST_NAME = "riderlName";
    public static String RIDE_ID = "rideId";
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";
    public static String DRIVER_ID = "driverId";
    public static String CURRENT_LATITUDE = "currentLat";
    public static String CURRENT_LONGITUDE = "currentLong";
    public static String PREVIOUS_LATITUDE = "prevLat";
    public static String PREVIOUS_LONGITUDE = "prevLong";
    public static String DRIVER_LAT = "driverLat";
    public static String DRIVER_LONG = "driverLong";

    public static String RIDER_FIRST_NAME_HOME = "riderfirstName";
    public static String RIDER_LAST_NAME_HOME = "riderlastName";


    public static String PICKUP_TIME = "pickupTime";
    public static String DISTANCE = "distance";
    public static String ADDRESS = "address";
    public static String COVER_PHOTO = "coverPhoto";
    public static String COVER = "cover";
    public static String CAR_IMAGE = "carImage";
    public static String INVITED_RIDER = "invitedRider";
    public static String NAME = "name";
    public static String JOINED_RIDER = "joinedRider";

    public static String FULL_NAME = "fullName";
    public static String DRIVER_STATUS = "driverStatus";
    public static String USER_TYPE = "userType";
    public static String RATING = "rating";
    public static String CAR_NAME = "carName";
    public static String COLLEGE = "college";
    public static String CLASSIFICATION = "classification";


    public static String INVITE_DRIVER = "inviteDriver";
    public static String INVITE_RIDER = "inviteRider";

    public static boolean BACK_PRESS = false;

    //Payment
    public static String CARD_ID = "cardId";
    public static String HOLDER_NAME = "holderName";
    public static String CARD_NUMBER = "cardNumber";
    public static String CARD_CVV = "cardCVV";
    public static String EXPIRATION_DATE = "expirationDate";

    //cancel dialog
    public static final String CANCEL_FEES = "cancelFees";

    public static final String REQUEST_ID = "requestId";
    public static final String REQUEST_STATUS = "requestStatus";
    public static int DRIVER_LIST_POSITION;

    //Rewards
    public static final String SPONSOR_ID = "sponsorId";
    public static final String COUPON_CODE = "couponCode";
    public static final String TITLE = "title";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String DESCRIPTION = "description";
    public static final String DISCOUNT_IMAGE = "discountImage";
    public static final String SPONSOR_NAME = "sponsorName";

    public static final String DRIVER_NAME = "driverName";
    public static final String DRIVER_IMAGE = "driverImage";
    public static final String DRIVER_RATING = "driverRating";
    public static final String DRIVER_FIRSTNAME = "driverfirstName";


    public static final String PICKUP_LAT = "pickupLat";
    public static final String PICKUP_LONG = "pickupLong";
    public static final String DESTINATION_LAT = "destinationLat";
    public static final String DESTINATION_LONG = "destinationLong";


    public static final String REPORT_REASON = "reportReason";
    public static final String REPORT_CAT = "reportCat";
    public static final String REPORT_REPORT = "rideReport";


    //Become a driver
    public static final String DRIVER_LIC_PROOF = "driverLicProof";
    public static final String CAR_INS_PROOF = "carInsProof";
    public static final String DRIVER_LIC_EXPIRAY_DATE = "driverLicExpirayDate";
    public static final String DRIVER_LIC_NUMBER = "driverLicNumber";
    public static final String CAR_INS_EXPIRAY_DATE = "carInsExpirayDate";
    public static final String CAR_INS_NUMBER = "carInsNumber";
    public static final String VEHICAL_TAG_PROOF = "vehicleTagProof";
    public static final String VEHICAL_TAG_NUMBER = "vehicleTagNumber";

    public static final String REGISTER_PROOF = "registerProof";
    public static final String VEHICAL_FRONT_PICTURE = "vehicleFrontPicture";
    public static final String VEHICAL_SIDE_PICTURE = "vehicleSidePicture";
    public static final String VEHICAL_REAR_PICTURE = "vehicleRearPicture";
    public static final String SEAT_AVALIABLE = "seatAvailable";
    public static final String CAR_MODEL = "carModel";


    public static final String CARPOOL_ID = "carpoolId";
    public static final String CLASS_ID = "classId";
    public static final String POSITION_ID = "positionId";
    public static final String ACCOUNT_TYPE_ID = "accountTypeId";
    public static final String CURRENT_PASSWORD = "current_password";
    public static final String NEW_PASSWORD = "new_password";
    public static final String CURRENT_EMAIL = "current_email";
    public static final String NEW_EMAIL = "new_email";
    public static final String NOTIFICATION = "notification";
    public static final String NOTIFICATION_ID = "notificationId";
    public static final String MILEAGE_LIMIT = "mileage_limit";


    public static final String STATUS = "status";
    public static final String TOTAL_DONATION = "totalDonation";

    public static final String ACTIVE_RIDE_MESSAGE = "message";
    public static final String ACTIVE_RIDE_RESULT = "result";
    public static final String ACTIVE_RIDE_DATA = "data";
    public static final String ACTIVE_RIDE_RIDE_ID = "rideId";
    public static final String ACTIVE_RIDE_RIDER_ID = "riderId";
    public static final String ACTIVE_RIDE_DRIVER_ID = "driverId";
    public static final String ACTIVE_RIDE_RIDE_TIME = "rideTime";
    public static final String ACTIVE_RIDE_TYPE = "rideType";
    public static final String ACTIVE_RIDE_TYPE_ID = "rideTypeId";
    public static final String ACTIVE_RIDE_DATE = "rideDate";
    public static final String ACTIVE_RIDE_PICK_UP_LOCATION = "pickupLocation";
    public static final String ACTIVE_RIDE_DESTINATION = "destination";
    public static final String ACTIVE_RIDE_SEAT_NO = "seatNo";
    public static final String ACTIVE_RIDE_TRIP_TYPE = "tripType";
    public static final String ACTIVE_RIDE_DONATION = "donation";
    public static final String ACTIVE_RIDE_LADIES_ONLY = "ladiesOnly";
    public static final String ACTIVE_RIDE_DISCOUNT_ID = "discountId";
    public static final String ACTIVE_RIDE_DRIVER_REQ_COUNT = "driverReqCount";
    public static final String ACTIVE_RIDE_COMMENTS = "comments";
    public static final String ACTIVE_RIDE_DRIVER_REQUEST = "driverRequest";
    public static final String ACTIVE_RIDE_CAR_POOL_ID = "carpoolId";
    public static final String ACTIVE_RIDE_RIDER_IMAGE = "riderImage";
    public static final String ACTIVE_RIDE_POSTED_TIME = "postedTime";
    public static final String ACTIVE_RIDE_ESTIMATE_MI = "estimatedMI";
    public static final String ACTIVE_RIDE_PICKUP_LAT = "pickupLat";
    public static final String ACTIVE_RIDE_PICKUP_LONG = "pickupLong";
    public static final String ACTIVE_RIDE_DESTINATION_LAT = "destinationLat";
    public static final String ACTIVE_RIDE_DESTINATION_LONG = "destinationLong";
    public static final String DRIVER_BANK_ERROR = "Driver must provide bank details before requesting a ride.";


    public static boolean CAMERA_RECALL = false;
    public static boolean PERSONAL_CAMERA_RECALL = false;
    public static boolean STUDENT_CAMERA_RECALL = false;
    public static boolean FACULTY_CAMERA_RECALL = false;
    public static boolean PERSONAL_VEHICLE_CAMERA_RECALL = false;
    public static int CAMERA_ONE = 1;

    public static Bitmap thumbnailDriverLicence;
    public static Bitmap bitmapCarproof;
    public static Bitmap bitmapVehicle;
    public static Bitmap bitmapRegproof;
    public static Bitmap bitmapStudentPicture;
    public static Bitmap bitmapFront;
    public static Bitmap bitmapSide;
    public static Bitmap bitmapRear;
    public static Uri uriDriverLicence;
    public static Uri mUri;

    public static String strDriverLicenseNumber;
    public static String strDriverLicenceExpDate;
    public static String strInsuranceExpdate;
    public static String strinsuranceNumber;
    public static String strTagNumber;

    public static float seekValueFloat = 0f;

    public static Double Current_Lattitude;
    public static Double Current_landitude;
    public static Double Destination_Lattitue;
    public static Double Destination_Landitude;


    public static final String RIDER_STATUS = "riderStatus";
    public static final String DRIVER_MODE = "driverMode";

    public static String GENDERS = "Tap Here";
    public static String SCHOOL_ID;
    public static int REGISTRATION_ONE = 1;
    public static String strFirstname, strLastname, strUsername, strPhonenumber, strSchoolemail, strMailindaddress, strStreetAddress, strLocality, strRegion, strPostalCode, strDateOfBirth, strSocialsecurity, strStudentid, strPassword, strConfirmPassword;


    public static String strDriverLicenseNo;
    public static String strLicenseExpDate;
    public static String strInsuranceExpDate;
    public static String strInsuranceNo;
    public static String strTagno;
    public static int BECOME_CAMERA_ONE = 1;
    public static String SCHOOL_NAME;
    // public static String GENDERS;
    // public static String strFirstname,strLastname,strUsername,strPhonenumber,strSchoolemail,strMailindaddress,strSocialsecurity,strStudentid;
    // public static String SCHOOL_ID;
    //public static int REGISTRATION_ONE=1;

    public static final String SCHOOL_EMAIL = "schoolEmail";
    // public static final String MAILING_ADDRESS = "mailingAddress";
    //public static final String SOCIAL_SECURITY_NO = "social_security_no";
    //public static final String STUDENT_ID = "studentId";
    //public static final String DRIVER_IMG = "driverImg";

    public static boolean REG_LOAD = false;

    public static Bitmap bitmapRegProfile;
    public static Bitmap bitmapPersonalImage;

    public static final String LOCATION = "location";
    public static final String LOCATION_LAT = "location_lat";
    public static final String LOCATION_LONG = "location_long";
    public static String RIDEID;
    public static int RIDE_POSITION;

    public static String zipcode;
    public static String schoolId;
    public static String distance;
    public static String female_mode;


    public static String strCheckFromRealTimeOrAdvBook = "fromRealTimeOrAdvBook";
    public static String yesRealTime = "RealTime";
    public static String yesAdvBooking = "AdvBooking";


}
