package com.example.reformfitapp;

import java.util.HashMap;

public class ClientUpdateElement {

    private HashMap<String,Object> cardInfo;
    private String clientId;
    private boolean crossRegionalUpdate;
    private boolean test;


    public ClientUpdateElement(HashMap<String, Object> cardInfo, String clientId, boolean crossRegionalUpdate, boolean test) {
        this.cardInfo = cardInfo;
        this.clientId = clientId;
        this.crossRegionalUpdate = crossRegionalUpdate;
        this.test = test;
    }

    public HashMap<String,Object> toHashmap(){
        HashMap<String, Object> clientInfo = new HashMap<>();
        clientInfo.put("Id",clientId);
        clientInfo.put("ClientCreditCard",cardInfo);

        HashMap<String,Object>element = new HashMap<>();
        element.put("Client",clientInfo);
        element.put("Test",test);
        element.put("CrossRegionalUpdate",crossRegionalUpdate);

        return element;
    }
}
