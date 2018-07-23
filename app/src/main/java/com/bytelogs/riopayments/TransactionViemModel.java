package com.bytelogs.riopayments;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class TransactionViemModel extends AndroidViewModel {
    private final LiveData<List<TransactionModel>> transList;

    private TransactionDataBase transactionDataBase;

    public TransactionViemModel(@NonNull Application application) {
        super(application);
        transactionDataBase = TransactionDataBase.getDatabase(this.getApplication());
        transList = transactionDataBase.transactionDao().getAllTransactionItems();

    }
    public LiveData<List<TransactionModel>> getUserList() {
        return transList;
    }
    public void insertTransaction(TransactionModel transactionModel) {
        new insertAsyncTask(transactionDataBase).execute(transactionModel);
    }
    public void updateTransaction(TransactionModel transactionModel) {
        new updateAsyncTask(transactionDataBase).execute(transactionModel);
    }
    private static class insertAsyncTask extends AsyncTask<TransactionModel, Void, Void> {

        private TransactionDataBase db;

        insertAsyncTask(TransactionDataBase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final TransactionModel... params) {
            db.transactionDao().addTransaction(params[0]);
            return null;
        }

    }
    private static class updateAsyncTask extends AsyncTask<TransactionModel, Void, Void> {

        private TransactionDataBase db;

        updateAsyncTask(TransactionDataBase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final TransactionModel... params) {
            db.transactionDao().updateTransaction(params[0]);
            return null;
        }

    }

}
