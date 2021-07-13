package com.example.reformfitapp.expandedFunc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class YongjiuReportModel{

    private String id;
    private String weight;
    private String bodyFat;
    private String muscleAmt;
    private String muscleIndex;
    private String reportTime;
    private int gender;


    public YongjiuReportModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(String bodyFat) {
        this.bodyFat = bodyFat;
    }

    public String getMuscleAmt() {
        return muscleAmt;
    }

    public void setMuscleAmt(String muscleAmt) {
        this.muscleAmt = muscleAmt;
    }

    public String getMuscleIndex() {
        return muscleIndex;
    }

    public void setMuscleIndex(String muscleIndex) {
        this.muscleIndex = muscleIndex;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        Date localTime = null;

        try {
            localTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(reportTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        this.reportTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(localTime);

    }


    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {


        this.gender = gender;
    }
}
