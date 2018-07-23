package com.bytelogs.riopayments;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity

public class TransactionModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String email;
    public String mobile;
    public String amount;
    public String transationStatus;
    public String userUid;

    public TransactionModel() {
    }

    public TransactionModel(String email, String mobile, String amount, String transationStatus, String userUid) {
        this.email = email;
        this.mobile = mobile;
        this.amount = amount;
        this.transationStatus = transationStatus;
        this.userUid = userUid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransationStatus() {
        return transationStatus;
    }

    public void setTransationStatus(String transationStatus) {
        this.transationStatus = transationStatus;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
