package com.example.myapplicationlifesource.model;

import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;

import java.util.ArrayList;
import java.util.Date;

public class User {
    String name, email, phone, bloodType, gender, diseases, hospital;
    ArrayList<String> hospitals;
    Date appointment;
    int age;

    public ArrayList<String> getHospitals() {
        return hospitals;
    }

    public void setHospitals(ArrayList<String> hospitals) {
        this.hospitals = hospitals;
    }

    public int getDonateTime() {
        return donateTime;
    }

    public void setDonateTime(int donateTime) {
        this.donateTime = donateTime;
    }

    int donateTime;
    double weight;

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public Date getAppointment() {
        return appointment;
    }

    public void setAppointment(Date appointment) {
        this.appointment = appointment;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


}
