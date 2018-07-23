package com.bytelogs.riopayments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.util.ArrayList;
import java.util.List;

public class TransactionDetailActivity extends BaseActivity {

    RecyclerView recyclerView;
    TransactionViemModel transactionViemModel;
    TransactionAdapter transactionAdapter;
    LinearLayoutManager linearLayoutManager;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String uid;
    List<TransactionModel> transactionModelsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid().toString();
        recyclerView = findViewById(R.id.recycler);
        transactionModelsList  =new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        transactionViemModel = ViewModelProviders.of(this).get(TransactionViemModel.class);
        if(!ConnectivityReceiver.isConnected()){
            retriveOfflineTransacionData();
        }else {
            retriveOnlineTrnasactionData();

        }
    }
    public void retriveOfflineTransacionData(){
        Toast.makeText(this,"In",Toast.LENGTH_SHORT).show();
        transactionViemModel.getUserList().observe(this, new Observer<List<TransactionModel>>() {
            @Override
            public void onChanged(@Nullable List<TransactionModel> transactionModels) {
                transactionAdapter = new TransactionAdapter(transactionModels,TransactionDetailActivity.this);

                recyclerView.setAdapter(transactionAdapter);
                transactionAdapter.notifyDataSetChanged();
                new GlideToast.makeToast(TransactionDetailActivity.this, "Your Offline,Showing you cached data", 3000, GlideToast.CUSTOMTOAST, GlideToast.BOTTOM, R.drawable.ic_info_outline_black_24dp, "#ef5350").show();

            }
        });
    }
    public void retriveOnlineTrnasactionData(){
        Query firebaseQuery = firebaseFirestore.collection("Transaction").orderBy("timestamp",Query.Direction.DESCENDING);
        firebaseQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        TransactionModel transactionModel = documentChange.getDocument().toObject(TransactionModel.class);
                        transactionModelsList.add(transactionModel);
                    }
                }
                if(transactionModelsList !=null) {
                    transactionAdapter = new TransactionAdapter(transactionModelsList, TransactionDetailActivity.this);
                    recyclerView.setAdapter(transactionAdapter);
                    transactionAdapter.notifyDataSetChanged();
                }


            }
        });

    }
}
