package com.example.reformfitapp.purchaseFragment;

import android.os.Parcel;
import android.os.Parcelable;

public class ContractData  implements Parcelable {

    private int contract_id;
    private double first_payment_subtotal;
    private double first_payment_tax;
    private double first_payment_total;
    private double recurring_payment_subtotal;
    private double recurring_payment_tax;
    private double recurring_payment_total;
    private String agreement_terms;
    private String name;
    private int serviceId;
///////////////////////////// parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(contract_id);
        dest.writeDouble(first_payment_subtotal);
        dest.writeDouble(first_payment_tax);
        dest.writeDouble(first_payment_total);
        dest.writeDouble(recurring_payment_subtotal);
        dest.writeDouble(recurring_payment_tax);
        dest.writeDouble(recurring_payment_total);
        dest.writeString(agreement_terms);
        dest.writeString(name);
        dest.writeInt(serviceId);
    }

    protected ContractData(Parcel in) {
        contract_id = in.readInt();
        first_payment_subtotal = in.readDouble();
        first_payment_tax = in.readDouble();
        first_payment_total = in.readDouble();
        recurring_payment_subtotal = in.readDouble();
        recurring_payment_tax = in.readDouble();
        recurring_payment_total = in.readDouble();
        agreement_terms = in.readString();
        name = in.readString();
        serviceId = in.readInt();
    }

    public static final Creator<ContractData> CREATOR = new Creator<ContractData>() {
        @Override
        public ContractData createFromParcel(Parcel in) {
            return new ContractData(in);
        }

        @Override
        public ContractData[] newArray(int size) {
            return new ContractData[size];
        }
    };
    ///////////////// end of parcelable


    public ContractData() {
    }

    public double getFirst_payment_subtotal() {
        return first_payment_subtotal;
    }

    public void setFirst_payment_subtotal(double first_payment_subtotal) {
        this.first_payment_subtotal = first_payment_subtotal;
    }

    public double getFirst_payment_tax() {
        return first_payment_tax;
    }

    public void setFirst_payment_tax(double first_payment_tax) {
        this.first_payment_tax = first_payment_tax;
    }

    public double getFirst_payment_total() {
        return first_payment_total;
    }

    public void setFirst_payment_total(double first_payment_total) {
        this.first_payment_total = first_payment_total;
    }

    public double getRecurring_payment_subtotal() {
        return recurring_payment_subtotal;
    }

    public void setRecurring_payment_subtotal(double recurring_payment_subtotal) {
        this.recurring_payment_subtotal = recurring_payment_subtotal;
    }

    public double getRecurring_payment_tax() {
        return recurring_payment_tax;
    }

    public void setRecurring_payment_tax(double recurring_payment_tax) {
        this.recurring_payment_tax = recurring_payment_tax;
    }

    public double getRecurring_payment_total() {
        return recurring_payment_total;
    }

    public void setRecurring_payment_total(double recurring_payment_total) {
        this.recurring_payment_total = recurring_payment_total;
    }

    public String getAgreement_terms() {
        return agreement_terms;
    }

    public void setAgreement_terms(String agreement_terms) {
        this.agreement_terms = agreement_terms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public ContractData(int contract_id,double first_payment_subtotal, double first_payment_tax, double first_payment_total, double recurring_payment_subtotal, double recurring_payment_tax, double recurring_payment_total, String agreement_terms, String name) {
        this.contract_id = contract_id;
        this.first_payment_subtotal = first_payment_subtotal;
        this.first_payment_tax = first_payment_tax;
        this.first_payment_total = first_payment_total;
        this.recurring_payment_subtotal = recurring_payment_subtotal;
        this.recurring_payment_tax = recurring_payment_tax;
        this.recurring_payment_total = recurring_payment_total;
        this.agreement_terms = agreement_terms;
        this.name = name;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }
}
