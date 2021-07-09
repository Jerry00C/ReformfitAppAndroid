package com.example.reformfitapp;

import java.util.HashMap;

public class Payment {
    private String typeName;
    private HashMap<String,Object> metadata;

    public String getTypeName() {
        return typeName;
    }

    public HashMap<String, Object> getMetadata() {
        return metadata;
    }

    private HashMap<String,Object> paymentItem;
    public static String Type = "Type";
    public static String Metadata = "Metadata";

    public Payment() {
        metadata = new HashMap<>();
        paymentItem =new HashMap<>();
    }

    public HashMap<String,Object> setUpCreditCard(float amount, String cardNumber, int expMonth, int expYear, String cvv, String billingName, String billingAddress, String billingCity, String billingState, String postalCode, boolean saveInfo){
        typeName = "CreditCard";
        CreditCardInfo creditCardInfo = new CreditCardInfo(amount,cardNumber,expMonth,expYear,cvv,billingName,billingAddress,billingCity,billingState,postalCode,saveInfo);
        metadata = creditCardInfo.toHashMap_service();
        paymentItem.put(Type,typeName);
        paymentItem.put(Metadata,metadata);
        return paymentItem;
    }

    public HashMap<String,Object> setUpStoredCard(float amount,String lastFour){
        typeName = "StoredCard";
        metadata.put("Amount",amount);
        metadata.put("LastFour",lastFour);
        paymentItem.put(Type,typeName);
        paymentItem.put(Metadata,metadata);


        return paymentItem;

    }

    public HashMap<String,Object> setUpDirectDebit(float amount){
        typeName = "DirectDebit";
        metadata.put("Amount",amount);
        paymentItem.put(Type,typeName);
        paymentItem.put(Metadata,metadata);


        return paymentItem;

    }

    public HashMap<String,Object> setUpGiftCard(float amount,String cardNumber){
        typeName = "GiftCard";
        metadata.put("Amount",amount);
        metadata.put("CardNumber",cardNumber);
        paymentItem.put(Type,typeName);
        paymentItem.put(Metadata,metadata);


        return paymentItem;

    }

    public HashMap<String,Object> setUpComp(float amount){
        typeName = "Comp";
        metadata.put("Amount",amount);
        paymentItem.put(Type,typeName);
        paymentItem.put(Metadata,metadata);


        return paymentItem;

    }
}
