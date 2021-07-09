package com.example.reformfitapp;

public class MindbodyClientResponseModel {

    private String birthDate;
    private String country;
    private String creationDate;
    private String clientCreditCard;
    private String firstName;
    private String clientId;
    private String lastName;
    private String uniqueId;
    private String email;
    private String mobilePhone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String postalCode;
    private String photeUrl;
    private String gender;

    //custom client fields
    private String height;
    private String weight;
    private String wristBandNum;
    private String wristBandBrand;


    public MindbodyClientResponseModel() {
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        String[] parts = birthDate.split("T");
        this.birthDate = parts[0];
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }


    public String getClientCreditCard() {
        return clientCreditCard;
    }

    public void setClientCreditCard(String clientCreditCard) {
        this.clientCreditCard = clientCreditCard;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoteUrl() {
        return photeUrl;
    }

    public void setPhoteUrl(String photeUrl) {
        this.photeUrl = photeUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWristBandNum() {
        return wristBandNum;
    }

    public void setWristBandNum(String wristBandNum) {
        this.wristBandNum = wristBandNum;
    }

    public String getWristBandBrand() {
        return wristBandBrand;
    }

    public void setWristBandBrand(String wristBandBrand) {
        this.wristBandBrand = wristBandBrand;
    }
}





