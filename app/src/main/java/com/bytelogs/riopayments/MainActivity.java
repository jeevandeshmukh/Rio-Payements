package com.bytelogs.riopayments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements PaymentResultListener,View.OnClickListener {
    FirebaseAuth mAuth;
    TextInputLayout email_layout,mobile_layout,amount_layout;
    EditText emailEt,mobileEt,amountET;
    Button buyBt;
    TransactionViemModel transactionViemModel;
    String uid;
    String email,mobile,amount;
    TransactionModel transactionModel;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Checkout.preload(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        transactionViemModel = ViewModelProviders.of(this).get(TransactionViemModel.class);
        mAuth = FirebaseAuth.getInstance();
        email_layout = findViewById(R.id.email_layout);
        mobile_layout = findViewById(R.id.mobile_layout);
        amount_layout = findViewById(R.id.amount_layout);
        emailEt = findViewById(R.id.email);
        mobileEt = findViewById(R.id.mobile);
        amountET = findViewById(R.id.amount);
        buyBt = findViewById(R.id.buy);
        buyBt.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                mAuth.signOut();
                sendToLogin();
                return true;
            case R.id.action_setttings:
                Intent intent = new Intent(MainActivity.this, SetAccountActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_transation:
                Intent intent1 = new Intent(MainActivity.this, TransactionDetailActivity.class);
                startActivity(intent1);
                return true;
            default:
                return false;
        }

    }
    public void sendToLogin(){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void startPayment(String mobile,String email,int total) {
        this.email = email;
        this.mobile = mobile;

        final Activity activity = this;

        final Checkout co = new Checkout();
        Map<String,Object> transactionMap = new HashMap<>();
        transactionMap.put("email",email);
        transactionMap.put("mobile",mobile);
        transactionMap.put("amount",String.valueOf(total));
        transactionMap.put("status","Initited");
        transactionMap.put("timestamp", FieldValue.serverTimestamp());
        pushTransactionData(transactionMap);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Rio Payments");
            options.put("description", "Bytelogs");
            options.put("image", "");
            options.put("currency", "INR");
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mobile);
            options.put("prefill", preFill);
            co.open(activity, options);
            String amount = String.valueOf(total);
            this.amount = amount;
            transactionModel = new TransactionModel(email,mobile,amount,"Initiated",String.valueOf(uid));
            transactionViemModel.insertTransaction(transactionModel);


        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        transactionModel.setTransationStatus("Success");
        transactionViemModel.updateTransaction(transactionModel);
        Map<String,Object> transactionMap = new HashMap<>();
        transactionMap.put("email",email);
        transactionMap.put("mobile",mobile);
        transactionMap.put("amount",amount);
        transactionMap.put("status","Success");
        transactionMap.put("timestamp", FieldValue.serverTimestamp());
        pushTransactionData(transactionMap);



    }

    @Override
    public void onPaymentError(int i, String s) {
        transactionModel.setTransationStatus("Failed");
        transactionViemModel.updateTransaction(transactionModel);
        Map<String,Object> transactionMap = new HashMap<>();
        transactionMap.put("email",email);
        transactionMap.put("mobile",mobile);
        transactionMap.put("amount",amount);
        transactionMap.put("status","Failed");
        transactionMap.put("timestamp", FieldValue.serverTimestamp());
        pushTransactionData(transactionMap);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.buy:
                startTransaction();
                break;
        }

    }

    private void startTransaction() {
        if(TextUtils.isEmpty(mobileEt.getText().toString())){
            mobile_layout.setError("Enter mobile number");
            return;
        }
        if(TextUtils.isEmpty(emailEt.getText().toString())){
            email_layout.setError("Enter email number");
            return;
        }
        if(TextUtils.isEmpty(amountET.getText().toString())){
            amount_layout.setError("Enter amount number");
            return;
        }
        try {
            Integer.parseInt(amountET.getText().toString());
        }catch (Exception e){
            amount_layout.setError("Enter integers only");
            return;
        }
        startPayment(mobileEt.getText().toString(),emailEt.getText().toString(),Integer.parseInt(amountET.getText().toString()));
    }
    public void pushTransactionData(Map<String,Object> postMap){
        firebaseFirestore.collection("Transaction").document(uid).set(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Transaction Uploaded", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
