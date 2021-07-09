package com.example.reformfitapp.expandedFunc;

public class YongjiuReportModel{

    private String id;
    private String weight;
    private String bodyFat;
    private String muscleAmt;
    private String muscleIndex;
    private String reportTime;


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
        this.reportTime = reportTime;
    }


    @Override
    public String toString() {
        return "YongjiuReportModel{" +
                "id='" + id + '\'' +
                ", weight='" + weight + '\'' +
                ", bodyFat='" + bodyFat + '\'' +
                ", muscleAmt='" + muscleAmt + '\'' +
                ", muscleIndex='" + muscleIndex + '\'' +
                ", reportTime='" + reportTime + '\'' +
                '}';
    }
}
