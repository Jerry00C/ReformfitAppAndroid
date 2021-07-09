package com.example.reformfitapp;

import java.io.Serializable;
import java.util.ArrayList;

public class MindbodyLocationModel implements Serializable{

        private String address;
        private String address2;
        private String description;
        private double lat;
        private double lon;
        private String name;
        private String phone;
        private String postalCode;

        public MindbodyLocationModel(String address, String address2, String description, int lat, int lon, String name, String phone, String postalCode) {

                this.address = address;
                this.address2 = address2;
                this.description = description;
                this.lat = lat;
                this.lon = lon;
                this.name = name;
                this.phone = phone;
                this.postalCode = postalCode;
        }

        @Override
        public String toString() {
                return "MindbodyLocationModel{" +
                        ", address='" + address + '\'' +
                        ", address2='" + address2 + '\'' +
                        ", description='" + description + '\'' +
                        ", lat=" + lat +
                        ", lon=" + lon +
                        ", name='" + name + '\'' +
                        ", phone='" + phone + '\'' +
                        ", postalCode='" + postalCode + '\'' +
                        '}';
        }


        public String getAddress() {
                return address;
        }

        public String getAddress2() {
                return address2;
        }

        public String getDescription() {
                return description;
        }

        public double getLat() {
                return lat;
        }

        public double getLon() {
                return lon;
        }

        public String getName() {
                return name;
        }

        public String getPhone() {
                return phone;
        }

        public String getPostalCode() {
                return postalCode;
        }


        public void setAddress(String address) {
                this.address = address;
        }

        public void setAddress2(String address2) {
                this.address2 = address2;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public void setLat(double lat) {
                this.lat = lat;
        }

        public void setLon(double lon) {
                this.lon = lon;
        }

        public void setName(String name) {
                this.name = name;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public void setPostalCode(String postalCode) {
                this.postalCode = postalCode;
        }

        public MindbodyLocationModel() {
        }

}
