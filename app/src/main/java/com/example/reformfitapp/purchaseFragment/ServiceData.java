package com.example.reformfitapp.purchaseFragment;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceData implements Parcelable {

    private double price;
    private double online_price;
    private double tax_included;
    private int programId;
    private double tax_rate;
    private int productId;
    private int serviceId;
    private String serviceName;
    private int count ;
    private String expirationType;
    private String expirationUnit;
    private int expirationLength;
    private String program_name;


    ////////////////////////////// parcelable implementation starts here
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeDouble(price);
        dest.writeDouble(online_price);
        dest.writeDouble(tax_included);
        dest.writeInt(programId);
        dest.writeDouble(tax_rate);
        dest.writeInt(productId);
        dest.writeInt(serviceId);
        dest.writeString(serviceName);
        dest.writeInt(count);
        dest.writeString(expirationType);
        dest.writeString(expirationUnit);
        dest.writeInt(expirationLength);
        dest.writeString(program_name);
    }

    protected ServiceData(Parcel in) {
        price = in.readDouble();
        online_price = in.readDouble();
        tax_included = in.readDouble();
        programId = in.readInt();
        tax_rate = in.readDouble();
        productId = in.readInt();
        serviceId = in.readInt();
        serviceName = in.readString();
        count = in.readInt();
        expirationType = in.readString();
        expirationUnit = in.readString();
        expirationLength = in.readInt();
        program_name = in.readString();
    }

    public static final Creator<ServiceData> CREATOR = new Creator<ServiceData>() {
        @Override
        public ServiceData createFromParcel(Parcel in) {
            return new ServiceData(in);
        }

        @Override
        public ServiceData[] newArray(int size) {
            return new ServiceData[size];
        }
    };
/////////////////////////////////////end of parcelable implementation

    public ServiceData() {
    }

    public ServiceData(double price, int programId, double tax_rate, int serviceId, int count, String program_name) {
        this.price = price;
        this.programId = programId;
        this.tax_rate = tax_rate;
        this.serviceId = serviceId;
        this.count = count;
        this.program_name = program_name;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public void setOnline_price(double online_price) {
        this.online_price = online_price;
    }

    public void setTax_included(double tax_included) {
        this.tax_included = tax_included;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public void setTax_rate(double tax_rate) {
        this.tax_rate = tax_rate;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setExpirationType(String expirationType) {
        this.expirationType = expirationType;
    }

    public void setExpirationUnit(String expirationUnit) {
        this.expirationUnit = expirationUnit;
    }

    public void setExpirationLength(int expirationLength) {
        this.expirationLength = expirationLength;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public double getPrice() {
        return price;
    }

    public double getOnline_price() {
        return online_price;
    }

    public double getTax_included() {
        return tax_included;
    }

    public int getProgramId() {
        return programId;
    }

    public double getTax_rate() {
        return tax_rate;
    }

    public int getProductId() {
        return productId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getCount() {
        return count;
    }

    public String getExpirationType() {
        return expirationType;
    }

    public String getExpirationUnit() {
        return expirationUnit;
    }

    public int getExpirationLength() {
        return expirationLength;
    }

    public String getProgram_name() {
        return program_name;
    }
}
