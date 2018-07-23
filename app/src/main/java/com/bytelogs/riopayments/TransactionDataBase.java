package com.bytelogs.riopayments;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {TransactionModel.class}, version = 1,exportSchema = false)
public  abstract class TransactionDataBase extends RoomDatabase{


    private static TransactionDataBase INSTANCE;

    public static TransactionDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), TransactionDataBase.class, "transaction_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract TransactionDao transactionDao();
}
