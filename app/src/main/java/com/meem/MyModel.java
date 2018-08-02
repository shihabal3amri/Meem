package com.meem;

import android.os.Parcel;
import android.os.Parcelable;

public class MyModel implements Parcelable{
    private MyModel en;
    private MyModel ar;
    private String name;
    private String email;
    private String phoneNo;
    private String gender;
    private String userId;
    private String userType;
    private String serviceName;
    private String serviceId;
    private String areaName;
    private String qrCode;
    private String requestId;
    private Boolean isAccepted;
    private String date;
    private String time;
    private  Integer statusCode;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    protected MyModel(Parcel in) {
        en = in.readParcelable(MyModel.class.getClassLoader());
        ar = in.readParcelable(MyModel.class.getClassLoader());
        name = in.readString();
        email = in.readString();
        phoneNo = in.readString();
        gender = in.readString();
        userId = in.readString();
        userType = in.readString();
        serviceName = in.readString();
        serviceId = in.readString();
        areaName = in.readString();
        qrCode = in.readString();
        requestId = in.readString();
    }
    public MyModel() {

    }

    public static final Creator<MyModel> CREATOR = new Creator<MyModel>() {
        @Override
        public MyModel createFromParcel(Parcel in) {
            return new MyModel(in);
        }

        @Override
        public MyModel[] newArray(int size) {
            return new MyModel[size];
        }
    };

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public MyModel getEn() {
        return en;
    }

    public void setEn(MyModel en) {
        this.en = en;
    }

    public MyModel getAr() {
        return ar;
    }

    public void setAr(MyModel ar) {
        this.ar = ar;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(en, i);
        parcel.writeParcelable(ar, i);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(phoneNo);
        parcel.writeString(gender);
        parcel.writeString(userId);
        parcel.writeString(userType);
        parcel.writeString(serviceName);
        parcel.writeString(serviceId);
        parcel.writeString(areaName);
        parcel.writeString(qrCode);
        parcel.writeString(requestId);
    }
}
