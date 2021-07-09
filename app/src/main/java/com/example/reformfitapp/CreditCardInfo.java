package com.example.reformfitapp;

import java.util.HashMap;

public class CreditCardInfo {
    private float amount ;
    private String cardNumber;
    private int expMonth;
    private int expYear;
    private String cvv;
    private String cardType;
    private String billingName;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String postalCode;
    private boolean saveInfo;

    public CreditCardInfo(float amount, String cardNumber, int expMonth, int expYear, String cvv, String billingName, String billingAddress, String billingCity, String billingState, String postalCode, boolean saveInfo) {
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
        this.billingName = billingName;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingState = billingState;
        this.postalCode = postalCode;
        this.saveInfo = saveInfo;
    }
    public CreditCardInfo(String cardNumber, int expMonth, int expYear, String billingName, String billingAddress, String billingCity, String billingState, String postalCode, boolean saveInfo) {

        this.cardNumber = cardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.billingName = billingName;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingState = billingState;
        this.postalCode = postalCode;
        this.saveInfo = saveInfo;
    }

    public CreditCardInfo(String cardNumber, int expMonth, int expYear, String cardType, String billingName, String billingAddress, String billingCity, String billingState,String postalCode) {
        this.cardNumber = cardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cardType = cardType;
        this.billingName = billingName;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingState = billingState;
        this.postalCode = postalCode;
    }

    public HashMap<String, Object> toHashMap_service(){
        HashMap<String, Object> creditCardMetadata = new HashMap<>();
        creditCardMetadata.put("Amount",amount);
        creditCardMetadata.put("CreditCardNumber",cardNumber);
        creditCardMetadata.put("ExpMonth",expMonth);
        creditCardMetadata.put("ExpYear",expYear);
        creditCardMetadata.put("Cvv",cvv);
        creditCardMetadata.put("BillingName",billingName);
        creditCardMetadata.put("BillingAddress",billingAddress);
        creditCardMetadata.put("BillingCity",billingCity);
        creditCardMetadata.put("BillingState",billingState);
        creditCardMetadata.put("BillingPostalCode",postalCode);
        creditCardMetadata.put("SaveInfo",saveInfo);
        return creditCardMetadata;
    }
    public HashMap<String, Object> toHashMap_contract(){
        HashMap<String, Object> creditCardInfo = new HashMap<>();
        creditCardInfo.put("CreditCardNumber",cardNumber);
        creditCardInfo.put("ExpMonth",String.valueOf(expMonth));
        creditCardInfo.put("ExpYear",String.valueOf(expYear));
        creditCardInfo.put("BillingName",billingName);
        creditCardInfo.put("BillingAddress",billingAddress);
        creditCardInfo.put("BillingCity",billingCity);
        creditCardInfo.put("BillingState",billingState);
        creditCardInfo.put("BillingPostalCode",postalCode);
        creditCardInfo.put("SaveInfo",saveInfo);
        return creditCardInfo;
    }

    public  HashMap<String, Object> toHashMap_update(){

        HashMap<String, Object> creditCardInfo = new HashMap<>();
        creditCardInfo.put("CardNumber",cardNumber);
        creditCardInfo.put("ExpYear",String.valueOf(expMonth));
        creditCardInfo.put("ExpMonth",String.valueOf(expYear));
        creditCardInfo.put("CardHolder",billingName);
        creditCardInfo.put("Address",billingAddress);
        creditCardInfo.put("City",billingCity);
        creditCardInfo.put("State",billingState);
        creditCardInfo.put("PostalCode",postalCode);
        creditCardInfo.put("CardType",cardType);
        creditCardInfo.put("LastFour",slice_end(cardNumber,-4));
        return creditCardInfo;


    }


    public String slice_end(String s, int endIndex) {
        if (endIndex < 0) endIndex = s.length() + endIndex;
        return s.substring(endIndex, s.length());
    }



}

