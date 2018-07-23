package com.bytelogs.riopayments;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class UserViemModel extends AndroidViewModel {
    private final LiveData<List<UserModel>> userList;
    private UserDataBase userDataBase;
    public UserViemModel(@NonNull Application application) {
        super(application);
        userDataBase = UserDataBase.getDatabase(this.getApplication());
        userList = userDataBase.userModol().getAllUserItems();

    }
    public LiveData<List<UserModel>> getUserList() {
        return userList;
    }
    public void insertUser(UserModel userModel) {
        new deleteAsyncTask(userDataBase).execute(userModel);
    }
    private static class deleteAsyncTask extends AsyncTask<UserModel, Void, Void> {

        private UserDataBase db;

        deleteAsyncTask(UserDataBase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final UserModel... params) {
            db.userModol().addUser(params[0]);
            return null;
        }

    }

}
