package com.bytelogs.riopayments;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {UserModel.class}, version = 1,exportSchema = false)
public  abstract class UserDataBase  extends RoomDatabase{


    private static UserDataBase INSTANCE;

    public static UserDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), UserDataBase.class, "user_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract UserDao userModol();
}
