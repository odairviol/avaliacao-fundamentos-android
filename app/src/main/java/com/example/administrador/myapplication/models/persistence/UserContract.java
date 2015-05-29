package com.example.administrador.myapplication.models.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.administrador.myapplication.models.entities.User;

/**
 * Created by Administrador on 28/05/2015.
 */
public class UserContract {

    public static final String TABLE = "user_login";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";

    public static final String[] COLUNS = {ID, NAME, PASSWORD};

    public static String createTable() {
        final StringBuilder sql = new StringBuilder();
        sql.append(" CREATE TABLE ");
        sql.append(TABLE);
        sql.append(" ( ");
        sql.append(ID + " INTEGER PRIMARY KEY, ");
        sql.append(NAME + " TEXT, ");
        sql.append(PASSWORD + " TEXT ");
        sql.append(" ); ");
        return sql.toString();
    }

    public static ContentValues getContentValues(User user) {
        ContentValues content = new ContentValues();
        content.put(ID, user.getmId());
        content.put(NAME, user.getmName());
        content.put(PASSWORD, user.getmPassword());
        return content;
    }

    public static User bind(Cursor cursor) {
        if (cursor.moveToNext()) {
            User user = new User();
            user.setmId((cursor.getInt(cursor.getColumnIndex(ID))));
            user.setmName(cursor.getString(cursor.getColumnIndex(NAME)));
            user.setmPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
            return user;
        }
        return null;
    }
}
