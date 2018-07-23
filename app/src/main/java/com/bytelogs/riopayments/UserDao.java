package com.bytelogs.riopayments;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Query("select * from UserModel")
    LiveData<List<UserModel>> getAllUserItems();

    @Query("select * from UserModel where uid = :uid")
    UserModel getItembyId(String uid);

    @Insert(onConflict = REPLACE)
    void addUser(UserModel userModel);

    @Delete
    void deleteUser(UserModel UserModel);
}
