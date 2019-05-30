package hosanna_techvibes.revcollectenumerator.model;

import android.app.Activity;
import android.app.Application;

/**
 * Created by EbukaProf on 23/09/2016.
 */
public class App extends Application {
    String command;
    boolean reg_mode;
    String landrin;

    String aid;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getLandrin() {
        return landrin;
    }

    public void setLandrin(String landrin) {
        this.landrin = landrin;
    }

    public boolean getReg_mode() {
        return reg_mode;
    }

    public void setReg_mode(boolean reg_mode) {
        this.reg_mode = reg_mode;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    String reg_building_registered_on;
    String reg_building_building_id;
    String Latitude,Longitude;
    String userid;
    String myEmail;
    String myTIN;
    String myFirstName;
    String mySurname;
    String myWalletBalance;
    String applicationkey;
    String authorization_key;
    String access_code;
    String serviceID;
    String activePhoneCode;
    String TPFNP, TPLNP;
    String activeTIN,activeTempTIN,activeTax_registration_id; long tnxId;
    String CurrentFingerCapture;
    byte[] F_rightThumb;
    byte[] F_leftThumb;
    String transactionId, transactionYear, transactionDay, transactionMonth;
    long TIN;
    String ut;
    String a_buildingname;
    String a_buildingid;
    String bid;

    public String getA_buildingid() {
        return a_buildingid;
    }

    public void setA_buildingid(String a_buildingid) {
        this.a_buildingid = a_buildingid;
    }

    public String getA_buildingname() {
        return a_buildingname;
    }

    public void setA_buildingname(String a_buildingname) {
        this.a_buildingname = a_buildingname;
    }

    //////////////////////////
    byte[] CameraByte;
    Activity activeActivity;
    String SqlQuery;

    //////
    String currentPay_RevenueDimension;
    String currentPay_RevenueType;
    String currentPay_PaymentMethod;
    String currentPay_CustomerTaxID;
    double currentPay_Amount;
    double currentPay_AmountPayable;
    double currentPay_WalletBalance;
    String currentPay_TransactionID;
    String currentPay_CustomerSurname;
    String currentPay_CustomerFirstname;
    String currentPay_CustomerMiddlename;
    String currentPay_CustomerTaxRegistrationID;
    String currentPay_CustomerPhone;
    String currentPay_CustomerEmail;
    int x;
String QRTIN, QRName, in;

String p_town, p_lga, p_ward, p_assettype;

    public void setP_town(String p_town) {
        this.p_town = p_town;
    }

    public void setP_lga(String p_lga) {
        this.p_lga = p_lga;
    }

    public void setP_ward(String p_ward) {
        this.p_ward = p_ward;
    }

    public void setP_assettype(String p_assettype) {
        this.p_assettype = p_assettype;
    }

    public String getP_town() {
        return p_town;
    }

    public String getP_lga() {
        return p_lga;
    }

    public String getP_ward() {
        return p_ward;
    }

    public String getP_assettype() {
        return p_assettype;
    }

    public String getIN() {
        return in;
    }

    public void setIN(String in) {
        this.in = in;
    }

    public String getQRTIN() {
        return QRTIN;
    }

    public void setQRTIN(String QRTIN) {
        this.QRTIN = QRTIN;
    }

    public String getQRName() {
        return QRName;
    }

    public void setQRName(String QRName) {
        this.QRName = QRName;
    }


    /////
    public int getx(){
        return x;
    }
    public void setx(int x){
         this.x =x;
    }

    public byte[] getCameraByte() {
        return CameraByte;
    }

    public void setCameraByte(byte[] cameraByte) {
        CameraByte = cameraByte;
    }

    public byte[] getF_rightThumb() {
        return F_rightThumb;
    }

    public void setF_rightThumb(byte[] f_rightThumb) {
        F_rightThumb = f_rightThumb;
    }

    public byte[] getF_leftThumb() {
        return F_leftThumb;
    }

    public void setF_leftThumb(byte[] f_leftThumb) {
        F_leftThumb = f_leftThumb;
    }

    public Activity getActiveActivity() {
        return activeActivity;
    }

    public void setActiveActivity(Activity activeActivity) {
        this.activeActivity = activeActivity;
    }

    public String getSqlQuery() {
        return SqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        SqlQuery = sqlQuery;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceCode() {
        return serviceID;
    }

    public void setServiceCode(String serviceID) {
        this.serviceID = serviceID;
    }


    public String getCurrentPay_RevenueDimension() {
        return currentPay_RevenueDimension;
    }

    public void setCurrentPay_RevenueDimension(String currentPay_RevenueDimension) {
        this.currentPay_RevenueDimension = currentPay_RevenueDimension;
    }

    public String getCurrentPay_RevenueType() {
        return currentPay_RevenueType;
    }

    public void setCurrentPay_RevenueType(String currentPay_RevenueType) {
        this.currentPay_RevenueType = currentPay_RevenueType;
    }

    public String getCurrentPay_PaymentMethod() {
        return currentPay_PaymentMethod;
    }

    public void setCurrentPay_PaymentMethod(String currentPay_PaymentMethod) {
        this.currentPay_PaymentMethod = currentPay_PaymentMethod;
    }

    public String getCurrentPay_CustomerTaxID() {
        return currentPay_CustomerTaxID;
    }

    public void setCurrentPay_CustomerTaxID(String currentPay_CustomerTaxID) {
        this.currentPay_CustomerTaxID = currentPay_CustomerTaxID;
    }

    public double getCurrentPay_Amount() {
        return currentPay_Amount;
    }

    public void setCurrentPay_Amount(double currentPay_Amount) {
        this.currentPay_Amount = currentPay_Amount;
    }

    public double getCurrentPay_AmountPayable() {
        return currentPay_AmountPayable;
    }

    public void setCurrentPay_AmountPayable(double currentPay_AmountPayable) {
        this.currentPay_AmountPayable = currentPay_AmountPayable;
    }

    public double getCurrentPay_WalletBalance() {
        return currentPay_WalletBalance;
    }

    public void setCurrentPay_WalletBalance(double currentPay_WalletBalance) {
        this.currentPay_WalletBalance = currentPay_WalletBalance;
    }

    public String getCurrentPay_TransactionID() {
        return currentPay_TransactionID;
    }

    public void setCurrentPay_TransactionID(String currentPay_TransactionID) {
        this.currentPay_TransactionID = currentPay_TransactionID;
    }

    public String getCurrentPay_CustomerSurname() {
        return currentPay_CustomerSurname;
    }

    public void setCurrentPay_CustomerSurname(String currentPay_CustomerSurname) {
        this.currentPay_CustomerSurname = currentPay_CustomerSurname;
    }

    public String getCurrentPay_CustomerFirstname() {
        return currentPay_CustomerFirstname;
    }

    public void setCurrentPay_CustomerFirstname(String currentPay_CustomerFirstname) {
        this.currentPay_CustomerFirstname = currentPay_CustomerFirstname;
    }

    public String getCurrentPay_CustomerMiddlename() {
        return currentPay_CustomerMiddlename;
    }

    public void setCurrentPay_CustomerMiddlename(String currentPay_CustomerMiddlename) {
        this.currentPay_CustomerMiddlename = currentPay_CustomerMiddlename;
    }

    public String getCurrentPay_CustomerTaxRegistrationID() {
        return currentPay_CustomerTaxRegistrationID;
    }

    public void setCurrentPay_CustomerTaxRegistrationID(String currentPay_CustomerTaxRegistrationID) {
        this.currentPay_CustomerTaxRegistrationID = currentPay_CustomerTaxRegistrationID;
    }

    public String getCurrentPay_CustomerPhone() {
        return currentPay_CustomerPhone;
    }

    public void setCurrentPay_CustomerPhone(String currentPay_CustomerPhone) {
        this.currentPay_CustomerPhone = currentPay_CustomerPhone;
    }

    public String getCurrentPay_CustomerEmail() {
        return currentPay_CustomerEmail;
    }

    public void setCurrentPay_CustomerEmail(String currentPay_CustomerEmail) {
        this.currentPay_CustomerEmail = currentPay_CustomerEmail;
    }

    ////////////////////////////


    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    public String getMyTIN() {
        return myTIN;
    }

    public void setMyTIN(String myTIN) {
        this.myTIN = myTIN;
    }

    public String getMyWalletBalance() {
        return myWalletBalance;
    }

    public void setMyWalletBalance(String myWalletBalance) {
        this.myWalletBalance = myWalletBalance;
    }

    public String getMyFirstName() {
        return myFirstName;
    }

    public void setMyFirstName(String myFirstName) {
        this.myFirstName = myFirstName;
    }

    public String getMySurname() {
        return mySurname;
    }

    public void setMySurname(String mySurname) {
        this.mySurname = mySurname;
    }

    public String getApplicationkey() {
        return applicationkey;
    }

    public void setApplicationkey(String applicationkey) {
        this.applicationkey = applicationkey;
    }

    public String getAuthorization_key() {
        return authorization_key;
    }

    public void setAuthorization_key(String authorization_key) {
        this.authorization_key = authorization_key;
    }

    public String getAccess_code() {
        return access_code;
    }

    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }

    public String getActiveTIN() {
        return activeTIN;
    }

    public void setActiveTIN(String activeTIN) {
        this.activeTIN = activeTIN;
    }

    public String getActiveTempTIN() {
        return activeTempTIN;
    }

    public void setActiveTempTIN(String activeTempTIN) {
        this.activeTempTIN = activeTempTIN;
    }

    public String getActiveTax_registration_id() {
        return activeTax_registration_id;
    }

    public void setActiveTax_registration_id(String activeTax_registration_id) {
        this.activeTax_registration_id = activeTax_registration_id;
    }

    public long getTnxId() {
        return tnxId;
    }

    public void setTnxId(long tnxId) {
        this.tnxId = tnxId;
    }

    public String getTPFNP(){
        return TPFNP;
    }

    public void setTPFNP(String TPFNP){
        this.TPFNP = TPFNP;
    }

    public String getTPLNP(){
        return TPLNP;
    }

    public void setTPLNP(String TPLNP){
        this.TPLNP = TPLNP;
    }


    public String getTransactionId(){
        return transactionId;
    }

    public void setTransactionId(String transactionId){
        this.transactionId = transactionId;
    }

    public String getTransactionYear(){
        return transactionYear;
    }

    public void setTransactionYear(String transactionYear){
        this.transactionYear = transactionYear;
    }

    public String getTransactionDay(){
        return transactionDay;
    }

    public void setTransactionDay(String transactionDay){
        this.transactionDay = transactionDay;
    }

    public String getTransactionMonth(){
        return transactionMonth;
    }

    public void setTransactionMonth(String transactionMonth){
        this.transactionMonth = transactionMonth;
    }
    public long getTIN() {
        return TIN;
    }

    public void setTIN(long TIN) {
        this.TIN = TIN;
    }

    public String getActivePhoneCode() {
        return activePhoneCode;
    }

    public void setActivePhoneCode(String activePhoneCode) {
        this.activePhoneCode = activePhoneCode;
    }

    public String getCurrentFingerCapture() {
        return CurrentFingerCapture;
    }

    public void setCurrentFingerCapture(String currentFingerCapture) {
        CurrentFingerCapture = currentFingerCapture;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getreg_building_building_id() {
        return reg_building_building_id;
    }

    public void setreg_building_building_id(String reg_building_building_id) {
        this.reg_building_building_id = reg_building_building_id;
    }

    public String getreg_building_registered_on() {
        return reg_building_registered_on;
    }

    public void setreg_building_registered_on(String reg_building_registered_on) {
        this.reg_building_registered_on = reg_building_registered_on;
    }

    public String getBID() {
        return bid;
    }

    public void setBID(String bid) {
        this.bid = bid;
    }

}
