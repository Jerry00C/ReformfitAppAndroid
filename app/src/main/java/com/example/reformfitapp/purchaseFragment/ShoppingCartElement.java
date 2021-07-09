package com.example.reformfitapp.purchaseFragment;
import com.example.reformfitapp.Payment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartElement {
    private String clientId;
    private boolean test;
    //private String type;
    private int serviceId;
    private float discountAmount;
    private int classId;
    private int quantity;
    private boolean inStore;// default false
    private String promotionCode;

    private String paymentType; // Credit card, stored credit card, direct debit, debit account, custom
    private HashMap<String ,Object> paymentMetadata;

    private ArrayList<HashMap<String,Object>> payments;
    private boolean sendEmail;
    private boolean consumerPresent;
    private String paymentAuthenticationCallBackUrl;
    private ArrayList<Integer> transactionIds;
//    private boolean storedCardAdded;
//    private boolean diredebitAdded;

    /// modified version newly added fields

    public ShoppingCartElement(String clientId, boolean test, int serviceId, float discountAmount, int classId, int quantity, boolean inStore, String promotionCode, String paymentType, HashMap<String,Object> paymentMetadata, boolean sendEmail) {
        this.clientId = clientId;
        this.test = test;
        this.serviceId = serviceId;
        this.discountAmount = discountAmount;
        this.classId = classId;
        this.quantity = quantity;
        this.inStore = inStore;
        this.promotionCode = promotionCode;
        this.paymentType = paymentType;
        this.paymentMetadata = paymentMetadata;
        this.sendEmail = sendEmail;
        this.payments = new ArrayList<>();
        addThePaymentIntoPayments();

    }

    public ShoppingCartElement(String clientId, boolean test, int serviceId, float discountAmount, int quantity, boolean inStore, String promotionCode, String paymentType, HashMap<String, Object> paymentMetadata, ArrayList<HashMap<String, Object>> payments, boolean sendEmail) {
        this.clientId = clientId;
        this.test = test;
        this.serviceId = serviceId;
        this.discountAmount = discountAmount;
        this.quantity = quantity;
        this.inStore = inStore;
        this.promotionCode = promotionCode;
        this.paymentType = paymentType;
        this.paymentMetadata = paymentMetadata;
        this.payments = payments;
        this.sendEmail = sendEmail;
        addThePaymentIntoPayments();
    }

    public ShoppingCartElement() {

    }
    public void setGiftCardAmount(float giftCardAmount){
        if(paymentType=="GiftCard"){
            paymentMetadata.put("Amount",giftCardAmount);
        }
    }

    private void addThePaymentIntoPayments(){

        HashMap<String ,Object> paymentInfo = new HashMap<>();
        paymentInfo.put("Type",this.paymentType);
        paymentInfo.put("Metadata",this.paymentMetadata);
        this.payments.add(paymentInfo);

    }
    public void resetPaymentMethods(){
        this.payments = new ArrayList<>();
        HashMap<String ,Object> paymentInfo = new HashMap<>();
        paymentInfo.put("Type",this.paymentType);
        paymentInfo.put("Metadata",this.paymentMetadata);
        this.payments.add(paymentInfo);
    }


    public void addStoredCard(String lastFour, float amount){

        Payment payment = new Payment();

        HashMap<String,Object> newStoredCard = payment.setUpStoredCard(amount,lastFour);
        payments.add(newStoredCard);



    }

    public void addDirectDebit(float amount){

        Payment payment = new Payment();

        HashMap<String,Object> newDirectDebit = payment.setUpDirectDebit(amount);
        payments.add(newDirectDebit);



    }



    public JSONObject toJsonObject(){
        Map<String,Object> requestBody = new HashMap<>();
        // put in the client id and test boolean
        requestBody.put("ClientId",clientId);
        requestBody.put("Test",test);

        //construct the item array
        ArrayList<Map<String,Object>> items = new ArrayList<>(1);

        Map<String , Object> newItem  = new HashMap<>();

        Map<String, Object> key_Item =  new HashMap<>();
        key_Item.put("Type", "Service");
        Map<String, Integer> metadata = new HashMap<>();
        metadata.put("Id", serviceId);
        key_Item.put("Metadata", metadata);

        newItem.put("Item", key_Item);

        newItem.put("DiscountAmount",discountAmount);

        Integer[] classids = {classId};
        // for nore ignore class id
        // newItem.put("ClassIds",classids);

        newItem.put("Quantity",quantity);

        items.add(newItem);

        //put the items array into the request body
        requestBody.put("Items", items);

        // put inStore key value pair
        requestBody.put("InStore",inStore);

        // put payment method key value pair
        // list of payments


        requestBody.put("Payments",payments);

        requestBody.put("PromotionCode",promotionCode);
        // put in send email or not
        requestBody.put("SendEmail",sendEmail);

        JSONObject requestBodyInJson = new JSONObject(requestBody);

        return requestBodyInJson;



    }
    public static ShoppingCartElement generateDefaultCartElement(int serviceId,String clientId){
        ShoppingCartElement newShoppingCartElement = new ShoppingCartElement();

        boolean test  = true;
        float discountAmount = 0;
        int quantity = 1;
        boolean inStore = false;
        String promoCode = "";
        String paymentType = "";
        HashMap<String,Object> metadata = new HashMap<>();
        newShoppingCartElement.setClientId(clientId);
        newShoppingCartElement.setServiceId(serviceId);
        newShoppingCartElement.setTest(test);
        newShoppingCartElement.setQuantity(quantity);
        newShoppingCartElement.setInStore(inStore);
        newShoppingCartElement.setPromotionCode(promoCode);
        newShoppingCartElement.setPaymentType(paymentType);
        newShoppingCartElement.setPaymentMetadata(metadata);
        newShoppingCartElement.setPayments(new ArrayList<>());
        newShoppingCartElement.addThePaymentIntoPayments();


        return newShoppingCartElement;



    }
    public static ShoppingCartElement generateTestShoppingCartElement(String enteredCode, int serviceId, String clientId) {

        boolean test = true;
        float discountAmount = 0;
        int classId = 19365;// takes no effect for now
        int quantity = 1;
        boolean inStore = true;// default false
        String promotionCode = enteredCode;
//        String paymentType = "CreditCard"; // Credit card, stored credit card, direct debit, debit account, custom
//        CreditCardInfo creditCardInfo = new CreditCardInfo((float)1, "4111111111111111",1,2026,"545","John Smith","4565 ABC CT","San Luis Obispo","CA","93401",false);
//        HashMap<String ,Object> paymentMetadata =  creditCardInfo.toHashMap_service();
        Payment payment = new Payment();
        float amount = 0;
        payment.setUpComp(amount);
        String paymentType = payment.getTypeName(); // Credit card, stored credit card, direct debit, debit account, custom
        HashMap<String,Object> paymentMetadata = payment.getMetadata();
        boolean sendEmail = true;
        return new ShoppingCartElement(clientId,test,serviceId,discountAmount,classId,quantity,inStore,promotionCode,paymentType,paymentMetadata,sendEmail);
    }

    public static ShoppingCartElement generateTestShoppingCartElement(String promoCode,String giftCardCode,double giftCardAmount, int serviceId, String clientId) {

        boolean test = true;
        float discountAmount = 0;
        int quantity = 1;
        boolean inStore = true;// default false
        String promotionCode = promoCode;

        Payment firstPaymentMethod = new Payment();
        firstPaymentMethod.setUpGiftCard((float)giftCardAmount,giftCardCode);
        String paymentType =firstPaymentMethod.getTypeName();
        HashMap<String, Object> paymentMetadata = firstPaymentMethod.getMetadata();


        ArrayList<HashMap<String,Object>> payments = new ArrayList<>();

        boolean sendEmail = true;
        return new ShoppingCartElement(clientId,test,serviceId,discountAmount,quantity,inStore,promotionCode,paymentType,paymentMetadata,payments,sendEmail);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isInStore() {
        return inStore;
    }

    public void setInStore(boolean inStore) {
        this.inStore = inStore;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isConsumerPresent() {
        return consumerPresent;
    }

    public void setConsumerPresent(boolean consumerPresent) {
        this.consumerPresent = consumerPresent;
    }

    public String getPaymentAuthenticationCallBackUrl() {
        return paymentAuthenticationCallBackUrl;
    }

    public void setPaymentAuthenticationCallBackUrl(String paymentAuthenticationCallBackUrl) {
        this.paymentAuthenticationCallBackUrl = paymentAuthenticationCallBackUrl;
    }

    public ArrayList<Integer> getTransactionIds() {
        return transactionIds;
    }

    public void setTransactionIds(ArrayList<Integer> transactionIds) {
        this.transactionIds = transactionIds;
    }

    public HashMap<String, Object> getPaymentMetadata() {
        return paymentMetadata;
    }

    public void setPaymentMetadata(HashMap<String, Object> paymentMetadata) {
        this.paymentMetadata = paymentMetadata;
    }

    public ArrayList<HashMap<String, Object>> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<HashMap<String, Object>> payments) {
        this.payments = payments;
    }
}

