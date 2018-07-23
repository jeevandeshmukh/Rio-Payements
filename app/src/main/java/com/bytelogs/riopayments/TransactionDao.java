package com.bytelogs.riopayments;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TransactionDao {

    @Query("select * from TransactionModel")
    LiveData<List<TransactionModel>> getAllTransactionItems();

    @Query("select * from TransactionModel where userUid = :uid")
    UserModel getTransactionItembyId(String uid);

    @Insert(onConflict = REPLACE)
    void addTransaction(TransactionModel transactionModel);

    @Delete
    void deleteTransation(TransactionModel transactionModel);

    @Update
    void updateTransaction(TransactionModel transactionModel);
}
