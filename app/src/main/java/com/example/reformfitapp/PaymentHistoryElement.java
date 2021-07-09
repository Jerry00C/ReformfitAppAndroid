package com.example.reformfitapp;

import androidx.annotation.NonNull;

public class PaymentHistoryElement {
    private String title;
    private String purchase_date;
    private String amount;

    public PaymentHistoryElement(String title, String purchase_date, String amount) {
        this.title = title;
        this.purchase_date = purchase_date;
        this.amount = amount;
    }

    public PaymentHistoryElement() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
