package com.example.reformfitapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MindbodyClassModel implements Parcelable {



    private int classScheduleId;
    private String className;

    private int classId;

    private String startDate;
    private String startDateCut;
    private String startTime;
    private String startTimeCut;
    private String endDate;   /* 2019-01-11T19:00:00",*/
    private String endDateCut;
    private String endTime;   /* 2019-01-11T19:00:00",*/
    private String endTimeCut;

    private String programName;

    private int maxCapacity;
    private int totalBooked;
    private int totalBookedWaitlist;

    private boolean isWaitlistAvailable;
    private boolean isAvailable;
    private boolean isCancel;


    private boolean waitlist;
    private int wailistEntryId;
    private int wailistOrder;

    private long startTimestamp;
    private long endTImeStamp;

    private int cancelOffset;

    private String virtualStreamLink;


    private String staff_mageUrl;
    private String staff_name;
    private String staff_des;

    private String address;
    private String description;

    public MindbodyClassModel(){

    }


    protected MindbodyClassModel(Parcel in) {
        classScheduleId = in.readInt();
        className = in.readString();
        classId = in.readInt();
        startDate = in.readString();
        startDateCut = in.readString();
        startTime = in.readString();
        startTimeCut = in.readString();
        endDate = in.readString();
        endDateCut = in.readString();
        endTime = in.readString();
        endTimeCut = in.readString();
        programName = in.readString();
        maxCapacity = in.readInt();
        totalBooked = in.readInt();
        totalBookedWaitlist = in.readInt();
        isWaitlistAvailable = in.readByte() != 0;
        isAvailable = in.readByte() != 0;
        isCancel = in.readByte() != 0;
        waitlist = in.readByte() != 0;
        wailistEntryId = in.readInt();
        wailistOrder = in.readInt();
        startTimestamp = in.readLong();
        endTImeStamp = in.readLong();
        cancelOffset = in.readInt();
        virtualStreamLink = in.readString();
        staff_mageUrl = in.readString();
        staff_name = in.readString();
        staff_des = in.readString();
        address = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(classScheduleId);
        dest.writeString(className);
        dest.writeInt(classId);
        dest.writeString(startDate);
        dest.writeString(startDateCut);
        dest.writeString(startTime);
        dest.writeString(startTimeCut);
        dest.writeString(endDate);
        dest.writeString(endDateCut);
        dest.writeString(endTime);
        dest.writeString(endTimeCut);
        dest.writeString(programName);
        dest.writeInt(maxCapacity);
        dest.writeInt(totalBooked);
        dest.writeInt(totalBookedWaitlist);
        dest.writeByte((byte) (isWaitlistAvailable ? 1 : 0));
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        dest.writeByte((byte) (isCancel ? 1 : 0));
        dest.writeByte((byte) (waitlist ? 1 : 0));
        dest.writeInt(wailistEntryId);
        dest.writeInt(wailistOrder);
        dest.writeLong(startTimestamp);
        dest.writeLong(endTImeStamp);
        dest.writeInt(cancelOffset);
        dest.writeString(virtualStreamLink);
        dest.writeString(staff_mageUrl);
        dest.writeString(staff_name);
        dest.writeString(staff_des);
        dest.writeString(address);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MindbodyClassModel> CREATOR = new Creator<MindbodyClassModel>() {
        @Override
        public MindbodyClassModel createFromParcel(Parcel in) {
            return new MindbodyClassModel(in);
        }

        @Override
        public MindbodyClassModel[] newArray(int size) {
            return new MindbodyClassModel[size];
        }
    };

    public boolean isWaitlistAvailable() {
        return isWaitlistAvailable;
    }

    public void setWaitlistAvailable(boolean waitlistAvailable) {
        isWaitlistAvailable = waitlistAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getClassScheduleId() {
        return classScheduleId;
    }

    public void setClassScheduleId(int classScheduleId) {
        this.classScheduleId = classScheduleId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDateandTime(String startDate) {
        String[] parts = startDate.split("T");
        String date = parts[0];
        String time = parts[1];

        this.startDate = date;
        this.startTime = time;

        Date localTime = null;
        try {
            localTime = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault()).parse(date+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        startTimestamp = localTime.getTime();


        startTimeCut = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(localTime);
        startDateCut = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(localTime);


       // Log.d("endTimeCut", startTimeCut);
        //Log.d("endDateCut", startDateCut);


    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public String getStartTime() {
        return startTime;
    }


    public String getEndDate() {
        return endDate;
    }

    public void setEndDateandTime(String endDate){
        String[] parts = endDate.split("T");
        String date = parts[0];
        String time = parts[1];

        this.endDate = date;
        this.endTime = time;

        Date localTime = null;

        try {
           localTime = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault()).parse(date+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        endTImeStamp = localTime.getTime();

        endTimeCut = new SimpleDateFormat("HH:mmaa", Locale.getDefault()).format(localTime);
        endTimeCut = endTimeCut.replace("AM", "am").replace("PM","pm");

        endDateCut = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault()).format(localTime);

        //Log.d("endTimeCut", endTimeCut);
        //Log.d("endDateCut", endDateCut);


    }


    public String getEndTime() {
        return endTime;
    }

    public String getStartDateCut() {
        return startDateCut;
    }

    public String getStartTimeCut() {
        return startTimeCut;
    }

    public String getEndDateCut() {
        return endDateCut;
    }

    public String getEndTimeCut() {
        return endTimeCut;
    }


    public long getEndTImeStamp() {
        return endTImeStamp;
    }


    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getTotalBooked() {
        return totalBooked;
    }

    public void setTotalBooked(int totalBooked) {
        this.totalBooked = totalBooked;
    }

    public int getTotalBookedWaitlist() {
        return totalBookedWaitlist;
    }

    public void setTotalBookedWaitlist(int totalBookedWaitlist) {
        this.totalBookedWaitlist = totalBookedWaitlist;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public boolean isWaitlist() {
        return waitlist;
    }

    public void setWaitlist(boolean waitlist) {
        this.waitlist = waitlist;
    }

    public int getWailistEntryId() {
        return wailistEntryId;
    }

    public void setWailistEntryId(int wailistEntryId) {
        this.wailistEntryId = wailistEntryId;
    }

    public int getWailistOrder() {
        return wailistOrder;
    }

    public void setWailistOrder(int wailistOrder) {
        this.wailistOrder = wailistOrder;
    }

    public int getCancelOffset() {
        return cancelOffset;
    }

    public void setCancelOffset(int cancelOffset) {
        this.cancelOffset = cancelOffset;
    }


    public String getVirtualStreamLink() {
        return virtualStreamLink;
    }

    public void setVirtualStreamLink(String virtualStreamLink) {
        this.virtualStreamLink = virtualStreamLink;
    }

    public String getStaff_mageUrl() {
        return staff_mageUrl;
    }

    public void setStaff_mageUrl(String staff_mageUrl) {
        this.staff_mageUrl = staff_mageUrl;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_des() {
        return staff_des;
    }

    public void setStaff_des(String staff_des) {
        this.staff_des = staff_des;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MindbodyClassModel{" +
                "classScheduleId=" + classScheduleId +
                ", className='" + className + '\'' +
                ", classId=" + classId +
                ", startDate='" + startDate + '\'' +
                ", startDateCut='" + startDateCut + '\'' +
                ", startTime='" + startTime + '\'' +
                ", startTimeCut='" + startTimeCut + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endDateCut='" + endDateCut + '\'' +
                ", endTime='" + endTime + '\'' +
                ", endTimeCut='" + endTimeCut + '\'' +
                ", isCancel='" + isCancel + '\'' +
//                ", mindbodyClassDescriptionModel=" + mindbodyClassDescriptionModel.toString() +
                ", programName='" + programName + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", totalBooked=" + totalBooked +
                ", totalBookedWaitlist=" + totalBookedWaitlist +
                ", isWaitlistAvailable=" + isWaitlistAvailable +
                ", waitlist=" + waitlist +
                ", wailistEntryId=" + wailistEntryId +
                ", startTimestamp=" + startTimestamp +
                ", endTImeStamp=" + endTImeStamp +
                ", cancelOffset=" + cancelOffset +
                '}';
    }



}
