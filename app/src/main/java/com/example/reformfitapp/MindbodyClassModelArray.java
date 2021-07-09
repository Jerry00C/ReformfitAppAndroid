package com.example.reformfitapp;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MindbodyClassModelArray implements Serializable {

    private static ArrayList<ArrayList<MindbodyClassModel>> models = null;
    private static ArrayList<ArrayList<MindbodyClassModel>> modelsOnline = null;


    public MindbodyClassModelArray() {
        models = new ArrayList<ArrayList<MindbodyClassModel>>();
        modelsOnline = new ArrayList<ArrayList<MindbodyClassModel>>();
        for(int i = 0; i < 14; i++){
            models.add(new ArrayList<MindbodyClassModel>());
            modelsOnline.add(new ArrayList<MindbodyClassModel>());
        }
    }
    @Override
    public String toString() {
        return "MindbodyLocationModel{" +
                ", model='" + models.toString() + '\'' +
                ", modelsOnline='" + modelsOnline.toString() ;

    }


    public ArrayList<ArrayList<MindbodyClassModel>> getModelAll(){
        return models;
    }

    public ArrayList<MindbodyClassModel> getModel(int index){
        return models.get(index);
    }



    public static void setModels(int pos, MindbodyClassModel mindbodyClassModel) {

        long timestamp = mindbodyClassModel.getStartTimestamp();

        for(int index = 0; index < models.get(pos).size(); index++){

            long timestamp2 = models.get(pos).get(index).getStartTimestamp();



           // Log.d("model time", String.valueOf(timestamp));
           // Log.d("sample time", String.valueOf(timestamp2));
            if(timestamp <= timestamp2){
                models.get(pos).add(index, mindbodyClassModel);
                return;
            }


        }
        models.get(pos).add(mindbodyClassModel);
    }

    public ArrayList<ArrayList<MindbodyClassModel>>  getModelOnlineAll(){
        return modelsOnline;
    }

    public ArrayList<MindbodyClassModel> getModelOnline(int index){
        return modelsOnline.get(index);
    }



    public static void setModelsOnline(int pos, MindbodyClassModel mindbodyClassModel) {

        long timestamp = mindbodyClassModel.getStartTimestamp();

        for(int index = 0; index < modelsOnline.get(pos).size(); index++){

            long timestamp2 = modelsOnline.get(pos).get(index).getStartTimestamp();



           // Log.d("model time", String.valueOf(timestamp));
           // Log.d("sample time", String.valueOf(timestamp2));
            if(timestamp <= timestamp2){
                modelsOnline.get(pos).add(index, mindbodyClassModel);
                return;
            }


        }
        modelsOnline.get(pos).add(mindbodyClassModel);
    }





    private long getTimestamp(String dateTime){
        String[] parts = dateTime.split("T");
        String date = parts[0];
        String time = parts[1];


        Date localTime = null;
        try {
            localTime = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss", Locale.getDefault()).parse(date+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localTime.getTime();

    }

}
