package com.bytelogs.riopayments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected){
            new GlideToast.makeToast(BaseActivity.this, "Ah aa !,No Internet Connection", 3000, GlideToast.CUSTOMTOAST, GlideToast.BOTTOM, R.drawable.ic_info_outline_black_24dp, "#ef5350").show();

        }else {
            //new GlideToast.makeToast(BaseActivity.this, "Back Online", 3000, GlideToast.CUSTOMTOAST, GlideToast.BOTTOM, R.drawable.ic_info_outline_black_24dp, "#9CCC65").show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityReceiver.registerConnectivityBroadcast(this);
        RioPayments.getInstance().setConnectivityListener(this);
    }
}
