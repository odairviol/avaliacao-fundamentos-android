package com.example.administrador.myapplication.models.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrador.myapplication.models.entities.User;
import com.example.administrador.myapplication.util.AppUtil;

/**
 * Created by Administrador on 28/05/2015.
 */
public final class UserRepository {

    private static class Singleton {
        public static final UserRepository INSTANCE = new UserRepository();
    }

    private UserRepository() {
        super();
    }

    public static UserRepository getInstance() {
        return Singleton.INSTANCE;
    }

    public void save(User user) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (user.getmId() == null) {
            db.insert(ServiceOrderContract.TABLE, null, UserContract.getContentValues(user));
        }
        db.close();
        helper.close();
    }

    public boolean validUser(String name, String pass) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] args = {name, pass};
        String clausula = UserContract.NAME + "= ? and " + UserContract.PASSWORD + "= ? ";
        Cursor cursor = db.query(UserContract.TABLE, UserContract.COLUNS, clausula, args, null, null, UserContract.NAME);
        boolean isValid = cursor.moveToNext();
        cursor.close();
        db.close();
        helper.close();
        return isValid;
    }
}
