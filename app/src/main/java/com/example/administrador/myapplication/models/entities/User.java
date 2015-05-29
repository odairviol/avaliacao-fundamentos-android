package com.example.administrador.myapplication.models.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.administrador.myapplication.models.persistence.UserRepository;

/**
 * Created by Administrador on 28/05/2015.
 */
public class User implements Parcelable {
    private Integer mId;
    private String mName;
    private String mPassword;

    public Integer getmId() { return mId;}

    public void setmId(Integer mId) { this.mId = mId; }

    public String getmName() { return mName;  }

    public void setmName(String mName) { this.mName = mName; }

    public String getmPassword() { return mPassword; }

    public void setmPassword(String mPassword) { this.mPassword = mPassword; }

    public void save() {
        UserRepository.getInstance().save(this);
    }

    public static boolean validUser(String name, String pass) {
        return UserRepository.getInstance().validUser(name, pass);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mPassword);
    }

    public User() {
    }

    private User(Parcel in) {
        this.mId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mName = in.readString();
        this.mPassword = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
