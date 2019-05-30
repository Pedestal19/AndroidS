package hosanna_techvibes.revcollectenumerator.model;

/**
 * Created by EbukaProf on 02/11/2016.
 */
public class Taxpayer {
    String tax_registration_id;
    String surname;
    String first_name;
    String middle_name;
    String phone_number;
    String temp_tax_id;
    String tax_id;
    String registered_on;
    String photo;


    String town, lga, ward, assettype;
    int buildingid;

    String aid;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public int getBuildingid() {
        return buildingid;
    }

    public void setBuildingid(int buildingid) {
        this.buildingid = buildingid;
    }

    public String getTown() {
        return town;
    }

    public String getLga() {
        return lga;
    }

    public String getWard() {
        return ward;
    }

    public String getAssettype() {
        return assettype;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public void setAssettype(String assettype) {
        this.assettype = assettype;
    }

    public String getTax_registration_id() {
        return tax_registration_id;
    }

    public void setTax_registration_id(String tax_registration_id) {
        this.tax_registration_id = tax_registration_id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getTemp_tax_id() {
        return temp_tax_id;
    }

    public void setTemp_tax_id(String temp_tax_id) {
        this.temp_tax_id = temp_tax_id;
    }

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getRegistered_on() {
        return registered_on;
    }

    public void setRegistered_on(String registered_on) {
        this.registered_on = registered_on;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
