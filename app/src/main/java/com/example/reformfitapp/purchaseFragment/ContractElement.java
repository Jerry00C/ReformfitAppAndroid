package com.example.reformfitapp.purchaseFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContractElement {

    private boolean test;
    private int location;
    private String clientId;
    private int contractId;
    private String startDate;
    private String firstPaymentOccurs;
    private String promotionCode;
    private String storedLastFour;
    private boolean useDirectDebit;


    public ContractElement(boolean test, int location, String clientId, int contractId, String startDate, String firstPaymentOccurs, String promotionCode, String storedLastFour, boolean useDirectDebit) {
        this.test = test;
        this.location = location;
        this.clientId = clientId;
        this.contractId = contractId;
        this.startDate = startDate;
        this.firstPaymentOccurs = firstPaymentOccurs;
        this.promotionCode = promotionCode;
        this.storedLastFour = storedLastFour;
        this.useDirectDebit = useDirectDebit;
    }

    public ContractElement() {
    }

    public JSONObject toJSONObject(){
        Map<String,Object> requestBody = new HashMap<>();
        requestBody.put("Test",test);
        requestBody.put("LocationId",location);
        requestBody.put("ClientId",clientId);
        requestBody.put("ContractId",contractId);
        requestBody.put("StartDate",startDate);
        requestBody.put("FirstPaymentOccurs",firstPaymentOccurs);
        requestBody.put("PromotionCode",promotionCode);
        requestBody.put("UseDirectDebit",useDirectDebit);
        if (!useDirectDebit){
            Map<String,String> storedCardInfo = new HashMap<>();
            storedCardInfo.put("LastFour",storedLastFour);
            requestBody.put("StoredCardInfo",storedCardInfo);
        }

        return new JSONObject(requestBody);

    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFirstPaymentOccurs() {
        return firstPaymentOccurs;
    }

    public void setFirstPaymentOccurs(String firstPaymentOccurs) {
        this.firstPaymentOccurs = firstPaymentOccurs;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getStoredLastFour() {
        return storedLastFour;
    }

    public void setStoredLastFour(String storedLastFour) {
        this.storedLastFour = storedLastFour;
    }

    public boolean isUseDirectDebit() {
        return useDirectDebit;
    }

    public void setUseDirectDebit(boolean useDirectDebit) {
        this.useDirectDebit = useDirectDebit;
    }
}
